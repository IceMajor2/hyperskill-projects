
import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("Please input operation "
                    + "(encode/decode/exit):");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("Bye!");
                break;
            }
            if (input.equals("encode")) {
                System.out.println("Input string:");
                CipherEncoder encoder = null;
                try {
                    encoder = new CipherEncoder(scanner.nextLine());
                    System.out.println("Encoded string:");
                    System.out.println(encoder.encodeMessage());
                    System.out.println("");
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("No string was provided!");
                    System.out.println();
                }

                continue;
            }
            if (input.equals("decode")) {
                System.out.println("Input encoded string:");
                CipherDecoder decoder = null;
                try {
                    decoder = new CipherDecoder(scanner.nextLine());
                } catch (IllegalArgumentException e) {
                    System.out.println("Encoded string is not valid.");
                    System.out.println();
                    continue;
                }
                System.out.println("Decoded string:");
                System.out.println(decoder.decodeMessage());
                System.out.println();
                continue;
            }
            System.out.println("There is no '" + input + "' operation");
            System.out.println("");
        }
    }
}
