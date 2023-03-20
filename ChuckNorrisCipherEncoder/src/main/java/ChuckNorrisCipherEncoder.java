
import java.util.Scanner;

public class ChuckNorrisCipherEncoder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please input operation "
                    + "(encode/decode/exit):");
            String input = scanner.nextLine();
            if(input.equals("exit")) {
                break;
            }
            if(input.equals("encode")) {
                System.out.println("Input string:");
                input = encodeMessage(scanner.nextLine());
                System.out.println("Encoded string:");
                System.out.println(input);
                System.out.println("");
                continue;
            }
            if(input.equals("decode")) {
                System.out.println("Input encoded string:");
                input = decodeMessage(scanner.nextLine());
                System.out.println("Decoded string:");
                System.out.println(input); 
                System.out.println();
            }
        }

    }
    
    public static String getBinaryString(String string) {
        StringBuilder binaryString = new StringBuilder("");
        for(int i = 0; i < string.length(); i++) {
            char currChar = string.charAt(i);
            String binaryChar = getBinaryChar(currChar);
            binaryString.append(binaryChar);
        }
        return binaryString.toString();
    }
    
    public static String getBinaryChar(char ch) {
        StringBuilder binaryChar = new StringBuilder(Integer.toBinaryString(ch));
        while(binaryChar.length() != 7) {
                binaryChar.insert(0, '0');
        }
        return binaryChar.toString();
    }

    public static String encodeMessage(String message) {
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

    public static String decodeMessage(String cipher) {
        boolean onSeries = false;
        int counter = -1;

        StringBuilder toReturn = new StringBuilder("");
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
            if (binary.length() == 7) {
                toReturn.append(binToDec(binary.toString()));
                binary = new StringBuilder("");
            }
        }
        return toReturn.toString();
    }

    public static char binToDec(String binary) {
        int decimal = 0;
        for (int i = binary.length() - 1, power = 0; i >= 0; i--, power++) {
            if (binary.charAt(i) == '1') {
                decimal += Math.pow(2, power);
            }
        }
        return (char) decimal;
    }
}
