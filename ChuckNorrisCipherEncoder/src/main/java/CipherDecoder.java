
public class CipherDecoder {

    private String message;
    
    public CipherDecoder(String msg) {
        this.message = msg;
    }
    
    public String decodeMessage() {
        boolean onSeries = false;
        int counter = -1;

        StringBuilder toReturn = new StringBuilder("");
        StringBuilder binary = new StringBuilder("");
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (!onSeries) {
                counter = (message.charAt(i + 1) == '0') ? 2 : 1;
                i = (counter == 1) ? i + 1 : i + 2;
                onSeries = true;
                continue;
            }
            if (ch == ' ') {
                onSeries = false;
                continue;
            }
            char toAppend = (counter == 1) ? '1' : '0';
            binary.append(toAppend);
            if (binary.length() == 7) {
                toReturn.append(binToDec(binary.toString()));
                binary = new StringBuilder("");
            }
        }
        return toReturn.toString();
    }

    private char binToDec(String binary) {
        int decimal = 0;
        for (int i = binary.length() - 1, power = 0; i >= 0; i--, power++) {
            if (binary.charAt(i) == '1') {
                decimal += Math.pow(2, power);
            }
        }
        return (char) decimal;
    }
}
