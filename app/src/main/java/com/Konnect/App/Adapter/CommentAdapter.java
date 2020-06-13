package com.Konnect.App.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Konnect.App.MainActivity;
import com.Konnect.App.Model.Comment;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context mContext;
    private List<Comment> mComments;

    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }
    FirebaseUser firebaseUser;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( mContext ).inflate( R.layout.comment_item ,parent,false);
        return new CommentAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    final Comment comment=mComments.get( position );

    holder.comment.setText( comment.getComment() );
    getUserInfo( holder.image_profile,holder.username,comment.getPublisher() );

    holder.comment.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent( mContext, MainActivity.class );
            intent.putExtra( "publisherid",comment.getPublisher() );
            mContext.startActivity( intent );
        }
    } );
        holder.image_profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( mContext, MainActivity.class );
                intent.putExtra( "publisherid",comment.getPublisher() );
                mContext.startActivity( intent );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile;
        public TextView username,comment;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            image_profile=itemView.findViewById( R.id.image_profile );
            username=itemView.findViewById( R.id.username );
            comment=itemView.findViewById( R.id.comment );
        }
    }

    private  void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child( "Users" ).child( publisherid );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with( mContext ).load( user.getImageUrl() ).into( imageView );
                username.setText( user.getUserName() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
