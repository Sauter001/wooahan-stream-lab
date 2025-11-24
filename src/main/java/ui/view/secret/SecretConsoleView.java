package ui.view.secret;

import constants.OutputConstants;
import util.Console;

import java.util.List;

public class SecretConsoleView implements SecretView {

    @Override
    public void showSecretIntro(boolean showDialogue) {
        printSecretHeader();

        if (showDialogue) {
            printDialogue();
        }

        showLearningObjectives();
    }

    private void printSecretHeader() {
        System.out.println();
        System.out.println("░".repeat(50));
        System.out.println("░  ▓▓▓ SECRET PHASE ▓▓▓                          ░");
        System.out.println("░  Beyond the Stream                             ░");
        System.out.println("░".repeat(50));
        System.out.println();
        System.out.println("축하합니다. 숨겨진 영역에 도달했습니다.");
        System.out.println("이곳의 문제들은 Stream API의 한계를 시험합니다.");
        System.out.println();
    }

    private void printDialogue() {
        String[] lines = {
            "\"여기까지 온 자에게...\"",
            "",
            "\"Stream의 진정한 힘은 함수형 사고에 있다.\"",
            "\"순열, 무한 수열, 그리고 대용량 병렬 처리...\"",
            "\"이 세 가지 시련을 통과하라.\""
        };

        for (String line : lines) {
            System.out.println(line);
            sleep(OutputConstants.SHORT_DIALOGUE_TIME);
        }
        System.out.println();
        sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
    }

    private void showLearningObjectives() {
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("              SECRET PHASE 목표                   ");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  • reduce()를 활용한 순열 생성");
        System.out.println("  • Stream.iterate()와 무한 스트림");
        System.out.println("  • parallelStream()과 성능 최적화");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
    }

    @Override
    public void showProblemList(List<ProblemSummary> problems) {
        System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
        System.out.println("░  SECRET CHALLENGES                             ░");
        System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");

        for (ProblemSummary problem : problems) {
            String status = problem.solved() ? "◆" : "◇";
            System.out.printf("  %s [%s] %s%n", status, problem.id(), problem.name());
            System.out.printf("      └─ %s%n", problem.description());
        }

        System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
        System.out.println();
    }

    @Override
    public void showSecretPrompt() {
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  [M/main] 메인 메뉴로 돌아가기");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        System.out.println("📂 src/main/java/solutions/secret/LevelSecret.java");
        System.out.println("파일을 수정하고 저장(Ctrl + S)하면 자동으로 채점됩니다.");
        System.out.println("Secret Phase 진행 중... (파일 감시 중)");
    }

    @Override
    public void showSecretCompleteOptions() {
        System.out.println();
        System.out.println("▓".repeat(50));
        System.out.println("▓                                                ▓");
        System.out.println("▓     ★ SECRET PHASE COMPLETE ★                ▓");
        System.out.println("▓                                                ▓");
        System.out.println("▓".repeat(50));
        System.out.println();
        System.out.println("  Stream Master의 칭호를 획득했습니다!");
        System.out.println();
        System.out.println("  [M] 메인 화면으로");
        System.out.println();
        System.out.print("> ");
    }

    @Override
    public String readCommand() {
        return Console.readLine().trim().toLowerCase();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
