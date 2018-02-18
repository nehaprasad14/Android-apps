package com.example.android.temperatureconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView resTextView = (TextView)findViewById(R.id.textView4);
        resTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private static final String TAG = "MainActivity";

    String resultHistory = "";

    public void convertClick(View v){
        Log.d(TAG, "convertClick:start ");
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        final String value =
                ((RadioButton)findViewById(rg.getCheckedRadioButtonId()))
                        .getText().toString();
        Log.d(TAG, "value : "+value);

        //Get input value
        EditText inputEditText = (EditText)findViewById(R.id.inputText);
        EditText outputEditText = (EditText)findViewById(R.id.outputText);
        String inValue = inputEditText.getText().toString();

        //Fahrenheit to Celsius
        if(value.equalsIgnoreCase("Fahrenheit to Celsius")){
            if(!inValue.isEmpty()){
                Log.d(TAG, "inValue : "+inValue);
                Double input = Double.parseDouble(inValue);
                Double res = (input - 32.0) * 5.0/9.0;
                outputEditText.setText(String.format("%.1f",res));
                displayHistory(1,String.format("%.1f",input),String.format("%.1f",res));
                Log.d(TAG, "outValue : "+String.format("%.1f",res));
            }else{
                Toast.makeText(this, "Please enter proper value", Toast.LENGTH_SHORT).show();
            }
        }
        //Celsius to Fahrenheit
        else if(value.equalsIgnoreCase("Celsius to Fahrenheit")){
            if(!inValue.isEmpty()){
                Log.d(TAG, "inValue : "+inValue);
                Double input = Double.parseDouble(inValue);
                Double res = (input * 9.0/5.0) + 32.0;
                outputEditText.setText(String.format("%.1f",res));
                displayHistory(2,String.format("%.1f",input),String.format("%.1f",res));
                Log.d(TAG, "outValue : "+String.format("%.1f",res));
            }else{
                Toast.makeText(this, "Please enter proper value", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please select proper conversion type!", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "convertClick:end ");
    }

    public void displayHistory(int chk1,String inValue,String outValue){
        String latestResult = "";
        if(chk1==1)
            latestResult = "F to C: "+inValue+" = "+outValue+"\n";
        else
            latestResult = "C to F: "+inValue+" = "+outValue+"\n";

        resultHistory =  latestResult + resultHistory ;
        Log.d(TAG, "resultHistory: "+resultHistory);
        TextView resTextView = (TextView)findViewById(R.id.textView4);
        resTextView.setText(resultHistory);
    }
}
