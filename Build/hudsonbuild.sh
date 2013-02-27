pwd
set -x
cd 4.1
. Build/sethudsonenv.sh
cd "$NHINC_SOURCE_DIR/Product"
env | sort
type ant
sh "$ANT_HOME/bin/ant" -DBUILD_NUMBER=$BUILD_NUMBER clean package.create unittest.run copy.deployable.artifacts publish.create

