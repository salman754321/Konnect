package com.Konnect.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    EditText Email,Password;
    Button Login;
    TextView txt_Signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );



        Email=findViewById( R.id.email );

        Password=findViewById( R.id.password );
        Login=findViewById( R.id.login );
        txt_Signup=findViewById( R.id.txt_signup );


        auth=FirebaseAuth.getInstance();
        txt_Signup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( LoginActivity.this,RegisterActivity.class ) );
            }
        } );

        Login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(LoginActivity.this);
                pd.setMessage( "Please Wait" );
                pd.show();


                String Eemail=Email.getText().toString();
                String Pass=Password.getText().toString();

                if(TextUtils.isEmpty( Eemail )||TextUtils.isEmpty( Pass ))
                {
                    Toast.makeText( LoginActivity.this,"Please Fill All The Field" ,Toast.LENGTH_LONG).show();
                }else{

                    auth.signInWithEmailAndPassword( Eemail,Pass ).
                            addOnCompleteListener( LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child( "Users" ).
                                                child( auth.getCurrentUser().getUid() );

                                        ref.addValueEventListener( new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pd.dismiss();
                                                Intent intent=new Intent( LoginActivity.this,MainActivity.class );
                                                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                                startActivity( intent );
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pd.dismiss();
                                            }
                                        } );
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText( LoginActivity.this,"Authentication Failed ",Toast.LENGTH_LONG ).show();
                                    }
                                }
                            } );
                }

            }
        } );



    }
}
