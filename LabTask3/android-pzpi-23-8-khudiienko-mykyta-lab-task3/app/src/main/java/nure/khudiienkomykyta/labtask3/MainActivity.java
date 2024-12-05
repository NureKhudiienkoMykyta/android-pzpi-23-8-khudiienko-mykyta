package nure.khudiienkomykyta.labtask3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    private EditText editText;
    private String input = "";
    private double firstNumber = 0;
    private String operator = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText =findViewById(R.id.editText);
    }


    @Override
    protected void onStart() {
        super.onStart();


        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                input += button.getText().toString();
                editText.setText(input);
            }
        };


        findViewById(R.id.but_0).setOnClickListener(numberClickListener);
        findViewById(R.id.but_1).setOnClickListener(numberClickListener);
        findViewById(R.id.but_2).setOnClickListener(numberClickListener);
        findViewById(R.id.but_3).setOnClickListener(numberClickListener);
        findViewById(R.id.but_4).setOnClickListener(numberClickListener);
        findViewById(R.id.but_5).setOnClickListener(numberClickListener);
        findViewById(R.id.but_6).setOnClickListener(numberClickListener);
        findViewById(R.id.but_7).setOnClickListener(numberClickListener);
        findViewById(R.id.but_8).setOnClickListener(numberClickListener);
        findViewById(R.id.but_9).setOnClickListener(numberClickListener);

        findViewById(R.id.but_ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });


        findViewById(R.id.but_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOperator("/");
            }
        });
        findViewById(R.id.but_dob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOperator("*");
            }
        });
        findViewById(R.id.but_eq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
        findViewById(R.id.but_min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOperator("-");
            }
        });
        findViewById(R.id.but_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOperator("+");
            }
        });


        findViewById(R.id.but_koma).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.contains(".")) {
                    if (input.isEmpty()) {
                        input = "0.";
                    } else {
                        input += ".";
                    }
                    editText.setText(input);
                }
            }
        });
    }


    private void clear() {
        input = "";
        operator = "";
        firstNumber = 0;
        editText.setText("");
    }

    private void setOperator(String op) {
        if (!input.isEmpty()) {
            firstNumber = Double.parseDouble(input);
            operator = op;
            input = "";
            editText.setText("");
        }
    }

    private void calculate() {
        if (!input.isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(input);
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        editText.setText("Error");
                        return;
                    }
                    break;
            }

            editText.setText(String.valueOf(result));
            input = String.valueOf(result);
            operator = "";
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("input", input);
        outState.putDouble("firstNumber", firstNumber);
        outState.putString("operator", operator);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        input = savedInstanceState.getString("input", "");
        firstNumber = savedInstanceState.getDouble("firstNumber", 0);
        operator = savedInstanceState.getString("operator", "");

        editText.setText(input);
    }
}



