package com.sheaffer.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sheaffer.myapplication.data.RpnCalculator;

public class RpnViewModel extends ViewModel {
    private final RpnCalculator calculator = new RpnCalculator();

    private final StringBuilder currentInput = new StringBuilder();

    //Live data to update the calculator "window"
    private final MutableLiveData<String> _display = new MutableLiveData<>("0");
    public LiveData<String> display = _display;

    private boolean singleStepMode = true;

    public boolean isSingleStepMode() {
        return singleStepMode;
    }

    public void setSingleStepMode(boolean mode) {
        singleStepMode = mode;
        // Reset the calculator when switching modes
        calculator.clear();
        _display.setValue("0");
    }

    public void pushValue(double value) {
        calculator.pushValue(value);
        _display.setValue(String.valueOf(calculator.peek()));
    }

    public void appendToCurrentInput(String input) {
        currentInput.append(input);
        _display.setValue(currentInput.toString());
    }

    public void commitCurrentInput() {
        if (currentInput.length() > 0) {
            try {
                double value = Double.parseDouble(currentInput.toString());
                pushValue(value);
                currentInput.setLength(0);
            } catch (NumberFormatException e) {
                _display.setValue("Error");
            }
        }
    }

    public void performOperation(String operator) {
        commitCurrentInput();
        try {
            String result = calculator.performOperation(operator);
            _display.setValue(String.valueOf(result));
        } catch (IllegalArgumentException e) {
            _display.setValue(e.getMessage());
        }
    }

    public void processExpression(String expression) {
        try {
            String result = calculator.processExpression(expression);
            _display.setValue(String.valueOf(result));
        } catch (IllegalArgumentException e) {
            _display.setValue(e.getMessage());
        }
    }

    public void clear() {
        calculator.clear();
        _display.setValue("0");
    }

    public String peek() {
        return String.valueOf(calculator.peek());
    }
}

