package com.Konnect.App.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.Konnect.App.Fragment.ProfileFragment;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from( context ).inflate( R.layout.user_item,parent,false );
       return new UserAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final User user=userList.get( position );

        holder.btn_follow.setVisibility( View.VISIBLE );
        holder.username.setText( user.getUserName() );
        holder.Fullname.setText( user.getFullName() );
        Glide.with(context).load(user.getImageUrl()).into( holder.imageprofile );
        isFollowing(user.getUserId(),holder.btn_follow );

        if(user.getUserId().equals( firebaseUser.getUid() )){
            holder.btn_follow.setVisibility( View.GONE );
        }


        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences( "PREPS", Context.MODE_PRIVATE ).edit();
                editor.putString( "profileid", user.getUserId() );
                editor.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new ProfileFragment() ).commit();
            }
        } );

        holder.btn_follow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_follow.getText().toString().equals( "follow" )){
                     FirebaseDatabase.getInstance().getReference().child("Follow").child( firebaseUser.getUid() ).child("following").child( user.getUserId() ).setValue( true );
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( user.getUserId() ).child("followers").child( firebaseUser.getUid() ).setValue( true );
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( firebaseUser.getUid() ).child("following").child( user.getUserId() ).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child( user.getUserId() ).child("followers").child( firebaseUser.getUid() ).removeValue();
                }
            }
        } );
    }

    @Override
    public int getItemCount() {
      return  userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username,Fullname;
        public CircleImageView imageprofile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            username=itemView.findViewById( R.id.username );
            Fullname=itemView.findViewById( R.id.fullname );
            imageprofile=itemView.findViewById( R.id.image_profile );
            btn_follow=itemView.findViewById( R.id.btn_follow );
        }
    }

    private  void isFollowing(final String userId, final Button flw_btn){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow").child( firebaseUser.getUid() ).child( "following" );
        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child( userId ).exists()){
                    flw_btn.setText( "following" );
                }else{
                    flw_btn.setText( "follow" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
