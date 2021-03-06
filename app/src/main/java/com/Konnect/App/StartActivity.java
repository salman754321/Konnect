package com.Konnect.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
Button Login,Register;
FirebaseUser firebaseUser;
protected void onStart(){
    super.onStart();
    firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    if(firebaseUser!=null){
        startActivity( new Intent( StartActivity.this,MainActivity.class ) );
        finish();
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_start );


        Login=findViewById( R.id.login );
        Register=findViewById( R.id.register );

        Login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( StartActivity.this,LoginActivity.class ) );
            }
        } );
        Register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( StartActivity.this,RegisterActivity.class ) );
            }
        } );
    }
}
