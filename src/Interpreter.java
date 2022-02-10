import java.util.*;
import java.util.stream.Collectors;

public class Interpreter {
    private final Map<String, SpecialForm> specialForms = new HashMap<>();
    private final Map<String, Func> functions = new HashMap<>();
    private final Map<String, Object> constants = new HashMap<>();

    private interface Func {
        Object call(List<Object> parameters);
    }

    private interface SpecialForm {
        Object call(List<Ast> parameters);
    }

    public Interpreter() {
        specialForms.put("or", args -> {
            Arity.min(1).check("or", args);
            return args.stream().anyMatch(each -> (boolean)eval(each));
        });
        specialForms.put("and", args -> {
            Arity.min(1).check("and", args);
            return args.stream().allMatch(each -> (boolean)eval(each));
        });
        functions.put("not", args -> {
            Arity.UNARY.check("not", args);
            return !(boolean)args.get(0);
        });
        functions.put("=", args -> {
            Arity.BINARY.check("=", args);
            return args.get(0).equals(args.get(1));
        });
        functions.put("!=", args -> {
            Arity.BINARY.check("!=", args);
            return !args.get(0).equals(args.get(1));
        });
        functions.put("size", args -> {
            Arity.UNARY.check("size", args);
            return ((Collection<?>) args.get(0)).size();
        });
        functions.put("username", args -> {
            Arity.UNARY.check("username", args);
            return constants.get("username").equals(args.get(0));
        });
        functions.put("member", args -> {
            Arity.UNARY.check("member", args);
            return ((List<String>)constants.get("groups")).contains((String)args.get(0));
        });
        constants.put("true", true);
        constants.put("false", false);
    }

    public void addConstant(String name, Object value) {
        constants.put(name, value);
    }

    public Object eval(Ast ast) {
        try {
            if (ast == null) {
                return null;
            } else if (ast.isAtom()) {
                return ast.isStr() ? ast.strValue() : ast.isNumber() ? ast.numValue() : lookupConstant(ast);
            } else if (ast.isFunction()) {
                SpecialForm specialForm = specialForms.get(ast.functionName());
                if (specialForm != null) {
                    return specialForm.call(ast.functionParameters());
                } else {
                    Func func = functions.get(ast.functionName());
                    if (func == null)
                        throw new UndefinedSymbolException(ast.functionName(), "function");
                    return func.call(ast.functionParameters().stream().map(this::eval).collect(Collectors.toList()));
                }
            } else {
                throw new InterpreterException("Unknown token: " + ast.token());
            }
        } catch (ClassCastException e) {
            throw new TypeException("Type error at: " + ast, e);
        }
    }

    private Object lookupConstant(Ast ast) {
        Object var = constants.get(ast.token());
        if (var == null)
            throw new UndefinedSymbolException(ast.token(), "variable");
        return var;
    }
}
