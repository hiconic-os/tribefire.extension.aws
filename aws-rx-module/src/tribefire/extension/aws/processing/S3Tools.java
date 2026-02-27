package tribefire.extension.aws.processing;

import java.text.Normalizer;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class S3Tools {

    // Fixed formatter: stable, thread-safe, allocation-free after init
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH");

    private S3Tools() {
        // utility class
    }

    /**
     * @return path like "2026/02/19/14"
     */
    public static String newDateStructuredIndividualShardingPath() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(FORMAT);
    }
    
    /**
     * Sanitizes a string so it is safe and boring as an S3 key component.
     *
     * Rules:
     * - normalize unicode (NFKD)
     * - remove control characters
     * - keep: a-z A-Z 0-9 / - _ .
     * - replace everything else with '_'
     * - collapse multiple '_' into one
     */
    public static String sanitizeKey(String input) {
        if (input == null || input.isEmpty()) {
            return "_";
        }

        // Normalize unicode (e.g. ä → a¨ → a)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFKD);

        StringBuilder out = new StringBuilder(normalized.length());
        boolean lastWasUnderscore = false;

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);

            // drop control chars
            if (c < 0x20 || c == 0x7F) {
                continue;
            }

            boolean allowed =
                    (c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9') ||
                    c == '/' || c == '-' || c == '_' || c == '.';

            if (allowed) {
                out.append(c);
                lastWasUnderscore = false;
            } else {
                if (!lastWasUnderscore) {
                    out.append('_');
                    lastWasUnderscore = true;
                }
            }
        }

        return out.toString();
    }
}