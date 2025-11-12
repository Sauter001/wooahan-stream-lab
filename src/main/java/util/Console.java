package util;

import java.nio.file.Path;
import java.util.Scanner;

public final class Console {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String YES_OR_NO = "[Y] 네, [N] 아니요";


    public static String readLine() {
        return scanner.nextLine();
    }

    public static void close() {
        scanner.close();
    }

    public static boolean confirm(String prompt) {
        System.out.println(prompt);
        System.out.println(YES_OR_NO);
        String result = Console.readLine().strip();
        final String acceptInput = "Y";

        return result.toUpperCase().startsWith(acceptInput);
    }
}
