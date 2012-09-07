import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

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
            String filePath = getClassDirectory().getPath().concat("Example").concat(".class");
            if (isChanged(filePath)) {
                TestClassLoader classLoader = new TestClassLoader(new URL[]{getClassDirectory()});
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

    private boolean isChanged(String filePath) throws MalformedURLException {
        File file = new File(filePath);
        long newLastModified = file.lastModified();
        if (newLastModified > lastModified) {
            lastModified = newLastModified;
            return true;
        }
        return false;
    }

    public URL getClassDirectory() throws MalformedURLException {
        String classPath = Hotswap.class.getClassLoader().getResource(Hotswap.class.getName() + ".class").toExternalForm();
        String classFolderPath = classPath.substring(0, classPath.lastIndexOf("/") + 1);
        return new URL(classFolderPath);
    }


}
