package com.sheaffer.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.sheaffer.myapplication.data.RpnCalculator;

import org.junit.Before;
import org.junit.Test;



public class RpnCalculatorTest {

    private RpnCalculator calculator;

    @Before
    public void setUp() {
        calculator = new RpnCalculator();
    }

    @Test
    public void test_pushValue() {
        calculator.pushValue(5);
        assertEquals(5, calculator.peek(), 0.001);
    }

    @Test
    public void test_addition() {
        calculator.pushValue(5);
        calculator.pushValue(8);
        assertEquals("13", calculator.performOperation("+"));
    }

    @Test
    public void test_additionDouble() {
        calculator.pushValue(5.0);
        calculator.pushValue(8.0);
        assertEquals("13.0", calculator.performOperation("+"));
    }

    @Test
    public void test_subtraction() {
        calculator.pushValue(10);
        calculator.pushValue(3);
        assertEquals("7", calculator.performOperation("-"));
    }

    @Test
    public void test_subtractionDouble() {
        calculator.pushValue(10.0);
        calculator.pushValue(3.0);
        assertEquals("7.0", calculator.performOperation("-"));
    }

    @Test
    public void test_multiplication() {
        calculator.pushValue(4);
        calculator.pushValue(7);
        assertEquals("28", calculator.performOperation("*"));
    }

    @Test
    public void test_multiplicationDouble() {
        calculator.pushValue(4.0);
        calculator.pushValue(7.0);
        assertEquals("28.0", calculator.performOperation("*"));
    }

    @Test
    public void test_division() {
        calculator.pushValue(20);
        calculator.pushValue(5);
        assertEquals("4", calculator.performOperation("/"));
    }

    @Test
    public void test_divisionDouble() {
        calculator.pushValue(20.0);
        calculator.pushValue(5.0);
        assertEquals("4.0", calculator.performOperation("/"));
    }

    @Test
    public void test_divisionByZero() {
        calculator.pushValue(10);
        calculator.pushValue(0);
        assertThrows(IllegalArgumentException.class, () -> calculator.performOperation("/"));
    }

    @Test
    public void test_notEnoughValues() {
        calculator.pushValue(5);
        assertThrows(IllegalArgumentException.class, () -> calculator.performOperation("+"));
    }

    @Test
    public void test_invalidOperator() {
        calculator.pushValue(5);
        calculator.pushValue(3);
        assertThrows(IllegalArgumentException.class, () -> calculator.performOperation("%"));
    }

    @Test
    public void test_clear() {
        calculator.pushValue(5);
        calculator.pushValue(10);
        calculator.clear();
        assertEquals(0.0, calculator.peek(), 0.001);
    }

    @Test
    public void testClearStack() {
        calculator.pushValue(5);
        calculator.pushValue(8);
        calculator.performOperation("+");
        calculator.clear();
        assertEquals(0.0, calculator.peek(), 0.001);
    }

    @Test
    public void test_peekEmptyStack() {
        assertEquals(0.0, calculator.peek(), 0.001);
    }

    @Test
    public void test_complexExpression() {
        calculator.pushValue(5);
        calculator.pushValue(8);
        calculator.performOperation("+");
        calculator.pushValue(3);
        assertEquals("39", calculator.performOperation("*"));
    }

    @Test
    public void test_orderOfOperations() {
        calculator.pushValue(15);
        calculator.pushValue(7);
        calculator.pushValue(1);
        calculator.pushValue(1);
        calculator.performOperation("+");
        calculator.performOperation("-");
        calculator.performOperation("/");
        calculator.pushValue(3);
        assertEquals("9", calculator.performOperation("*"));
    }

    @Test
    public void testStepByStepInput() {
        calculator.pushValue(15);
        calculator.pushValue(7);
        calculator.pushValue(1);
        calculator.pushValue(1);
        calculator.performOperation("+");
        calculator.performOperation("-");
        calculator.pushValue(3);
        calculator.performOperation("*");
        calculator.pushValue(2);
        assertEquals("7.5", calculator.performOperation("/"));
    }

    @Test
    public void testBatchInput() {
        String expression = "15 7 1 1 + - 3 * 2 /";
        String result = calculator.processExpression(expression);
        assertEquals("7.5", result);
    }

    @Test
    public void testInvalidBatchInput() {
        assertThrows(IllegalArgumentException.class, () -> calculator.processExpression("15 7 + -"));
    }

    @Test
    public void testEmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> calculator.processExpression(""));
    }

    @Test
    public void testDivisionByZeroInBatch() {
        String expression = "10 0 /";
        assertThrows(IllegalArgumentException.class, () -> calculator.processExpression(expression));
    }

    @Test
    public void testFormattedBatchInput() {
        String expression = "15 7 1 1 + - /";
        String result = calculator.processExpression(expression);
        assertEquals("3", result);
        calculator.clear();

        String expressionWithDecimal = "15.5 7 1.2 + -";
        String resultWithDecimal = calculator.processExpression(expressionWithDecimal);
        assertEquals("7.30", resultWithDecimal);
        calculator.clear();

        String expressionWithTooManyOperators = "15.5 7 1.2 + - /";
        String resultWithError = calculator.processExpression(expressionWithTooManyOperators);
        assertEquals("Error: Not enough numbers for operator /", resultWithError);
        calculator.clear();

        String expressionWithTooManyNumbers = "15.5 7 1.2 +";
        String resultWithOtherError = calculator.processExpression(expressionWithTooManyNumbers);
        assertEquals("Error: Too many numbers left on the stack.", resultWithOtherError);
    }
}

