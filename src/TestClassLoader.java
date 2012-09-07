import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestClassLoader extends URLClassLoader {

    public TestClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if ("Example".equals(name)) {
            return findClass(name);
        }
        return super.loadClass(name);
    }
}