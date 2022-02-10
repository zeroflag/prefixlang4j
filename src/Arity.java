import java.util.List;
public interface Arity {
    void check(String function, List<?> params);
    Arity UNARY = Arity.of(1);
    Arity BINARY = Arity.of(2);

    static Arity of(int count) {
        return (methodName, params) -> {
            if (params.size() != count) {
                throw new ArityException(methodName, count, params.size());
            }
        };
    }
    static Arity min(int count) {
        return (methodName, params) -> {
            if (params.size() < count) {
                throw new ArityException("wrong number of arguments in call to '" + methodName
                        + "'. Expected at least " + count + " got " + params.size() + ".");
            }
        };
    }
}
