import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

public class Hotswap {
    public static void main(String[] args) {
        try {
            TestClassLoader classLoader = new TestClassLoader(new URL[]{getClassPath()});
            Class<?> clazz = classLoader.loadClass("Example");
            System.out.println(IExample.class.getClassLoader());
            IExample exampleInstance = (IExample)clazz.newInstance();
            System.out.println(exampleInstance.getClass().getClassLoader());
            String message = exampleInstance.message();
            System.out.println(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static URL getClassPath() throws MalformedURLException {
        String classPath = Hotswap.class.getClassLoader().getResource(Hotswap.class.getName()+".class").toExternalForm();
        return new URL(classPath.substring(0,classPath.lastIndexOf("/")+1));
    }
}
