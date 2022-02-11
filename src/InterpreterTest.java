import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.*;

public class InterpreterTest {
    Interpreter interpreter = new Interpreter();
    Parser parser = new Parser();

    @Test
    public void testEmpty() {
        assertNull(eval(null));
        assertNull(eval(""));
        assertNull(eval(" "));
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

    @Test
    public void testMatchString() {
        assertEquals(true, eval("(match 'user1' 'user\\d+')"));
        assertEquals(true, eval("(match 'user12' 'user\\d+')"));
        assertEquals(false, eval("(match 'user12d' 'user\\d+')"));
        assertEquals(false, eval("(match 'user' 'user\\d+')"));
        assertEquals(false, eval("(match '12' 'user\\d+')"));
    }

    @Test
    public void testMatchList() {
        interpreter.addConstant("groups", singletonList("grp1"));
        assertEquals(true, eval("(match groups 'grp\\d+')"));
        interpreter.addConstant("groups", singletonList("grp12"));
        assertEquals(true, eval("(match groups 'grp\\d+')"));
        interpreter.addConstant("groups", singletonList("grp12d"));
        assertEquals(false, eval("(match groups 'grp\\d+')"));
        interpreter.addConstant("groups", singletonList("grp"));
        assertEquals(false, eval("(match groups 'grp\\d+')"));
        interpreter.addConstant("groups", singletonList("12"));
        assertEquals(false, eval("(match groups 'grp\\d+')"));
        interpreter.addConstant("groups", asList("12", "grp12"));
        assertEquals(true, eval("(match groups 'grp\\d+')"));
    }

    private Object eval(String script) {
        return interpreter.eval(parser.parse(script));
    }
}