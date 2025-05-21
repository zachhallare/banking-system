import java.util.Scanner;

public class MenuUtil {
    public static int displayMenu(Scanner scanner, String title, String[] userOptions) {
        System.out.println("\n==== " + title + " ====");
        for (int i = 0; i < userOptions.length; i++) {
            if ((userOptions.length - 1) == i) {
                System.out.printf("[%d] %s%n", 0, userOptions[i]);
            }
            else {
                System.out.printf("[%d] %s%n", i + 1, userOptions[i]);
            }
        }

        String prompt = "Enter option number: ";
        return InputUtil.readIntInRange(scanner, prompt, 0, userOptions.length - 1);
    }
}
