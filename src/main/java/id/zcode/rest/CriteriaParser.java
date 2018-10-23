package id.zcode.rest;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;

public class CriteriaParser {

    private static Map<String, Operator> ops;

    private static Pattern SpecCriteraRegex = Pattern.compile("^(\\w+?)(" + Joiner.on("|")
            .join(SearchOperation.SIMPLE_OPERATION_SET) + ")(\\*?)([\\w-\\s]+)(\\*?)$");

    private enum Operator {
        OR(1), AND(2);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    static {
        Map<String, Operator> tempMap = new HashMap<>();
        tempMap.put("AND", Operator.AND);
        tempMap.put("OR", Operator.OR);
        tempMap.put("or", Operator.OR);
        tempMap.put("and", Operator.AND);

        ops = Collections.unmodifiableMap(tempMap);
    }

    private static boolean isHigerPrecedenceOperator(String currOp, String prevOp) {
        return (ops.containsKey(prevOp) && ops.get(prevOp).precedence >= ops.get(currOp).precedence);
    }

    public Deque<?> parse(String searchParam) {

        Deque<Object> output = new LinkedList<>();
        Deque<String> stack = new LinkedList<>();
        if (searchParam == null) return output;

        for (String token : searchParam.split(",")) {
            token = token.trim();
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrecedenceOperator(token, stack.peek()))
                    output.push(orAnd(stack.pop()));
                stack.push(orAnd(token));
            } else if (token.equals(SearchOperation.LEFT_PARANTHESIS)) {
                stack.push(SearchOperation.LEFT_PARANTHESIS);
            } else if (token.equals(SearchOperation.RIGHT_PARANTHESIS)) {
                while (!stack.peek()
                        .equals(SearchOperation.LEFT_PARANTHESIS))
                    output.push(stack.pop());
                stack.pop();
            } else {
                String dmtr = "_";
                String a = token;
                token = token.replaceAll("\\s+", dmtr);
                boolean isReplaced = !token.equals(a);
                Matcher matcher = SpecCriteraRegex.matcher(token);
                while (matcher.find()) {
                    String value = matcher.group(4);
                    value = (isReplaced ? value.replaceAll(dmtr, " ") : value).toLowerCase();
//                    value = value.replaceAll(dmtr, " ").toLowerCase();
                    output.push(new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), value, matcher.group(5)));
                }
            }
        }

        while (!stack.isEmpty())
            output.push(stack.pop());

        return output;
    }

    private String orAnd(String token){
        boolean isOr = token.equalsIgnoreCase(SearchOperation.OR_OPERATOR);
        return isOr ? SearchOperation.OR_OPERATOR : SearchOperation.AND_OPERATOR;
    }

}