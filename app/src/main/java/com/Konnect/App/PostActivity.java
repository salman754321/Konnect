package com.Konnect.App;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    StorageTask Uploadtaskk;
    StorageReference storageReference;
    String myUri="";


    ImageView close,Addimage;
    TextView post;
    EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_post );

        close=findViewById( R.id.close );
        Addimage=findViewById( R.id.image_added );
        post=findViewById( R.id.post );
        description=findViewById( R.id.description );

        storageReference= FirebaseStorage.getInstance().getReference("Posts");

        close.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( PostActivity.this,MainActivity.class ) );
                finish();
            }
        } );


        post.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        } );
        CropImage.activity().setAspectRatio( 1,1 ).
                         start( PostActivity.this );
    }
private void uploadImage(){
    final ProgressDialog pd=new ProgressDialog( this );
    pd.setMessage( "posting" );
    pd.show();

    if(imageUri!=null)
    {
        final StorageReference fileReference=storageReference.child( System.currentTimeMillis() +"."+getFileExtension( imageUri ));
        Uploadtaskk=fileReference.putFile( imageUri );

        Uploadtaskk.continueWithTask( new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri DownloadUri=task.getResult();
                    myUri=DownloadUri.toString();


                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");
                    String postId=reference.push().getKey();
                    HashMap<String,Object> hashMap=new HashMap<>(  );
                    hashMap.put("postId",postId);
                    hashMap.put("PostImage",myUri);
                    hashMap.put("Description",description.getText().toString());
                    hashMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid() );
                    reference.child( postId ).setValue( hashMap );

                    pd.dismiss();
                    startActivity( new Intent( PostActivity.this,MainActivity.class ) );
                    finish();
                }else{
                    Toast.makeText(PostActivity.this,"Operation Failed ",Toast.LENGTH_LONG ).show();
                }
            }
        } );

    }else{
        Toast.makeText( PostActivity.this,"No Image Selected",Toast.LENGTH_LONG ).show();
    }
}

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap maime=MimeTypeMap.getSingleton();
        return maime.getExtensionFromMimeType( contentResolver.getType( uri ) );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult( data );
            imageUri=result.getUri();
            Addimage.setImageURI( imageUri );
        }else {
            Toast.makeText( PostActivity.this, "Somethings Going Wrong", Toast.LENGTH_SHORT ).show();
            startActivity( new Intent( PostActivity.this,MainActivity.class ) );
            finish();
        }
    }
}
