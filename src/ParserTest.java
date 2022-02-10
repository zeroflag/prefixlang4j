import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testEmpty() {
        assertEquals(null, parse(""));
        assertEquals(null, parse(" "));
        assertEquals(null, parse(null));
    }

    @Test
    public void testUnexpectedClosingParen() {
        parse(")", "unexpected closing )");
        parse("())", "unexpected closing )");
        parse("() )", "unexpected closing )");
        parse("() ) ", "unexpected closing )");
        parse("(a (b) ))", "unexpected closing )");
        parse("( a (  b ( c (d   (e   (f ( g  ) )  )) ) ) ))", "unexpected closing )");
    }

    @Test
    public void testMissingClosingParen() {
        parse("(", "missing closing )");
        parse(" (", "missing closing )");
        parse("( ", "missing closing )");
        parse("  (a", "missing closing )");
        parse("  (a ()", "missing closing )");
        parse("  (a (b ( ) ) ", "missing closing )");
        parse("( a (  b ( c (d   (e   (f ( g  ) )  )) ) ", "missing closing )");
    }

    @Test
    public void testValidSingle() {
        assertEquals("[]", parse("()").toString());
        assertEquals("[]", parse("( )").toString());
        assertEquals("[]", parse(" ( ) ").toString());
        assertEquals("[a]", parse("(a)").toString());
        assertEquals("[a]", parse("( a)").toString());
        assertEquals("[a]", parse("(a )").toString());
        assertEquals("[a]", parse("( a )").toString());
        assertEquals("[a]", parse(" ( a ) ").toString());
    }

    @Test
    public void testValidNested1() {
        assertEquals("[a, [b]]", parse("(a (b))").toString());
        assertEquals("[a, [b]]", parse(" (a (b))").toString());
        assertEquals("[a, [b]]", parse(" ( a ( b))").toString());
        assertEquals("[a, [b]]", parse(" ( a ( b )) ").toString());
        assertEquals("[a, [b]]", parse(" ( a ( b ) ) ").toString());
        assertEquals("[a, [b]]", parse("(a (b) )").toString());
    }

    @Test
    public void testValidNested2() {
        assertEquals("[a, [b, c, [d]], e]", parse("(a (b c (d)) e)").toString());
        assertEquals("[a, [b, c, [d]], e]", parse("(a ( b c ( d)) e)").toString());
        assertEquals("[a, [b, c, [d]], e]", parse("(a (b c (d ) ) e)").toString());
        assertEquals("[a, [b, c, [d]], e]", parse(" (a ( b c (d )) e )").toString());
        assertEquals("[a, [b, c, [d]], e]", parse(" (a   ( b c (  d )  ) e ) ").toString());
    }

    @Test
    public void testValidNested3() {
        assertEquals("[ab, [cd], ef]", parse("(ab (cd) ef)").toString());
        assertEquals("[ab, [cd], ef]", parse("(ab ( cd) ef)").toString());
        assertEquals("[ab, [cd], ef]", parse("(ab ( cd ) ef)").toString());
        assertEquals("[ab, [cd], ef]", parse(" ( ab ( cd ) ef ) ").toString());
    }

    @Test
    public void testValidNested4() {
        assertEquals("[a, [b, [c, [d, [e, [f, [g]]]]]]]", parse("(a (b (c (d (e (f (g)))))))").toString());
        assertEquals("[a, [b, [c, [d, [e, [f, [g]]]]]]]", parse("( a (  b (c (d   (e (f ( g))  )) )))").toString());
        assertEquals("[a, [b, [c, [d, [e, [f, [g]]]]]]]", parse("( a (  b (c (d   (e (f ( g) )  )) ) ) )").toString());
        assertEquals("[a, [b, [c, [d, [e, [f, [g]]]]]]]", parse("( a (  b ( c (d   (e   (f ( g  ) )  )) ) ) )").toString());
    }

    @Test
    public void testValidStrings() {
        assertEquals("''", parse("''").toString());
        assertEquals("' '", parse("' '").toString());
        assertEquals("'abc'", parse("'abc'").toString());
        assertEquals("'a (bc'", parse("'a (bc'").toString());
        assertEquals("'ab)c'", parse("'ab)c'").toString());
        assertEquals("'ab) c'", parse("'ab) c'").toString());
        assertEquals("'abc'", parse(" 'abc' ").toString());
        assertEquals("'ab c'", parse(" 'ab c' ").toString());
        assertEquals("'a   b c'", parse(" 'a   b c' ").toString());
        assertEquals("['abc']", parse("('abc')").toString());
        assertEquals("['abc']", parse("( 'abc')").toString());
        assertEquals("['abc']", parse("( 'abc'  )").toString());
        assertEquals("[' a b c ']", parse(" ( ' a b c '  ) ").toString());
        assertEquals("[['a', [''], ['b c', 'd']]]", parse(" ( ('a' ('') ('b c' 'd') )) ").toString());
    }

    @Test
    public void testInvalidStrings() {
        parse("'", "unterminated string");
        parse(" ' ", "unterminated string");
        parse(" 'a ", "unterminated string");
        parse(" 'a b ", "unterminated string");
        parse("( 'a b ", "unterminated string");
        parse(" 'a b ) ", "unterminated string");
    }

    @Test
    public void testNumbers() {
        assertEquals("0", parse("0").toString());
        assertEquals("1", parse("1").toString());
        assertEquals("-1", parse("-1").toString());
        assertEquals("+1", parse("+1").toString());
        assertEquals("1.2342424", parse("1.2342424").toString());
    }

    private static Ast parse(String script) {
        Parser parser = new Parser();
        return parser.parse(script);
    }

    private static void parse(String script, String message) {
        Parser parser = new Parser();
        try {
            parser.parse(script);
            fail("Expected syntax error with message: " + message);
        } catch (SyntaxException e) {
            assertTrue(
                    "Expected syntax error: " + message + " but got: " + e.getMessage(),
                    e.getMessage().toLowerCase().contains(message.toLowerCase()));
        }
    }
}