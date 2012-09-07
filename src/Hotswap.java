import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

public class Hotswap {
    public static void main(String[] args) throws InterruptedException {
        Example oldExample = new Example();
        oldExample.plus();
        System.out.println(oldExample.getCount());

        IExample newExample = new Hotswap().swap(oldExample);
        String message = newExample.message();
        int count = newExample.plus();
        System.out.println(message.concat(" : " + count));
    }

    private IExample swap(IExample old) {
        try {
            TestClassLoader classLoader = new TestClassLoader(new URL[]{getClassPath()});
            Class<?> clazz = classLoader.loadClass("Example");
            System.out.println(IExample.class.getClassLoader());
            IExample exampleInstance = ((IExample) clazz.newInstance()).copy(old);
            System.out.println(exampleInstance.getClass().getClassLoader());
            return exampleInstance;
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
