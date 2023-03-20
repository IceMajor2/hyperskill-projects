
import java.util.Scanner;

public class ChuckNorrisCipherEncoder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please input operation "
                    + "(encode/decode/exit):");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (input.equals("encode")) {
                System.out.println("Input string:");
                CipherEncoder encoder = new CipherEncoder(scanner.nextLine());
                System.out.println("Encoded string:");
                System.out.println(encoder.encodeMessage());
                System.out.println("");
                continue;
            }
            if (input.equals("decode")) {
                System.out.println("Input encoded string:");
                CipherDecoder decoder = new CipherDecoder(scanner.nextLine());
                System.out.println("Decoded string:");
                System.out.println(decoder.decodeMessage());
                System.out.println();
                continue;
            }
            System.out.println("There is no '" + input + "' operation");
        }
    }
}
