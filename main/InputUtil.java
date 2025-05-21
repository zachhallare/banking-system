import java.util.Scanner;

public class InputUtil {
    public static int readIntInRange(Scanner scanner, String prompt, int minNum, int maxNum) {
        int userInput;
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                if (userInput >= minNum && userInput <= maxNum) {
                    return userInput;
                }
                else {
                    System.out.println("Input must be between " + minNum + " and " + maxNum + ".");
                }
            }
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        double userInput;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                userInput = scanner.nextDouble();
                return userInput;
            }
            else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
    }
}
