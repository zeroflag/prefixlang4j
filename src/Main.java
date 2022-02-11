import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // with explicit equality check

        eval("(or " +
                "(and " +
                "   (member 'admin') " +
                "   (member 'datalake')) " +
                "(or " +
                "   (= username 'lmccay') " +
                "   (= username 'pzampino')))");


        // with a shortcut

        eval("(or " +
                "(and " +
                "   (member 'admin') " +
                "   (member 'datalake')) " +
                "(or " +
                "   (username 'lmccay') " +
                "   (username 'pzampino')))");

        eval("(match groups 'data.*')");
        eval("(match username 'admi.')");
        eval("(!= (size groups) 0)");

//        eval("(= (session 'name') 'value')");
//        eval("(= (request-header 'name') 'value')");
//        eval("(= request-method 'value')");

    }

    private static void eval(String script) {
        Parser parser = new Parser();
        Ast ast = parser.parse(script);
        System.out.println("AST: " + ast);
        Interpreter interpreter = new Interpreter();
        interpreter.addConstant("username", "admin");
        interpreter.addConstant("groups", Arrays.asList("admin", "datalake"));
        System.out.println("result: " + interpreter.eval(ast));
    }
}
