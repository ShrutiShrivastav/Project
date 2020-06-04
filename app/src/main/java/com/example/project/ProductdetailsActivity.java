package com.example.project;


import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.project.Model.Products;
import com.example.project.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductdetailsActivity extends AppCompatActivity {

    protected FloatingActionButton addtocart;
    private ImageView productimage;
    protected ElegantNumberButton numberButton;
    private TextView productprice,producedescription,productname;
    private String productID=" " ,state="Normal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        productID=getIntent().getStringExtra("pid");

        addtocart=(FloatingActionButton)findViewById(R.id.add_product_to_cart);
        numberButton=(ElegantNumberButton) findViewById(R.id.number_btn);
        productimage=(ImageView)findViewById(R.id.product_image_details);
        productname=(TextView)findViewById(R.id.product_name_details);
        producedescription=(TextView)findViewById(R.id.product_description_details);
        productprice=(TextView)findViewById(R.id.product_price_details);

        getproductdetails(productID);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(state.equals("Order Pleaced") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductdetailsActivity.this,"You can purchase more",Toast.LENGTH_LONG).show();
                }
                else {
                    addtocastlist();
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckorderState();
    }

    private void addtocastlist() {

        String savecurrenttime, savecurrentdate;

        Calendar CalForData= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd,yyyy");
        savecurrentdate= currentDate.format(CalForData.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:MM:ss a");
        savecurrenttime= currentDate.format(CalForData.getTime());


        final  DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productname.getText().toString());
        cartMap.put("price",productprice.getText().toString());
        cartMap.put("date",savecurrentdate);
        cartMap.put("time",savecurrenttime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProductdetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                Intent i =new Intent(ProductdetailsActivity.this,HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });

    }

    private void getproductdetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Products products= dataSnapshot.getValue(Products.class);

                    productname.setText(products.getPname());
                    producedescription.setText(products.getDescription());
                    productprice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productimage);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void CheckorderState()
    {
        DatabaseReference ordersRef;
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingstate=dataSnapshot.child("state").getValue().toString();

                    if(shippingstate.equals("shipped"))
                    {
                        state="Order shipped";


                    }
                    else if(shippingstate.equals("Not shipped"))

                    {
                        state="Order placed";


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


    }

}
