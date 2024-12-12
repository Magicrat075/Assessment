package com.sheaffer.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.sheaffer.myapplication.data.RpnCalculator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RpnViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private RpnViewModel viewModel;

    @Mock
    private Observer<String> observer;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new RpnViewModel();
        viewModel.display.observeForever(observer);
    }

    @Test
    public void testInitialDisplay() {
        // Initial display should be "0"
        assertEquals("0", viewModel.display.getValue());
        verify(observer).onChanged("0");
    }

    @Test
    public void testPushValueUpdatesDisplay() {
        viewModel.pushValue(5);
        assertEquals("5.0", viewModel.display.getValue());
        verify(observer).onChanged("5.0");
    }

    @Test
    public void testPerformOperationAddition() {
        viewModel.pushValue(5);
        viewModel.pushValue(3);
        viewModel.performOperation("+");
        assertEquals("8.0", viewModel.display.getValue());
        verify(observer).onChanged("8.0");
    }

    @Test
    public void testPerformOperationSubtraction() {
        viewModel.pushValue(5);
        viewModel.pushValue(3);
        viewModel.performOperation("-");
        assertEquals("2.0", viewModel.display.getValue());
        verify(observer).onChanged("2.0");
    }

    @Test
    public void testPerformOperationMultiplication() {
        viewModel.pushValue(5);
        viewModel.pushValue(3);
        viewModel.performOperation("*");
        assertEquals("15.0", viewModel.display.getValue());
        verify(observer).onChanged("15.0");
    }

    @Test
    public void testPerformOperationDivision() {
        viewModel.pushValue(6);
        viewModel.pushValue(3);
        viewModel.performOperation("/");
        assertEquals("2.0", viewModel.display.getValue());
        verify(observer).onChanged("2.0");
    }

    @Test
    public void testDivisionByZero() {
        viewModel.pushValue(6);
        viewModel.pushValue(0);
        viewModel.performOperation("/");
        assertEquals("Cannot divide by zero", viewModel.display.getValue());
        verify(observer).onChanged("Cannot divide by zero");
    }

    @Test
    public void testInvalidOperation() {
        viewModel.pushValue(6);
        viewModel.pushValue(3);
        viewModel.performOperation("^");
        assertEquals("Invalid operator", viewModel.display.getValue());
        verify(observer).onChanged("Invalid operator");
    }

    @Test
    public void testProcessExpression() {
        viewModel.processExpression("5 3 +");
        assertEquals("8.0", viewModel.display.getValue());
        verify(observer).onChanged("8.0");
    }

    @Test
    public void testProcessExpressionInvalid() {
        viewModel.processExpression("5 +");
        assertEquals("Not enough values to perform operation", viewModel.display.getValue());
        verify(observer).onChanged("Not enough values to perform operation");
    }

    @Test
    public void testClear() {
        viewModel.pushValue(5);
        clearInvocations(observer);
        viewModel.clear();
        verify(observer, times(1)).onChanged("0");
    }

    @Test
    public void testSetSingleStepMode() {
        viewModel.setSingleStepMode(false);
        assertFalse(viewModel.isSingleStepMode());
        verify(observer, atLeastOnce()).onChanged("0");

        viewModel.setSingleStepMode(true);
        assertTrue(viewModel.isSingleStepMode());
        verify(observer, atLeastOnce()).onChanged("0");
    }
}

