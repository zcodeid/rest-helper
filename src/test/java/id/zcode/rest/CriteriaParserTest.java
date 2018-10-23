package id.zcode.rest;

import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class CriteriaParserTest {

    @Test
    public void testParseWhiteSpace(){
        String s = "name:Mie Gore*";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 1, deque.size());
    }

    @Test
    public void testParseDash(){
        String s = "name:Mie-Gore*";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 1, deque.size());
    }

    @Test
    public void testParseWhiteSpace2(){
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

    @Test
    public void testParseUnderScore(){
        String s = "name:Mie_Gore*,or,type:0";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 3, deque.size());
        String value = (String) ((SpecSearchCriteria) ((LinkedList) deque).get(2)).getValue();
        assertEquals("value with underscor", "mie_gore", value);
    }

    @Test
    public void testDash(){
        String s = "name:Mie-Gore*,or,type:0";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        assertEquals("number of token is 3", 3, deque.size());
    }

    @Test
    public void testAsterikStartWith(){
        String s = "name:Mie*";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        SpecSearchCriteria ssc = (SpecSearchCriteria) deque.getFirst();
        assertEquals("should start with", SearchOperation.STARTS_WITH, ssc.getOperation());
    }
    @Test
    public void testAsterikEndWith(){
        String s = "name:*Mie";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        SpecSearchCriteria ssc = (SpecSearchCriteria) deque.getFirst();
        assertEquals("should end with", SearchOperation.ENDS_WITH, ssc.getOperation());
    }

    @Test
    public void testAsterikContains(){
        String s = "name:*Mie*";
        CriteriaParser cp = new CriteriaParser();
        Deque<?> deque = cp.parse(s);
        SpecSearchCriteria ssc = (SpecSearchCriteria) deque.getFirst();
        assertEquals("should contains", SearchOperation.CONTAINS, ssc.getOperation());
    }
}
