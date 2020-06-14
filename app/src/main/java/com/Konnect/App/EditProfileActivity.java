package com.Konnect.App;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Konnect.App.Model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    ImageView closw,profile_image;
    TextView save,tc_change;
    MaterialEditText fulname,usrname,bio;

    FirebaseUser firebaseUser;

    private Uri imageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_profile );
        closw=findViewById( R.id.close );
        profile_image=findViewById( R.id.image_profile );
        save=findViewById( R.id.save );
        tc_change=findViewById( R.id.tv_change );
        fulname=findViewById( R.id.fullname );
        usrname=findViewById( R.id.username );
        bio=findViewById( R.id.bio );

        firebaseUser= FirebaseAuth.getInstance( ).getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("Uploads");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );

        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                fulname.setText( user.getFullName() );
                usrname.setText( user.getUserName() );
                bio.setText( user.getBio() );
                Glide.with( getApplicationContext() ).load( user.getImageUrl() ).into( profile_image );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );

        closw.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        tc_change.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio( 1,1 ).
                setCropShape( CropImageView.CropShape.OVAL ).start( EditProfileActivity.this );
            }
        } );
        profile_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio( 1,1 ).
                        setCropShape( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL).start( EditProfileActivity.this );
            }
        } );

        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(fulname.getText().toString(),usrname.getText().toString(),bio.getText().toString());
                finish();
            }


        } );


    }
    private void updateProfile(String toString, String toString1, String toString2) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );
        HashMap<String ,Object> hashMap=new HashMap<>(  );
        hashMap.put( "FullName",toString );
        hashMap.put("UserName",toString1);
        hashMap.put( "Bio",toString2 );

        reference.updateChildren( hashMap );

    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType( contentResolver.getType( uri ) );
    }

    private void uploadImage(){
       final ProgressDialog pd=new ProgressDialog( this);
        pd.setMessage( "Uploading" );
        pd.show();

        if(imageUri!=null)
        {
            final StorageReference filReference=storageReference.child( System.currentTimeMillis()+"."+getExtension( imageUri ) );
            uploadTask=filReference.putFile( imageUri );
            uploadTask.continueWithTask( new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filReference.getDownloadUrl();
                }
            } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String myuri=downloadUri.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference( "Users" ).child( firebaseUser.getUid() );
                        HashMap<String,Object> hashMap=new HashMap<>(  );
                        hashMap.put( "ImageUrl",myuri );
                        ref.updateChildren( hashMap );
                        pd.dismiss();
                    }else {
                        Toast.makeText( EditProfileActivity.this,"Operation Failed",Toast.LENGTH_LONG ).show();
                    }
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( EditProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG ).show();
                }
            } );
        }else {
            Toast.makeText( EditProfileActivity.this,"No Image Selected",Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );


        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult( data );
            imageUri=result.getUri();
            uploadImage();
        }else {
            Toast.makeText( EditProfileActivity.this,"SomeThing Went Wrong",Toast.LENGTH_LONG ).show();
        }
    }
}
