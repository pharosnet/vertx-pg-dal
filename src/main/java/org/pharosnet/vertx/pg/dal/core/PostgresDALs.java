package org.pharosnet.vertx.pg.dal.core;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgPool;
import org.pharosnet.vertx.pg.dal.core.annotations.Dal;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PostgresDALs {

    private static final Logger log = LoggerFactory.getLogger(PostgresDALs.class);

    private static Map<String, Object> dals = new ConcurrentHashMap<>();

    public static void INIT(PgPool pgPool, String basePackage) throws Exception {
        PostgresDAL.INTI(pgPool);
        Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(basePackage.replace(".", "/"));
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            if (url.getProtocol().equals("jar")) {
                scanPackage(url);
            } else {
                File file = new File(url.toURI());
                if (!file.exists()) {
                    continue;
                }
                scanPackage(basePackage, file);
            }
        }
    }

    private static void scanPackage(String packageName, File currentFile) throws Exception {
        File[] fileList = currentFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                return pathname.getName().endsWith(".class");
            }
        });
        for (File file : fileList) {
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName(), file);
            } else {
                String fileName = file.getName().replace(".class", "");
                String className = packageName + "." + fileName;
                Class<?> clazz = Class.forName(className);
                if (!clazz.isInterface()) {
                    continue;
                }
                newDAL(clazz);
            }
        }
    }

    private static void scanPackage(URL url) throws Exception {
        JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = urlConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if (jarEntry.isDirectory() || !jarEntryName.endsWith(".class")) {
                continue;
            }
            String className = jarEntryName.replace(".class", "").replace("/", ".");
            Class<?> clazz = Class.forName(className);
            if (!clazz.isInterface()) {
                continue;
            }
            newDAL(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private static void newDAL(Class<?> clazz) throws Exception {
        if (!clazz.isAnnotationPresent(Dal.class)) {
            return;
        }
        String className = String.format("%sImpl", clazz.getName());
        if (dals.containsKey(className)) {
            return;
        }
        Class implClass = PostgresDALs.class.getClassLoader().loadClass(className);
        Object impl = implClass.getConstructor(PgPool.class).newInstance(PostgresDAL.get().getClient());
        dals.put(className, impl);
    }

    @SuppressWarnings("unchecked")
    public static <R> R get(Class<R> clazz) {
        if (PostgresDAL.get() == null || PostgresDAL.get().getClient() == null) {
            log.warn("postgres dal get failed, cause PostgresDAL has no instance, please call PostgresDAL.INIT() first");
            return null;
        }

        String className = String.format("%sImpl", clazz.getName());
        if (dals.containsKey(className)) {
            return (R) dals.get(className);
        }
        try {
            Class implClass = PostgresDALs.class.getClassLoader().loadClass(className);
            R impl = (R) implClass.getConstructor(PgPool.class).newInstance(PostgresDAL.get().getClient());
            dals.put(className, impl);
            return impl;
        } catch (Exception e) {
            log.error("get postgres dal failed", e);
            return null;
        }
    }

}
