package com.sudhanshujaisani.mywhatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
EditText editTextName,editTextEmail,editTextPass;
FirebaseAuth firebaseAuth;
ProgressDialog progressDialog;
FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=(Toolbar)findViewById(R.id.regToolbar);
        setSupportActionBar(toolbar);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPass=(EditText)findViewById(R.id.editTextPass);
        Button button=(Button)findViewById(R.id.regButton);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Please wait while we sign you up..");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final String name=editTextName.getText().toString();
                String email=editTextEmail.getText().toString();
                String pass=editTextPass.getText().toString();
                Task task=firebaseAuth.createUserWithEmailAndPassword(email,pass);
                task.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task1) {
                        if(task1.isSuccessful()){
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String uid=firebaseUser.getUid();
                            firebaseDatabase=FirebaseDatabase.getInstance();
                            //creating new entry with pk as uid generated by firebaseAuth
                            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Users").child(uid);
                            HashMap<String,String>hashMap=new HashMap<>();
                            hashMap.put("name",name);
                            hashMap.put("status","Hi there! I'm using My Whatsapp!!");
                            hashMap.put("image","noImage");
                            hashMap.put("thumb","noThumb");
                            databaseReference.setValue(hashMap);

                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{progressDialog.hide();

                            Toast.makeText(RegisterActivity.this, "Please check the entries and try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
