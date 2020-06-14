package com.Konnect.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Konnect.App.Adapter.CommentAdapter;
import com.Konnect.App.Model.Comment;
import com.Konnect.App.Model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment>   commentList;
    EditText addComment;
    TextView postComment;
    ImageView image_profile;

    String postid;
    String publisherid;


    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_comments );

        Toolbar toolbar=findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "Comments" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        recyclerView=findViewById( R.id.recycler_view );
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( this );
        recyclerView.setLayoutManager( linearLayoutManager );
        commentList=new ArrayList<>(  );
        commentAdapter=new CommentAdapter( this,commentList );
        recyclerView.setAdapter( commentAdapter );
            addComment=findViewById( R.id.add_comment );
            image_profile=findViewById( R.id.image_profile );
            postComment=findViewById( R.id.post );
    firebaseUser= FirebaseAuth.getInstance(  ).getCurrentUser();
        Intent intent=getIntent();
        postid=intent.getStringExtra( "postid" );
        publisherid=intent.getStringExtra( "publisherid" );
        postComment.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals( "" ))
                {
                    Toast.makeText( CommentsActivity.this, "You Can't Post Empty Comment", Toast.LENGTH_SHORT ).show();
                }else{
                    addComment();
                }
            }
        } );
        getImage();
        readComments();

    }

    private void addComment() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Comments").child( postid );
        HashMap<String,Object> hashMap=new HashMap<>(  );
        hashMap.put("comment",addComment.getText().toString());
        hashMap.put( "publisher",firebaseUser.getUid() );

        ref.push().setValue( hashMap );
        addNotification();
        addComment.setText( "" );
    }

    private  void addNotification()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference( "Notifications" ).child( publisherid );

        HashMap<String,Object> hashMap =new HashMap<>(  );
        FirebaseUser firebaseUser=FirebaseAuth.getInstance( ).getCurrentUser() ;
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put( "text","Commented"+addComment.getText().toString() );
        hashMap.put( "postid",postid );
        hashMap.put( "ispost",true );
        ref.push().setValue( hashMap );
    }
    private void getImage()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference( "Users" ).child( firebaseUser.getUid() );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with( getApplicationContext()).load(user.getImageUrl()).into( image_profile );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private void readComments(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Comments").child( postid );


        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Comment comment=snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
