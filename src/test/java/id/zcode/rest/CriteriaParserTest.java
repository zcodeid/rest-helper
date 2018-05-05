package id.zcode.rest;

import org.junit.Test;

import java.util.Deque;

import static org.junit.Assert.assertEquals;

public class CriteriaParserTest {

    @Test
    public void testParseWhiteSpace(){
        String s = "name:Mie*,or,type:0";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 3, deque.size());
    }

    @Test
    public void testParseComma(){
        String s = "name:Mie Gore*,or,type:0";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 3, deque.size());
    }
}
