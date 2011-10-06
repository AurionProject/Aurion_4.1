# This should be executed from the NHIN source directory
ANT_OPTS="-Xmx872m -XX:MaxPermSize=640m -Dcom.sun.aas.instanceName=server"
NHINC_SOURCE_DIR=`pwd`
ANT_HOME="$NHINC_SOURCE_DIR"/Build/Packages/Ant/Ant
NHINC_PROPERTIES_DIR=/nhin/SUNWappserver/domains/domain1/config/nhin
JAVA_HOME=/opt/java
PATH=$ANT_HOME/bin:$PATH:$JAVA_HOME/bin:/sbin:$AS_HOME/bin
NHINC_THIRDPARTY_DIR="$NHINC_SOURCE_DIR"/ThirdParty
MYSQL_HOME=/nhin/mysql-5.1.42-linux-i686-glibc23
AS_HOME=/opt/glassfish
export ANT_OPTS ANT_HOME IDE_HOME AS_HOME NHINC_SOURCE_DIR NHINC_PROPERTIES_DIR NHINC_THIRDPARTY_DIR NHINC_THIRDPARTY_DIR JAVA_HOME MYSQL_HOME PATH
