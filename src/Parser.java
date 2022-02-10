import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class Parser {

    public Ast parse(String str) {
        if (str == null || str.trim().equals(""))
            return null;
        try (PushbackReader reader = new PushbackReader(new StringReader(str))) {
            Ast ast = parse(reader);
            String rest = peek(reader);
            if (rest != null)
                throw new SyntaxException("Unexpected closing " + rest);
            return ast;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Ast parse(PushbackReader reader) throws IOException {
        String token = nextToken(reader);
        if ("(".equals(token)) {
            Ast children = new Ast(token);
            while (!")".equals(peek(reader)))
                children.addChild(parse(reader));
            nextToken(reader); // skip )
            return children;
        } else if (")".equals(token)) {
            throw new SyntaxException("Unexpected closing )");
        } else if ("".equals(token)) {
            throw new SyntaxException("Missing closing )");
        } else {
            return new Ast(token);
        }
    }

    private String nextToken(PushbackReader reader) throws IOException {
        String chr = peek(reader);
        if ("'".equals(chr))
            return parseString(reader);
        if ("(".equals(chr) || ")".equals(chr))
            return String.valueOf((char)reader.read());
        return parseAtom(reader);
    }

    private String parseAtom(PushbackReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        int chr = reader.read();
        while (chr != -1 && !Character.isWhitespace(chr) && ')' != chr) {
            buffer.append((char)chr);
            chr = reader.read();
        }
        if (chr == ')') reader.unread(')');
        return buffer.toString();
    }

    private String parseString(PushbackReader reader) throws IOException {
        StringBuilder str = new StringBuilder();
        str.append((char)reader.read());
        int chr = reader.read();
        while (chr != -1 && '\'' != chr) {
            str.append((char)chr);
            chr = reader.read();
        }
        if (chr == -1) throw new SyntaxException("Unterminated string");
        return str.append("'").toString();
    }

    private String peek(PushbackReader reader) throws IOException {
        int chr = reader.read();
        while (chr != -1 && Character.isWhitespace(chr)) chr = reader.read();
        if (chr == -1) return null;
        reader.unread(chr);
        return String.valueOf((char) chr);
    }
}
