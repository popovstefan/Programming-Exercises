package aps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExpressionEvaluator {

    public static int evaluateExpression(String expression){
        int result = 0;
        String [] multipliers = expression.split("\\+");
        for (String multiplier : multipliers) {
            String [] numbers = multiplier.split("\\*");
            int intermediateResult = 1;
            for (String number : numbers)
                intermediateResult *= Integer.parseInt(number);
            result += intermediateResult;
        }
        return result;

    }
    public static void main(String[] args) throws IOException {
        BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
        System.out.println(evaluateExpression(input.readLine()));
    }

}