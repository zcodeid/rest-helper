package id.zcode.rest;

import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;

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
        String s = "name:Mie   Gore*,or,type:0";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 3, deque.size());
    }
    @Test
    public void testParseUnderScore(){
        String s = "name:Mie_Gore*,or,type:0";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 3, deque.size());
        String value = (String) ((SpecSearchCriteria) ((LinkedList) deque).get(2)).getValue();
        assertEquals("value with underscor", "mie_gore", value);
    }
}
