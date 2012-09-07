import java.net.MalformedURLException;
import java.net.URL;

public class Hotswap {
    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        Example oldExample = new Example();
        oldExample.plus();
        System.out.println(oldExample.getCount());

        Hotswap hotswap = new Hotswap();
        while (true) {
            IExample newExample = hotswap.swap(oldExample, new TestClassLoader(new URL[]{hotswap.getClassPath()}));
            String message = newExample.message();
            int count = newExample.plus();
            System.out.println(message.concat(" : " + count));
            Thread.sleep(5000);
        }
    }

    private IExample swap(IExample old, TestClassLoader classLoader) {
        try {
            Class<?> clazz = classLoader.loadClass("Example");
            System.out.println(IExample.class.getClassLoader());
            IExample exampleInstance = ((IExample) clazz.newInstance()).copy(old);
            System.out.println(exampleInstance.getClass().getClassLoader());
            return exampleInstance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return old;
    }

    public URL getClassPath() throws MalformedURLException {
        String classPath = Hotswap.class.getClassLoader().getResource(Hotswap.class.getName() + ".class").toExternalForm();
        return new URL(classPath.substring(0, classPath.lastIndexOf("/") + 1));
    }
}
