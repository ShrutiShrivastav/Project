package com.example.project;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    TextView txt;
    String amount;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        txt=(TextView) (findViewById(R.id.r6));
        amount=getIntent().getStringExtra("Totalamount");
        txt.setText("Total amount to be paid is Rs "+amount);
        home=(Button)findViewById(R.id.homebutton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(PaymentActivity.this,HomeActivity.class);

                startActivity(i);
            }
        });

    }
}

