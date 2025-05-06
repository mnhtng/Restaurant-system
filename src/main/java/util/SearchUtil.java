package main.java.util;

import java.text.Normalizer;
import java.util.Map;

public class SearchUtil {
    public static String normalize(String input) {
        String normalized = Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD);

        return normalized.replaceAll("\\p{M}", "");
    }

    public static String convert(Map<String, String> convert, String input) {
        String normalizedInput = normalize(input);

        for (Map.Entry<String, String> entry : convert.entrySet()) {
            String key = normalize(entry.getKey());

            if (key.equals(normalizedInput)) {
                return entry.getValue();
            }
        }

        return null;
    }
}
