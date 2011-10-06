call %CD%\4.1\Build\hudsonenv.bat
cd %NHINC_SOURCE_DIR%\Product
ant -DBUILD_NUMBER=%BUILD_NUMBER% package.create unittest.run copy.deployable.artifacts publish.create


