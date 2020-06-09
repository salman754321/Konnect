package com.Konnect.App.Fragment;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.Konnect.App.Adapter.UserAdapter;
import com.Konnect.App.Model.User;
import com.Konnect.App.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    EditText searchbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view =inflater.inflate( R.layout.fragment_search, container, false );
      recyclerView=view.findViewById( R.id.recycler_view );
      recyclerView.setHasFixedSize( true );
      recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );

      searchbar=view.findViewById( R.id.searchbar );
      userList=new ArrayList<>(  );
      userAdapter=new UserAdapter( getContext(), userList );
      recyclerView.setAdapter( userAdapter );
    readUsers();
   searchbar.addTextChangedListener( new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchUser( s.toString().toLowerCase() );
       }

       @Override
       public void afterTextChanged(Editable s) {

       }
   } );


        return view;
    }


    private void searchUser(String s)
    {
        Query queery= FirebaseDatabase.getInstance().getReference("Users").orderByChild( "username" ).startAt( s ).endAt( s+"\uf8ff" );

        queery.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    userList.add( user );
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private void readUsers(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(searchbar.getText().toString().equals( "" )){

                    userList.clear();

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        User user=snapshot.getValue(User.class);
                        userList.add( user );
                    }
                    userAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
