package com.Konnect.App.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;
import java.util.zip.Inflater;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mcontext;
    public List<Post> mPost;

    public PostAdapter(Context mcontext, List<Post> mPost) {
        this.mcontext = mcontext;
        this.mPost = mPost;
    }

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    public List<Post> getmPost() {
        return mPost;
    }

    public void setmPost(List<Post> mPost) {
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from( mcontext ).inflate( R.layout.post_item,parent,false);
       return new PostAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post=mPost.get( position );

        Glide.with( mcontext ).load( post.getPostImage() ).into( holder.post_image );
        if(post.getDescription().equals( "" ))
        {
            holder.description.setVisibility( View.GONE );

        }else{
            holder.description.setVisibility( View.VISIBLE );
            holder.description.setText( post.getDescription() );
        }

        publisherInfo( holder.image_profile,holder.username,holder.publisher,post.getPublisher() );
        isLiked( post.getPostId(),holder.like );
        nrlikes( holder.likes,post.getPostId() );


        holder.like.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals( "like" )){
                    FirebaseDatabase.getInstance().getReference().child( "Likes" ).child( post.getPostId() ).child( firebaseUser.getUid() ).setValue( true );

                }else{
                    FirebaseDatabase.getInstance().getReference().child( "Likes" ).child( post.getPostId() ).child( firebaseUser.getUid() ).removeValue();
                }
            }
        } );
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView image_profile,post_image,like,comment,save;
        public TextView username,likes,comments,description,publisher;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            image_profile=itemView.findViewById( R.id.image_profile );
            post_image=itemView.findViewById( R.id.post_image );
            like=itemView.findViewById( R.id.like );
            comment=itemView.findViewById( R.id.commentss );
            save=itemView.findViewById( R.id.savepost );

            username=itemView.findViewById( R.id.username );
            likes=itemView.findViewById( R.id.likes );
            comments=itemView.findViewById( R.id.comments );
            description=itemView.findViewById( R.id.description );
            publisher=itemView.findViewById( R.id.publisher  );
        }
    }

    private void isLiked(String postId, final ImageView like_image) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Likes" ).child( postId );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child( firebaseUser.getUid() ).exists()) {
                    like_image.setImageResource( R.drawable.ic_liked );
                    like_image.setTag( "liked" );

                } else {
                    like_image.setImageResource( R.drawable.ic_like );
                    like_image.setTag( "like" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );
    }
    private void nrlikes(final TextView like, String postid){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child( "Likes" ).child( postid );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                like.setText( dataSnapshot.getChildrenCount()+" Likes" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, String userid) {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Users" ).child( userid );
    ref.addValueEventListener( new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue( User.class );
            Glide.with( mcontext ).load( user.getImageUrl() ).into( image_profile );
            username.setText( user.getUserName() );
            publisher.setText( user.getUserName() );
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    } );
}
}