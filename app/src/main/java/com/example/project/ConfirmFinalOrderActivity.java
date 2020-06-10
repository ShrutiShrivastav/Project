package com.example.project;

import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;  //new
import androidx.appcompat.app.AppCompatActivity;  //new

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameedittext,addressedittext,phoneedittext,cityedittext;
    private Button confirmButton;

    private static final Pattern phoneno_pattern=
            Pattern.compile("^[7-9]\\d{9}$");

    //private static final Pattern NamePattern = Pattern.compile("[a-zA-Z]");

    public String Totalamount=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        Totalamount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this,"total price =Rs" + Totalamount,Toast.LENGTH_SHORT).show();

        final TextView t=(TextView)findViewById(R.id.txt);
        nameedittext=(EditText)findViewById(R.id.shipment_name);
        phoneedittext=(EditText)findViewById(R.id.shipment_phone_number);
        addressedittext=(EditText)findViewById(R.id.shipment_address);
        cityedittext=(EditText)findViewById(R.id.shipment_city);
        confirmButton=(Button)findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                check();
            }
        });

    }

    private void check() {




        if(TextUtils.isEmpty(nameedittext.getText().toString()))
        {
            Toast.makeText(this,"Please enter your full name",Toast.LENGTH_SHORT).show();

        }


        else if(TextUtils.isEmpty(phoneedittext.getText().toString()))
        {
            Toast.makeText(this,"Please enter your phone number",Toast.LENGTH_SHORT).show();

        }

        else if(!phoneno_pattern.matcher(phoneedittext.getText().toString()).matches())
        {
            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(addressedittext.getText().toString()))
        {
            Toast.makeText(this,"Please enter your full address",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(cityedittext.getText().toString()))
        {
            Toast.makeText(this,"Please enter your city name",Toast.LENGTH_SHORT).show();

        }
        else
        {
            confirmorder();
        }

    }

    private void confirmorder()
    {
        final String savecurrentdate,savecurrenttime;

        Calendar CalForData= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd,yyyy");
        savecurrentdate= currentDate.format(CalForData.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:MM:ss a");
        savecurrenttime= currentTime.format(CalForData.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance("https://bookmart-b2ad7.firebaseio.com/").getReference()
                .child("Orders")
                .child(Prevalent.CurrentOnlineUser.getPhone());


        HashMap<String ,Object> ordersMap= new HashMap<>();
        ordersMap.put("amount",Totalamount);
        ordersMap.put("name",nameedittext.getText().toString());
        ordersMap.put("phone",phoneedittext.getText().toString());
        ordersMap.put("address",addressedittext.getText().toString());
        ordersMap.put("city",cityedittext.getText().toString());
        ordersMap.put("date",savecurrentdate);
        ordersMap.put("time",savecurrenttime);
        ordersMap.put("state","Not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance("https://bookmart-b2ad7.firebaseio.com/").getReference()
                            .child("Cart List")
                            .child("User view")
                            .child(Prevalent.CurrentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Your final order has been placed",Toast.LENGTH_SHORT).show();

                                        Intent i =new Intent(ConfirmFinalOrderActivity.this,PaymentActivity.class);
                                        i.putExtra("Totalamount",Totalamount );
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();


                                    }
                                }
                            });

                }
            }
        });




    }
}
