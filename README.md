# CurrencyConverter
## The What
CurrencyConverter is a simple command line tool that can convert currencies.
Syntax for conversion is:

`<Source Currency> <Currency Amount> TO <Target Currency>`

The currencies are referenced by their ISO name.

## The How
The project is written in Java 8 and gets the currency exchange rates from the [Danish National Bank](http://www.nationalbanken.dk/valutakurser).

There are build scripts included in the repo, both for batch and bash.
Java 8 or higher is required.
