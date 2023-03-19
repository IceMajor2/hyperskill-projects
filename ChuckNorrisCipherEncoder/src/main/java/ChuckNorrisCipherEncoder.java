
import java.util.Scanner;

public class ChuckNorrisCipherEncoder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input string:");
        String input = scanner.nextLine();
        System.out.println("");
        System.out.println("The result:");
        
        StringBuilder binaryString = new StringBuilder("");
        for (char ch : input.toCharArray()) {
            StringBuilder binaryChar = new StringBuilder(Integer.toBinaryString(ch));
            while (binaryChar.length() != 7) {
                binaryChar.insert(0, '0');
            }
            binaryString.append(binaryChar);
        }
        System.out.println(binaryToChuckNorrisCipher(binaryString.toString()));
    }

    public static String binaryToChuckNorrisCipher(String binaryChar) {
        StringBuilder encoded = new StringBuilder("");
        char currPointer = '\0';
        char prevPointer = '\0';
        for (int i = 0; i < binaryChar.length(); i++) {
            currPointer = binaryChar.charAt(i);
            prevPointer = (i != 0) ? binaryChar.charAt(i - 1) : '\0';
            
            if(currPointer != prevPointer) {
                encoded.append(" ");
                switch(currPointer) {
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
}
