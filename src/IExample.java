public interface IExample {
    String message();
    int plus();

    IExample copy(IExample old);

    int getCount();
}
