package jp.ac.ritsumei.ise.phy.exp2.is0691ve.locationgame;

public class NmeaParser {
    public static double[] parseGGA(String nmeaSentence) {
        if (nmeaSentence.startsWith("$GPGGA")) {
            String[] tokens = nmeaSentence.split(",");
            if (tokens.length > 9) {
                double latitude = convertToDecimal(tokens[2], tokens[3]);
                double longitude = convertToDecimal(tokens[4], tokens[5]);
                return new double[]{latitude, longitude};
            }
        }
        return null;
    }

    private static double convertToDecimal(String value, String direction) {
        if (value == null || value.isEmpty()) return 0.0;
        double degrees = Double.parseDouble(value.substring(0, 2));
        double minutes = Double.parseDouble(value.substring(2)) / 60.0;
        double decimal = degrees + minutes;
        return (direction.equals("S") || direction.equals("W")) ? -decimal : decimal;
    }
}
