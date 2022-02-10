import org.junit.Test;

import static org.junit.Assert.*;

public class InterpreterTest {

    @Test
    public void testEmpty() {
        assertEquals(null, eval(null));
        assertEquals(null, eval(""));
        assertEquals(null, eval(" "));
    }

    @Test
    public void testBooleans() {
        assertTrue((boolean)eval("true"));
        assertFalse((boolean)eval("false"));
    }

    @Test
    public void testEq() {
        assertTrue((boolean)eval("(= true true)"));
        assertTrue((boolean)eval("(= false false)"));
        assertFalse((boolean)eval("(= true false)"));
        assertFalse((boolean)eval("(= false true)"));
        assertFalse((boolean)eval("(= 'apple' 'orange')"));
        assertTrue((boolean)eval("(= 'apple' 'apple')"));
        assertTrue((boolean)eval("(= 0 0)"));
        assertTrue((boolean)eval("(= -10.33242 -10.33242)"));
    }

    @Test
    public void testNotEq() {
        assertFalse((boolean)eval("(!= true true)"));
        assertFalse((boolean)eval("(!= false false)"));
        assertTrue((boolean)eval("(!= true false)"));
        assertTrue((boolean)eval("(!= false true)"));
        assertTrue((boolean)eval("(!= 'apple' 'orange')"));
        assertFalse((boolean)eval("(!= 'apple' 'apple')"));
        assertFalse((boolean)eval("(!= 0 0)"));
        assertFalse((boolean)eval("(!= -10.33242 -10.33242)"));
    }

    @Test
    public void testOr() {
        assertTrue((boolean)eval("(or true true)"));
        assertTrue((boolean)eval("(or true false)"));
        assertTrue((boolean)eval("(or false true)"));
        assertFalse((boolean)eval("(or false false)"));
        assertTrue((boolean)eval("(or false false false true false)"));
        assertFalse((boolean)eval("(or false false false false false)"));
    }

    @Test
    public void testAnd() {
        assertTrue((boolean)eval("(and true true)"));
        assertFalse((boolean)eval("(and false false)"));
        assertFalse((boolean)eval("(and true false)"));
        assertFalse((boolean)eval("(and false true)"));
        assertFalse((boolean)eval("(and true true true false)"));
        assertTrue((boolean)eval("(and true true true true)"));
    }

    @Test
    public void testNot() {
        assertFalse((boolean)eval("(not true)"));
        assertTrue((boolean)eval("(not false)"));
        assertFalse((boolean)eval("(not (not false))"));
        assertTrue((boolean)eval("(not (not true))"));
    }

    @Test
    public void testComplex() {
        assertTrue((boolean)eval("(and (not false) (or (not (or (not true) (not false) )) true))"));
    }

    @Test(expected = TypeException.class)
    public void testTypeError() {
        eval("(size 12)");
    }

    @Test
    public void testStrings() {
        assertEquals("", eval("''"));
        assertEquals(" a b c ", eval("' a b c '"));
    }

    private Object eval(String script) {
        Interpreter interpreter = new Interpreter();
        Parser parser = new Parser();
        return interpreter.eval(parser.parse(script));
    }
}