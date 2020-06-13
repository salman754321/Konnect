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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    EditText username,Email,FullName,Password;
    Button register;
    TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        username=findViewById( R.id.username );
        Email=findViewById( R.id.email );
        FullName=findViewById( R.id.fullname );
        Password=findViewById( R.id.password );
        register=findViewById( R.id.register );
        txt_login=findViewById( R.id.txt_login );


        auth=FirebaseAuth.getInstance();

        txt_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( RegisterActivity.this,LoginActivity.class ) );
            }
        } );

        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(RegisterActivity.this  );
                pd.setMessage( "Please Wait..." );
                pd.show();



                String uname= username.getText().toString();
                String Fname=FullName.getText().toString();
                String email=Email.getText().toString();
                String pass=Password.getText().toString();

                if(TextUtils.isEmpty( uname )|| TextUtils.isEmpty( Fname )||TextUtils.isEmpty( email )|| TextUtils.isEmpty( pass )){
                    Toast.makeText( RegisterActivity.this,"Fill All Fields",Toast.LENGTH_LONG ).show();
                }else if(pass.length()<6)
                {
                    Toast.makeText( RegisterActivity.this, "Password Must Contain 6 Or More Characters",Toast.LENGTH_LONG).show();
                }else{
                    Register(uname,pass,Fname,email);
                }

            }
        } );



    }
    private void Register(final String aasername , String paass , final String Fullname, String email ){
        auth.createUserWithEmailAndPassword( email,paass ).addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseuser=auth.getCurrentUser();
                    String userId=firebaseuser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference().child( "Users" ).child(userId);
                    HashMap<String,Object> hashMap=new HashMap<>(  );
                    hashMap.put( "UserId",userId );
                    hashMap.put( "UserName",aasername.toLowerCase() );
                    hashMap.put("FullName",Fullname);
                    hashMap.put("Bio","");
                    hashMap.put( "ImageUrl","https://firebasestorage.googleapis.com/v0/b/konnect-9b405.appspot.com/o/30-307416_profile-icon-png-image-free-download-searchpng-employee.png?alt=media&token=3556bce8-2238-4e20-be26-55860dd8a654" )
                    ;
                    reference.setValue( hashMap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                pd.dismiss();
                                Intent intent=new Intent( RegisterActivity.this,MainActivity.class );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    } );


                }
                else {
                    pd.dismiss();
                    Toast.makeText( RegisterActivity.this,"You Can't Register With This Email Or UserName Or PassWord",Toast.LENGTH_LONG ).show();
                }
            }
        } );
    }
}
