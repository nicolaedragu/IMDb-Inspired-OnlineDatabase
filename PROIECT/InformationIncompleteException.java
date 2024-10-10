public class InformationIncompleteException extends Exception {
    public InformationIncompleteException(String message) {
        super(message);
    }
    public InformationIncompleteException() {
        super("Information is incomplete!");
    }
}
