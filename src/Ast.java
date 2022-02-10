import java.util.ArrayList;
import java.util.List;

public class Ast {
    private final List<Ast> children = new ArrayList<>();
    private final String token;

    public Ast(String token) {
        this.token = token;
    }

    public void addChild(Ast child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return isAtom() ? token : children.toString();
    }

    public String token() {
        return token;
    }

    public boolean isStr() {
        return token != null && token.startsWith("'") && token.endsWith("'");
    }

    public boolean isNumber() {
        if (token == null) return false;
        try {
            numValue();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Number numValue() {
        try {
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            return Double.parseDouble(token);
        }
    }

    public String strValue() {
        return token.substring(1, token.length() -1);
    }

    public boolean isAtom() {
        return !"(".equals(token);
    }

    public boolean isFunction() {
        return !children.isEmpty();
    }

    public String functionName() {
        return children.get(0).token();
    }

    public List<Ast> functionParameters() {
        return children.subList(1, children.size());
    }
}
