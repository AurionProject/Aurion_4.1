CLASSPATH=:.:./lib/AurionCoreLib.jar:./lib/c3p0-0.9.1.2.jar:./lib/antlr-2.7.6.jar:./lib/asm.jar:./lib/asm-attrs.jar:./lib/cglib-2.1.3.jar:./lib/commons-collections-2.1.1.jar:./lib/commons-logging-1.1.1.jar:./lib/dom4j-1.6.1.jar:./lib/ehcache-1.2.3.jar:./lib/hibernate3.jar:./lib/jdbc2_0-stdext.jar:./lib/jta.jar:./lib/log4j-1.2.15.jar:./lib/mysql-connector-java-5.1.8-bin.jar
export CLASSPATH
java -Dnhinc.properties.dir=/home/yourusername/docload/config org.alembic.aurion.docrepository.adapter.util.DocumentLoadUtil

