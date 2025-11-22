package ui.view.common;

public class CommonConsoleView implements CommonView {
    private static final String TITLE_HEADER = """
            ╔══════════════════════════════════════════════════════╗
            ║                                                      ║
            ║             🧪 Welcome to Stream Lab 🧪             ║
            ║                                                      ║
            ║   Mastering Java Streams, One Challenge at a Time.   ║
            ║   v1.0.0 0x646F4E6F                                  ║
            ╚══════════════════════════════════════════════════════╝
            """;


    @Override
    public void showIntro() {
        System.out.println(TITLE_HEADER);
    }


}
