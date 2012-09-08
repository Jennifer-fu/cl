import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Hotswap {
    private long lastModified;

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        IExample oldExample = new Example();
        oldExample.plus();
        System.out.println(oldExample.getCount());

        Hotswap hotswap = new Hotswap();
        while (true) {
            IExample newExample = hotswap.swap(oldExample);
            String message = newExample.message();
            int count = newExample.plus();
            System.out.println(message.concat(" : " + count));
            oldExample = newExample;
            Thread.sleep(5000);
        }
    }

    private IExample swap(IExample old) {
        try {
            String sourceFile = srcPath().concat("Example.java");
            if (isChanged(sourceFile)) {
                comiple(sourceFile, classPath());
                MyClassLoader classLoader = new MyClassLoader(new URL[]{new URL("file:"+classPath())});
                Class<?> clazz = classLoader.loadClass("Example");
                System.out.println(IExample.class.getClassLoader());
                IExample exampleInstance = ((IExample) clazz.newInstance()).copy(old);
                System.out.println(exampleInstance.getClass().getClassLoader());
                return exampleInstance;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return old;
    }

    private void comiple(String sourceFile, String destination) throws IOException {
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);
        File file = new File(sourceFile);
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file);
        jc.getTask(null,fileManager,null, Arrays.asList(new String[]{"-d", destination}),null,javaFileObjects).call();
        fileManager.close();
    }

    private boolean isChanged(String filePath) throws MalformedURLException {
        File file = new File(filePath);
        long newLastModified = file.lastModified();
        if (newLastModified > lastModified) {
            lastModified = newLastModified;
            return true;
        }
        return false;
    }

    private String srcPath() {
        return System.getProperty("user.dir").concat("/src/");
    }

    public String classPath() throws MalformedURLException {
        String classPath = Hotswap.class.getClassLoader().getResource(Hotswap.class.getName() + ".class").toExternalForm();
        return classPath.substring(classPath.indexOf("/"), classPath.lastIndexOf("/") + 1);
    }


}
