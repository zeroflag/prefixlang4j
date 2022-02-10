public class UndefinedSymbolException extends InterpreterException {
    public UndefinedSymbolException(String name, String type) {
        super(String.format("Undefined %s: %s", type, name));
    }
}
