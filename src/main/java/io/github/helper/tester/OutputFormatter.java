package io.github.helper.tester;

/**
 * Utility per normalizzare l'output prodotto dal codice testato prima del confronto
 * con l'output atteso. Serve principalmente a gestire in modo uniforme i numeri
 * decimali (es. "10" e "10.0" devono essere considerati equivalenti).
 */
public final class OutputFormatter {

    private OutputFormatter() {
        // utility class
    }

    public static String normalize(String raw) {
        try {
            double val = Double.parseDouble(raw);
            if (val == (long) val) {
                return (long) val + ".0";
            }
            return String.valueOf(val);
        } catch (NumberFormatException e) {
            return raw;
        }
    }
}