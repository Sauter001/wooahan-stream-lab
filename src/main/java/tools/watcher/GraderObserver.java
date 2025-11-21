package tools.watcher;

import java.nio.file.Path;

public interface GraderObserver {
    /**
     * 파일이 변경되었을 때 호출됩니다.
     *
     * @param filePath 변경된 파일의 경로
     */
    void onFileChanged(Path filePath);
}
