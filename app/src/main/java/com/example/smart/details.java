package com.example.smart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class details extends AppCompatActivity {
    EditText name,blood,vhm,ec;
    Button submit;
    DatabaseReference databaselocation;
   // public static String e;
    private String file="mydata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name=(EditText)findViewById(R.id.editname);
        blood=(EditText)findViewById(R.id.editbg);
        ec=(EditText)findViewById(R.id.editnum);
        vhm=(EditText)findViewById(R.id.editvehnum);
        submit=(Button)findViewById(R.id.btnSubmit);
        databaselocation = FirebaseDatabase.getInstance().getReference("location");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });


    }

    private void upload() {
        String id=databaselocation.push().getKey();
        String n=name.getText().toString();
        String b=blood.getText().toString();
        String e=ec.getText().toString();
        String v=vhm.getText().toString();


//        locationdatabase locationdatabase=new locationdatabase(id,n,b,e,v);
//        databaselocation.child(id).setValue(locationdatabase);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child("Name").setValue(n);
        myRef.child("Blood_Group").setValue(b);
        myRef.child("Emergency_Contact").setValue(e);
        myRef.child("Vehicle_Number").setValue(v);

        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);


        Toast.makeText(this,e,Toast.LENGTH_SHORT).show();
    }

}
