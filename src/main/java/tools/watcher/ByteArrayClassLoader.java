package tools.watcher;

/**
 * 바이트 배열에서 클래스를 로드하는 ClassLoader.
 * 파일 변경 후 캐싱 없이 새로운 클래스를 로드하기 위해 사용.
 */
public class ByteArrayClassLoader extends ClassLoader {
    private final String targetClassName;
    private final byte[] classBytes;

    public ByteArrayClassLoader(ClassLoader parent, String targetClassName, byte[] classBytes) {
        super(parent);
        this.targetClassName = targetClassName;
        this.classBytes = classBytes;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.equals(targetClassName)) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.loadClass(name);
    }
}
