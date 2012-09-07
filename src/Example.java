public class Example implements IExample{
    private int count;

    public String message(){
        return "version2";
    }

    public int plus(){
        return count++;
    }

    @Override
    public IExample copy(IExample old) {
        if(old!=null)
            count = old.getCount();
        return this;
    }

    @Override
    public int getCount() {
        return count;
    }


}
