package com.sheaffer.myapplication.data;

import com.sheaffer.myapplication.R;
import com.sheaffer.myapplication.utils.ResourceProvider;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;

public class RpnCalculator {

    private final ResourceProvider resourceProvider = ResourceProvider.instance;
    final Deque<Double> stack = new ArrayDeque<>();

    public void pushValue(double value) {
        stack.push(value);
    }

    public String performOperation(String operator) throws IllegalArgumentException {
        if (stack.size() < 2) throw new IllegalArgumentException(resourceProvider.getString(R.string.notEnoughValues));

        double b = stack.pop();
        double a = stack.pop();

        switch (operator) {
            case "+":
                stack.push(a + b);
                break;
            case "-":
                stack.push(a - b);
                break;
            case "*":
                stack.push(a * b);
                break;
            case "/":
                if (b == 0) throw new IllegalArgumentException(resourceProvider.getString(R.string.cannotDivideBy0));
                stack.push(a / b);
                break;
            default:
                throw new IllegalArgumentException(resourceProvider.getString(R.string.invalidOperator, operator));
        }

        double result = stack.peek();
        return formatResult(result);
    }

    public String processExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) throw new IllegalArgumentException(resourceProvider.getString(R.string.expressionCannotBeEmpty));

        String[] tokens = expression.split("\\s+");

        try {
            for (String token : tokens) {
                if (isNumeric(token)) {
                    pushValue(Double.parseDouble(token));
                } else {
                    if (stack.size() < 2) {
                        return resourceProvider.getString(R.string.notEnoughNumber, token);
                    }
                    performOperation(token);
                }
            }
            if (stack.size() > 1) {
                return resourceProvider.getString(R.string.tooManyNumbers);
            }

            double result = stack.peek();
            return formatResult(result);
        } catch (IllegalArgumentException e) {
            return resourceProvider.getString(R.string.error, e.getMessage());
        }
    }

    public void clear() {
        stack.clear();
    }

    public double peek() {
        return stack.isEmpty() ? 0.0 : stack.peek();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String formatResult(double result) {
        // If the result is a whole number, return it as an integer string
        if (result == (int) result) {
            return String.valueOf((int) result);
        }
        return String.format(Locale.getDefault(), "%.2f", result);
    }
}

