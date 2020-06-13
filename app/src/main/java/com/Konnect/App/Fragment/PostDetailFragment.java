package com.Konnect.App.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Konnect.App.Adapter.PostAdapter;
import com.Konnect.App.Model.Post;
import com.Konnect.App.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {
    String postid;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate( R.layout.fragment_post_detail, container, false );
        SharedPreferences Preferences=getContext().getSharedPreferences( "PREPS" , Context.MODE_PRIVATE );
        postid=Preferences.getString( "postid","none" );
        recyclerView=view.findViewById( R.id.recycler_view );
      recyclerView.setHasFixedSize( true );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( linearLayoutManager );
        postList=new ArrayList<>(  );
        postAdapter=new PostAdapter( getContext(),postList );
        recyclerView.setAdapter( postAdapter );
        readPost();

        return view;
    }

    private void readPost() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts").child( postid );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                Post post=dataSnapshot.getValue(Post.class);
                postList.add( post );

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
