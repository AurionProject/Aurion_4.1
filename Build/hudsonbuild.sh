pwd
set -x
cd Current
. Build/sethudsonenv.sh
cd "$NHINC_SOURCE_DIR/Product"
env | sort
type ant
sh "$ANT_HOME/bin/ant" clean package.create

