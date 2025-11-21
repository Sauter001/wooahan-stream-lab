package tools.watcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileWatcher implements Runnable {
    private final Path directoryToWatch;
    private final List<GraderObserver> observers;
    private volatile boolean running = true;

    public FileWatcher(Path directoryToWatch) {
        this.directoryToWatch = directoryToWatch;
        this.observers = new ArrayList<>();
    }

    public void addObserver(GraderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GraderObserver observer) {
        observers.remove(observer);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            // 디렉토리와 하위 디렉토리 등록
            registerRecursive(directoryToWatch, watchService);

            System.out.println("👀 파일 감시 중: " + directoryToWatch);
            System.out.println("Tutorial.java를 수정하면 자동으로 채점이 시작됩니다.\n");

            while (running) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path fileName = pathEvent.context();

                    // .java 파일만 감시
                    if (fileName.toString().endsWith(".java")) {
                        Path watchedPath = (Path) key.watchable();
                        Path fullPath = watchedPath.resolve(fileName);

                        // Observer들에게 알림
                        notifyObservers(fullPath);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("파일 감시 시작 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerRecursive(Path start, WatchService watchService) throws IOException {
        Files.walk(start)
                .filter(Files::isDirectory)
                .forEach(path -> {
                    try {
                        // ENTRY_MODIFY만 감지하여 중복 이벤트 방지
                        path.register(
                                watchService,
                                StandardWatchEventKinds.ENTRY_MODIFY
                        );
                    } catch (IOException e) {
                        System.err.println("디렉토리 등록 중 오류 발생: " + path);
                    }
                });
    }

    private void notifyObservers(Path filePath) {
        for (GraderObserver observer : observers) {
            observer.onFileChanged(filePath);
        }
    }
}
