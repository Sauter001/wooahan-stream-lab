package ui.view.tutorial;

import constants.DirectoryConstants;
import constants.OutputConstants;
import context.GameContext;
import util.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TutorialConsoleView implements TutorialView {

    public static final String TUTORIAL_SECRET_PIECE = "0x74546F75";

    private static void displayBasicStreamExamples() throws InterruptedException {
        System.out.println("""
                예를 들어, 이런 코드 대신:
                ❌ int sum = 0;
                   for (int n : numbers) {
                       if (n % 2 == 0) sum += n;
                   }
                """);
        Thread.sleep(OutputConstants.SHORT_DIALOGUE_TIME);
        System.out.println("""
                이렇게 쓸 수 있죠:
                ✅ numbers.stream()
                       .filter(n -> n % 2 == 0)
                       .sum();
                """);
        Thread.sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
        System.out.println("코드가 무엇을 하는지 더 명확하지 않나요?");
        Thread.sleep(OutputConstants.LONG_DIALOGUE_TIME);
    }

    private static void displayHeader() throws InterruptedException {
        System.out.println("""
                ========================================
                          튜토리얼
                ========================================
                
                반가워요! Stream Lab에 오신 걸 환영합니다.
                
                여기서는 for문이나 while문을 쓰지 않고도
                멋진 코드를 작성할 수 있어요.
                """);
        Thread.sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
    }

    @Override
    public void showTutorialIntro() {
        try {
            displayHeader();
            displayRules();
            displayBasicStreamExamples();
            displayTutorialProblem();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void displayRules() throws InterruptedException {
        System.out.println("""
                📋 규칙은 간단해요:
                  • for문, while문 사용 금지!
                  • 오직 Stream API만 사용하세요.

                파일을 저장하면 자동으로 채점됩니다.
                """);
        Thread.sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
    }

    private void displayTutorialProblem() throws InterruptedException {
        System.out.println("이제 간단한 예제를 풀어봅시다!");
        Thread.sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
        System.out.println(OutputConstants.DIVISOR);
        System.out.println("""
                문제: 주어진 리스트에서 짝수만 걸러낸 리스트를 구하세요.

                입력: [1, 2, 3, 4, 5, 6]
                출력: [2, 4, 6]
                """);
        System.out.printf("%s 파일을 수정하고 저장하세요.\n", DirectoryConstants.TUTORIAL_PATH);
    }

    @Override
    public void waitForExitOrCompletion(GameContext context) {
        System.out.println("\n" + OutputConstants.DIVISOR);
        System.out.println("파일을 수정하고 저장하세요. (파일 감시 중...)");
        System.out.println("종료하려면 'exit'를 입력하세요.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            // 튜토리얼 완료 체크
            if (context.isTutorialCompleted()) {
                displayCompletionAndTransition();
                break;
            }

            // 논블로킹 입력 체크
            try {
                if (reader.ready()) {
                    String input = reader.readLine();
                    if (input != null && isExitCommand(input.trim().toLowerCase())) {
                        System.out.println("튜토리얼을 종료합니다.");
                        break;
                    }
                }
            } catch (IOException e) {
                // 입력 읽기 실패 시 무시
            }

            // CPU 과부하 방지
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void displayCompletionAndTransition() {
        try {
            System.out.println("\n" + OutputConstants.DIVISOR + TUTORIAL_SECRET_PIECE);
            System.out.println("축하합니다! 튜토리얼을 완료했습니다!");
            Thread.sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
            System.out.println("메인 메뉴로 이동합니다...");
            Thread.sleep(OutputConstants.SHORT_DIALOGUE_TIME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean isExitCommand(String input) {
        return input.equals("exit") || input.startsWith("e");
    }
}
