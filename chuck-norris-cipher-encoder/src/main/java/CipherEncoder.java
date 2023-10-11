
public class CipherEncoder {

    private String message;

    public CipherEncoder(String msg) {
        this.message = msg;
    }

    public String encodeMessage() {
        String binaryMsg = getBinaryString(message);
        StringBuilder encoded = new StringBuilder("");
        char currPointer = '\0';
        char prevPointer = '\0';
        for (int i = 0; i < binaryMsg.length(); i++) {
            currPointer = binaryMsg.charAt(i);
            prevPointer = (i != 0) ? binaryMsg.charAt(i - 1) : '\0';

            if (currPointer != prevPointer) {
                encoded.append(" ");
                switch (currPointer) {
                    case '1':
                        encoded.append("0 0");
                        break;
                    case '0':
                        encoded.append("00 0");
                        break;
                }
                continue;
            }
            encoded.append("0");
        }
        return encoded.deleteCharAt(0).toString();
    }

    private String getBinaryString(String string) {
        StringBuilder binaryString = new StringBuilder("");
        for (int i = 0; i < string.length(); i++) {
            char currChar = string.charAt(i);
            String binaryChar = getBinaryChar(currChar);
            binaryString.append(binaryChar);
        }
        return binaryString.toString();
    }

    private String getBinaryChar(char ch) {
        StringBuilder binaryChar = new StringBuilder(Integer.toBinaryString(ch));
        while (binaryChar.length() != 7) {
            binaryChar.insert(0, '0');
        }
        return binaryChar.toString();
    }
}
