package cybercat3.currency_converter;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;

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

class Currency {
    public final String fullName;
    public final String isoName;
    public final double rate;

    private static Map<String, Currency> currencyMap;

    public static void intialiseCurrencies() throws IOException {
        currencyMap = new HashMap<>();

        // Read the NationalBank's currency rate from their website into a string.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = new URL("http://www.nationalbanken.dk/valutakurser").openStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }
        String html = baos.toString();

        // Parses the html with a regex and extracts the currencies.
        Pattern regex = Pattern
                .compile("<td><strong>(.+?)</strong></td>\\s*<td>(.*?)</td>\\s*<td class=\"text-right\">(.*?)</td>");
        Matcher m = regex.matcher(html);

        while (m.find()) {
            String fullName = m.group(1).replaceAll("\\s", "").toLowerCase();
            String isoName = m.group(2).toLowerCase();
            double rate = Double.parseDouble(m.group(3).replace(",", "."));

            Currency currency = new Currency(fullName, isoName, rate);
            currencyMap.put(fullName, currency);
            currencyMap.put(isoName, currency);
        }

        // Add the danishKrone, which is the baseline.
        Currency danishKrone = new Currency("Danske kroner", "DKK", 100);
        currencyMap.put("danskekroner", danishKrone);
        currencyMap.put("dkk", danishKrone);
    }

    public static Currency getByName(String name) {
        return currencyMap.get(name.trim().toLowerCase());
    }

    private Currency(String fullName, String isoName, double rate) {
        this.fullName = fullName;
        this.isoName = isoName;
        this.rate = rate;
    }

    public double convert(double amount, Currency other) {
        return amount * (this.rate / 100) / (other.rate / 100);
    }

    @Override
    public String toString() {
        return "{" +
            " fullName='" + fullName + "'" +
            ", isoName='" + isoName + "'" +
            ", rate='" + rate + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Currency)) {
            return false;
        }
        Currency currency = (Currency) o;
        return Objects.equals(fullName, currency.fullName) && Objects.equals(isoName, currency.isoName) && rate == currency.rate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, isoName, rate);
    }
}