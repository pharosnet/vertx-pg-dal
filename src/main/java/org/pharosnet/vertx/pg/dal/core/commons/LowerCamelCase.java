package org.pharosnet.vertx.pg.dal.core.commons;

import java.util.Iterator;

public class LowerCamelCase extends Case {

    @Override
    public String name() {
        return "LOWER_CAMEL";
    }

    @Override
    public String format(Iterable<String> atoms) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = atoms.iterator();
        while (it.hasNext()) {
            String atom = it.next();
            if (atom.length() > 0) {
                sb.append(atom.toLowerCase());
                break;
            }
        }
        while (it.hasNext()) {
            String atom = it.next();
            if (atom.length() > 0) {
                char c = atom.charAt(0);
                if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                    sb.append(atom, 1, atom.length());
                } else {
                    sb.append(atom);
                }
            }
        }
        return sb.toString();
    }
}