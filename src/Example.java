public class Example implements IExample{
    private int count;

    public String message(){
        return "version 1";
    }

    public int plus(){
        return count++;
    }
}
