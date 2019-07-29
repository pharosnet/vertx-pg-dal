# vertx-pg-dal
Postgres database access layer, base on vertx-pg-client (3.8.0). 

## Features

* asynchrony
* simple
* no reflect-code cost



## usage

* add dependency

  ```xml
  <dependency>
  	<groupId>org.pharosnet</groupId>
  	<artifactId>vertx-pg-dal</artifactId>
  	<version>1.0.0</version>
  </dependency>
  ```

  

* update pom.xml

  ```xml
  <plugin>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.8.0</version>
      <configuration>
          <source>11</source>
          <target>11</target>
          <encoding>UTF-8</encoding>
          <verbose>true</verbose>
          <generatedSourcesDirectory>src/main/generated</generatedSourcesDirectory><generatedTestSourcesDirectory>src/test/generated</generatedTestSourcesDirectory>
      </configuration>
      <executions>
          <execution>
              <id>default-compile</id>
              <configuration>
                  <annotationProcessors>
                      <annotationProcessor>io.vertx.codegen.CodeGenProcessor</annotationProcessor>
                      <annotationProcessor>org.pharosnet.vertx.pg.dal.gen.PgCodeGenProcessor</annotationProcessor>
                  </annotationProcessors>
                  <generatedSourcesDirectory>src/main/generated</generatedSourcesDirectory>
                  <compilerArgs>
                      <arg>-Acodegen.output=${project.basedir}/src/main</arg>
                  </compilerArgs>
              </configuration>
          </execution>
          <execution>
              <id>default-testCompile</id>
              <configuration>
                  <annotationProcessors>
                      <annotationProcessor>io.vertx.codegen.CodeGenProcessor</annotationProcessor>
                      <annotationProcessor>org.pharosnet.vertx.pg.dal.gen.PgCodeGenProcessor</annotationProcessor>
                  </annotationProcessors>
                  <generatedTestSourcesDirectory>src/test/generated</generatedTestSourcesDirectory>
                  <compilerArgs>
                      <arg>-Acodegen.output=${project.basedir}/src/test</arg>
                  </compilerArgs>
              </configuration>
          </execution>
      </executions>
  </plugin>
  ```

* CREATE A TABLE ROW CLASS

  ```JAVA
  @Table(schema = "CDST", name = "USER")
  public class User extends AbstractRow {
  
      @Column(name = "ID", kind = ColumnKind.ID)
      private String id;
  
      @Column(name = "data")
      private String name;
  
  
      public String getId() {
          return id;
      }
  
      public void setId(String id) {
          this.id = id;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  }
  
  ```

  

* CREATE A DAL INTERFACE

  ```java
  @Dal
  public interface UserDAL {
  
      @Query("select * from \"cdst\".\"user\" where \"id\" = $1 or \"id\" = $2 offset $3 limit $4")
      void query01(
              @Arg({1, 2}) String id,
              @Arg({3})Integer offset,
              @Arg({4})Integer length,
              Handler<AsyncResult<Optional<Stream<User>>>> handler);
  
      @Query("select * from \"cdst\".\"user\" where \"id\" = $1 ")
      void query02(
              @Arg({1}) String id,
              @Arg({2})Integer offset,
              @Arg({3})Integer length,
              Handler<AsyncResult<Optional<User>>> handler);
  
  }
  
  ```

  

* RUN MAVEN COMPILE, THEN IT WILL GENERATE DAL JAVA FILES. 

* HAPPY TO USE THEM.

## THANKS

* vertx-pg-client