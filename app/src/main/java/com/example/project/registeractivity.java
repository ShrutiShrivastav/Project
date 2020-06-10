package com.example.project;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;

public class registeractivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 34;
    private static final String TAG = "kritika" ;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    SignInButton signInButton;
    Button verify;
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
        signInButton= (SignInButton)findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        createRequest();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(registeractivity.this, user.getEmail()+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(registeractivity.this,  task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(registeractivity.this,HomeActivity.class);
        startActivity(intent);

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
        RoofRef= FirebaseDatabase.getInstance("https://bookmart-b2ad7.firebaseio.com/").getReference();

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

                                        Intent intent=new Intent(registeractivity.this,HomeActivity.class);
                                        startActivity(intent); //new
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

                    Intent intent=new Intent(registeractivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}


