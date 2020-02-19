package cybercat3.currency_converter;

import java.util.*;
import cybercat3.currency_converter.Currency;

public class CurrencyConverter {

    public static void main(String... args) throws Exception {
        System.out.print("Loading currencies... ");
        Currency.intialiseCurrencies();
        System.out.println(" Done");

        System.out.println("=====Currency Converter=====");
        System.out.println("Syntax:");
        System.out.println("\t<amount> <source currency> TO <target currency>");
        System.out.println("\nExample:");
        System.out.println("\t50 EUR TO GBP\n");

        Scanner scanner = new Scanner(System.in);

        String line;

        while ((line = scanner.nextLine()) != null) {

            switch (line.trim().toLowerCase().charAt(0)) {
                case 'e':   case 'q':   case 's': // exit, quit or stop.
                    return;
            }

            line = line.toUpperCase();
            int toIndex = line.indexOf(" TO ");

            try {
                String firstPart = line.substring(0, toIndex);
                String secondPart = line.substring(toIndex + 4);
    
                String[] firstPartSegments = firstPart.split(" ");
    
                double amount = Double.parseDouble(firstPartSegments[0]);
                Currency source = Currency.getByName(firstPartSegments[1]);
    
                Currency target = Currency.getByName(secondPart);
    
                System.out.println(" ->  " + source.convert(amount, target) + secondPart.trim() + "\n");
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Invalid Index.");
            }

        }

        // No reason to do this, but it makes VSCode happy.
        // Not closing a scanner is, in fact, not a resource leak in this case.
        // The scanner reads from the underlying ressource, which
        // in this case is System.in. When we close the scanner,
        // the close call will propogate and close System.in aswell,
        // which means we can't read from it in the future.
        scanner.close();
    }
}