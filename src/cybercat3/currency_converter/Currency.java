package cybercat3.currency_converter;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;

public class Currency {
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