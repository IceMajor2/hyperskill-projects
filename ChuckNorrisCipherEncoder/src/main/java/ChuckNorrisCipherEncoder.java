import java.util.Scanner;

public class ChuckNorrisCipherEncoder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Input string:");
        String input = scanner.nextLine();
        System.out.println("");
        System.out.println("The result:");
        for(char ch : input.toCharArray()) {
            StringBuilder binary = new StringBuilder(Integer.toBinaryString(ch));
            while(binary.length() != 7) {
                binary.insert(0, '0');
            }
            System.out.println(ch + " = " + binary);
        }
    }
}
