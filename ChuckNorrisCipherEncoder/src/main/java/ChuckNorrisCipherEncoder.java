
import java.util.Scanner;

public class ChuckNorrisCipherEncoder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input string:");
        String input = scanner.nextLine();
        System.out.println("");
        System.out.println("The result:");
        System.out.println(ChuckNorrisCipherToBinary(input));
    }

    public static String binaryToChuckNorrisCipher(String binaryChar) {
        StringBuilder encoded = new StringBuilder("");
        char currPointer = '\0';
        char prevPointer = '\0';
        for (int i = 0; i < binaryChar.length(); i++) {
            currPointer = binaryChar.charAt(i);
            prevPointer = (i != 0) ? binaryChar.charAt(i - 1) : '\0';

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

    public static String ChuckNorrisCipherToBinary(String cipher) {
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
