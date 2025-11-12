package ui.view.tutorial;

import constants.DirectoryConstants;
import constants.OutputConstants;

public class TutorialConsoleView implements TutorialView {

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
            displayBasicStreamExamples();
            displayTutorialProblem();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
}
