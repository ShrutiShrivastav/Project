package com.example.project;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class registeractivity extends AppCompatActivity {

    EditText inputname,inputpass,inputphone;
    Button registerbttn;
    private ProgressDialog loadingBar;

    private static final Pattern phone_pattern=
            Pattern.compile("^[7-9]\\d{9}$");

    private static final Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9_-]{6,14}$");


    private static final Pattern password_pattern=
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        inputname = (EditText) findViewById(R.id.name_reg);
        inputpass = (EditText) findViewById(R.id.passw_reg);

        inputphone = (EditText) findViewById(R.id.login_reg);
        registerbttn = (Button) findViewById(R.id.reg_bttn);
        loadingBar=new ProgressDialog(this);

        registerbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {

        String name= inputname.getText().toString();
        String phone=inputphone.getText().toString();
        String password=inputpass.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter your name..",Toast.LENGTH_SHORT).show();
        }

        else if(!userNamePattern.matcher(name).matches())
        {
            Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please enter your phone number..",Toast.LENGTH_SHORT).show();
        }

        else if(!phone_pattern.matcher(phone).matches())
        {
            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
        }


        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter your password..",Toast.LENGTH_SHORT).show();
        }
        else if(!password_pattern.matcher(password).matches())
        {
            Toast.makeText(this, "Atleast 1 special char req, Min 4 characters required", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name,phone,password);
        }

    }

    private void ValidatephoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference RoofRef;
        RoofRef= FirebaseDatabase.getInstance().getReference();

        RoofRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.child("Users").child(phone).exists())
                {
                    HashMap<String, Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RoofRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(registeractivity.this, "Your account has been created",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent=new Intent(registeractivity.this,loginactivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(registeractivity.this, "Network error.Please try again",Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

                }

                else
                {
                    Toast.makeText(registeractivity.this,"This"+phone+"already exits",Toast.LENGTH_SHORT).show();
                    Toast.makeText(registeractivity.this,"Please try again using another phone number",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(registeractivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


