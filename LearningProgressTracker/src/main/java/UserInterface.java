
import java.util.Scanner;

public class UserInterface {
    
    private Scanner scanner;
    
    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        while(true) {
            String usrCommand = scanner.nextLine();
            if("exit".equals(usrCommand)) {
                System.out.println("Bye!");
                break;
            }
            if(usrCommand.isBlank()) {
                System.out.println("No input.");
                continue;
            }
            System.out.println("Error: unknown command!");
        }
    }
}
