#!/bin/bash
mkdir -p build
cd src
javac -d ../build cybercat3/currency_converter/CurrencyConverter.java
echo Compiled.
cd ../build
jar cfe CurrencyConverter.jar cybercat3.currency_converter.CurrencyConverter *
echo Packaged. "(.jar is in build directory)"