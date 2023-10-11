
public class CipherDecoder {

    private String cipher;
    private String cipherInBinary;

    public CipherDecoder(String cipher) throws IllegalArgumentException {
        this.cipher = cipher;
        this.cipherInBinary = this.cipherToBinary();
        
        if (!this.isValid()) {
            throw new IllegalArgumentException();
        }
    }

    public String cipherToBinary() {
        boolean onSeries = false;
        int counter = -1;

        StringBuilder binary = new StringBuilder("");
        for (int i = 0; i < cipher.length(); i++) {
            char ch = cipher.charAt(i);
            if (!onSeries) {
                counter = (cipher.charAt(i + 1) == '0') ? 2 : 1;
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
        }
        return binary.toString();
    }

    public String decodeMessage() {
        StringBuilder binaryChar = new StringBuilder("");
        StringBuilder toReturn = new StringBuilder("");
        for (int i = 0; i < cipherInBinary.length(); i++) {
            binaryChar.append(cipherInBinary.charAt(i));
            if (binaryChar.length() == 7) {
                toReturn.append(binToDec(binaryChar.toString()));
                binaryChar = new StringBuilder("");
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

    private boolean isValid() {
        for (char ch : cipher.toCharArray()) {
            if (ch != '0' && ch != ' ') {
                return false;
            }
        }
        String[] blocks = cipher.split(" ");
        if (!blocks[0].equals("00") && !blocks[0].equals("0")) {
            return false;
        }
        if (blocks.length % 2 != 0) {
            return false;
        }
        if (cipherInBinary.length() % 7 != 0) {
            return false;
        }
        return true;
    }
}
