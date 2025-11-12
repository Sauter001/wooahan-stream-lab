package constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class DirectoryConstants {
    public static final Path SOLUTION_PATH = Paths.get("solutions");
    public static final Path TUTORIAL_PATH = Paths.get(SOLUTION_PATH.toString(), "Tutorial.java");
}
