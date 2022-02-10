public class ArityException extends InterpreterException {
    public ArityException(String funcName, int formalCount, int actualCount) {
        super("Wrong number of arguments in call to '" + funcName
                + "'. Expected " + formalCount + " got " + actualCount + ".");
    }

    public ArityException(String message) {
        super(message);
    }
}
