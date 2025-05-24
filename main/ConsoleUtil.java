public class ConsoleUtil {
    public static void clearScreen(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } 
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch (Exception error) {
            System.out.println("Error clearing screen: " + error);
        }
    }
}
