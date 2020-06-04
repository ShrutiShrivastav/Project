package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategory extends AppCompatActivity {

    ImageView books ,hole,brush,draft,glue,magazine,stat,spiral,stapler,pencil;
    private Button logout,checkorderstate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logout=(Button)findViewById(R.id.admin_logout);
        checkorderstate=(Button)findViewById(R.id.check_new_orders);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategory.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkorderstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategory.this,AdminNewProductActivity.class);
                startActivity(intent);


            }
        });

        books=(ImageView)findViewById(R.id.books);
        hole=(ImageView)findViewById(R.id.hole);
        brush=(ImageView)findViewById(R.id.brush);
        stat=(ImageView)findViewById(R.id.stat);
        stapler=(ImageView)findViewById(R.id.stapler);
        spiral=(ImageView)findViewById(R.id.spiral);
        magazine=(ImageView)findViewById(R.id.magazine);
        glue=(ImageView)findViewById(R.id.glue);
        pencil=(ImageView)findViewById(R.id.drafter);


        spiral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","spiral");
                startActivity(i);
            }
        });
        hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","hole");
                startActivity(i);
            }
        });
        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","brush");
                startActivity(i);

            }
        });
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","drafter");
                startActivity(i);

            }
        });
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","stat");
                startActivity(i);

            }
        });
        stapler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","stapler");
                startActivity(i);

            }
        });
        glue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","glue");
                startActivity(i);

            }
        });
        magazine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","magazine");
                startActivity(i);

            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategory.this,AdminActivity.class);
                i.putExtra("category","books");
                startActivity(i);
            }
        });
    }
}

