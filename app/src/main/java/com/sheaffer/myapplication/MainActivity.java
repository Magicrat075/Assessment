package com.sheaffer.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private RpnViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the ViewModel
        viewModel = new ViewModelProvider(this).get(RpnViewModel.class);

        // Initialize UI elements
        ToggleButton toggleMode = findViewById(R.id.toggleMode);
        LinearLayout singleStepLayout = findViewById(R.id.singleStepLayout);
        LinearLayout batchModeLayout = findViewById(R.id.batchModeLayout);
        TextView displaySingleStep = findViewById(R.id.display);
        EditText inputBatch = findViewById(R.id.inputBatch);
        Button btnCalculateBatch = findViewById(R.id.btnCalculateBatch);
        Button btnClearBatch = findViewById(R.id.btnClearBatch);

        // Observe the display value
        viewModel.display.observe(this, displaySingleStep::setText);

        // Toggle mode
        toggleMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setSingleStepMode(isChecked);
            singleStepLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            batchModeLayout.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });

        // Single button clicks
        View.OnClickListener singleStepClickListener = v -> {
            Button button = (Button) v;
            String text = button.getText().toString();

            if (text.equalsIgnoreCase(getString(R.string.clear))) {
                viewModel.clear();
                return;
            }

            if (text.equalsIgnoreCase(getString(R.string.next))) {
                viewModel.commitCurrentInput();
                displaySingleStep.setText(viewModel.peek());
                return;
            }

            if (isOperator(text)) {
                viewModel.performOperation(text);
            } else {
                viewModel.appendToCurrentInput(text);
            }
        };

        // Initialize single number buttons
        for (int id : new int[]{R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDot, R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv, R.id.btnNext, R.id.btnClear}) {
            findViewById(id).setOnClickListener(singleStepClickListener);
        }

        // Batch mode
        btnCalculateBatch.setOnClickListener(v -> {
            String input = inputBatch.getText().toString().trim();
            if (!input.isEmpty()) {
                viewModel.processExpression(input);
            }
        });

        btnClearBatch.setOnClickListener(v -> {
            inputBatch.setText("");
            viewModel.clear();
        });
    }

    // Used to validate if the string is an operator
    private boolean isOperator(String operator) {
        return "+".equals(operator) || "-".equals(operator) || "*".equals(operator) || "/".equals(operator);
    }
}