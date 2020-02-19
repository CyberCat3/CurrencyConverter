@echo off
if not exist build mkdir build
javac -d build src/cybercat3/currency_converter/CurrencyConverter.java
echo Compiled.
cd build
jar cfe CurrencyConverter.jar cybercat3.currency_converter.CurrencyConverter *
echo Packaged. (.jar is in build directory)