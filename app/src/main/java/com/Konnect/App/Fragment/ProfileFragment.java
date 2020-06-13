package com.Konnect.App.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.Konnect.App.Model.Post;
import com.Konnect.App.Model.User;
import com.Konnect.App.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    ImageView image_profile,option;
    TextView post,followers,following,bio,username,fullname;
    Button edit_profile;
    String profileid;
    FirebaseUser firebaseUser;
    ImageButton my_photos,saved_photos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate( R.layout.fragment_profile, container, false );
        firebaseUser= FirebaseAuth.getInstance( ).getCurrentUser();
        SharedPreferences prefs=getContext().getSharedPreferences( "PREPS", Context.MODE_PRIVATE );
        profileid=prefs.getString( "profileid","none" );
        image_profile=view.findViewById( R.id.image_profile );
        option=view.findViewById( R.id.option );
        post=view.findViewById( R.id.posts );
        followers=view.findViewById( R.id.followers );
        following=view.findViewById( R.id.following );
        bio=view.findViewById( R.id.bio );
        username=view.findViewById( R.id.username );
        fullname=view.findViewById( R.id.full_name );
        edit_profile=view.findViewById( R.id.edit_profile );
        my_photos=view.findViewById( R.id.my_photos );
        saved_photos=view.findViewById( R.id.saved_photos );
        userInfo();
        getFollowers();
        getNrPosts();
        if(profileid.equals( firebaseUser.getUid() ))
        {
            edit_profile.setText( "Edit Profile");
        }else{
            checkFollow();
            saved_photos.setVisibility( View.GONE );
        }


        edit_profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_profile.getText().toString().equals( "Edit Profile" )){
                    //
                }else  if(edit_profile.getText().toString().equals( "follow" )){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( firebaseUser.getUid() ).child("following").child( profileid).setValue( true );
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( profileid ).child("followers").child( firebaseUser.getUid() ).setValue( true );
                }else if(edit_profile.getText().toString().equals( "following" )){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( firebaseUser.getUid() ).child("following").child( profileid ).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( profileid).child("followers").child( firebaseUser.getUid() ).removeValue();
                }
            }
        } );
        return view;
    }

    private void userInfo()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child( profileid );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext()==null)
                {
                    return ;
                }
                User user=dataSnapshot.getValue(User.class);
                Glide.with( getContext() ).load( user.getImageUrl() ).load( image_profile );
                username.setText( user.getUserName() );
                bio.setText( user.getBio() );
                fullname.setText( user.getFullName() );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private  void checkFollow(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child( "Follow" ).child( firebaseUser.getUid() ).child( "following" );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child( profileid ).exists()) {
                    edit_profile.setText( "following" );
                }else{
                    edit_profile.setText( "follow" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private void getFollowers(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child( "Follow" ).child( profileid ).child( "followers" );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText( ""+dataSnapshot.getChildrenCount() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child( "Follow" ).child( profileid ).child( "following" );

        ref2.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText( ""+dataSnapshot.getChildrenCount() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void getNrPosts(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Post post=snapshot.getValue( Post.class );
                    if(post.getPublisher().equals( profileid ))
                    {
                        i++;
                    }
                }
                post.setText( ""+i );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
