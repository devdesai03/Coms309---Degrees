package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    Button increaseBtn;
    Button decreaseBtn;
    ProgressBar progressBar;
    Button backBtn;
    TextView numberTxt;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        increaseBtn = findViewById(R.id.increaseBtn);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        progressBar = findViewById(R.id.progressBar);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
                counter++;
                progressBar.setProgress(counter);
                numberTxt.setText(String.valueOf(counter));

                if (counter == 50) {
                    progressBar.setVisibility(View.INVISIBLE);
                    numberTxt.setText("You Win");
                    increaseBtn.setEnabled(false); // Disable the increase button
                    decreaseBtn.setEnabled(false); // Disable the decrease button
                }

                // Check if the counter reaches -50
                if (counter == -50) {
                    progressBar.setVisibility(View.INVISIBLE);
                    numberTxt.setText("You Lose");
                    increaseBtn.setEnabled(false); // Disable the increase button
                    decreaseBtn.setEnabled(false); // Disable the decrease button
                }
            }
        });


        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
                counter--;
                progressBar.setProgress(counter);
                numberTxt.setText(String.valueOf(counter));

                if (counter == 50) {
                    progressBar.setVisibility(View.INVISIBLE);
                    numberTxt.setText("You Win!");
                    increaseBtn.setEnabled(false); // Disable the increase button
                    decreaseBtn.setEnabled(false); // Disable the decrease button
                }

                // Check if the counter reaches -50
                if (counter == -50) {
                    progressBar.setVisibility(View.INVISIBLE);
                    numberTxt.setText("You Lose!");
                    increaseBtn.setEnabled(false); // Disable the increase button
                    decreaseBtn.setEnabled(false); // Disable the decrease button
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}