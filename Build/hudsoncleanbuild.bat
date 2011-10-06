call %CD%\Current\Build\hudsonenv.bat
cd %NHINC_SOURCE_DIR%\Product
ant -DBUILD_NUMBER=%BUILD_NUMBER% clean package.create unittest.run copy.deployable.artifacts publish.create


