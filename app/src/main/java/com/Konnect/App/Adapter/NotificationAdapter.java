package com.Konnect.App.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.Konnect.App.Fragment.PostDetailFragment;
import com.Konnect.App.Fragment.ProfileFragment;
import com.Konnect.App.Model.Notification;
import com.Konnect.App.Model.Post;
import com.Konnect.App.Model.User;
import com.Konnect.App.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context mContext;
    List<Notification> notificationList;

    public NotificationAdapter(Context mContext, List<Notification> notificationList) {
        this.mContext = mContext;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from( mContext ).inflate( R.layout.notification_item,parent,false );
      return new NotificationAdapter.ViewHolder(view  );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification notification=notificationList.get( position );

        holder.comment.setText( notification.getText() );
        getUserinfo( holder.profile_image,holder.username,notification.getUserid() );

        if(notification.isPost()){
            holder.post_image.setVisibility( View.VISIBLE );
            getPost( holder.post_image,notification.getPostid() );
        }else {
            holder.post_image.setVisibility( View.GONE );
        }

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.isPost())
                {
                    SharedPreferences.Editor editor=mContext.getSharedPreferences( "PREPS",Context.MODE_PRIVATE ).edit();
                    editor.putString( "postid",notification.getPostid() );
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new PostDetailFragment() ).commit();
                }else{
                    SharedPreferences.Editor editor=mContext.getSharedPreferences( "PREPS",Context.MODE_PRIVATE ).edit();
                    editor.putString( "profileid",notification.getUserid() );
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new ProfileFragment() ).commit();
                }
            }
        } );
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView post_image,profile_image;
        public final TextView username,comment;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );


           post_image=itemView.findViewById( R.id.post_image );
           profile_image=itemView.findViewById( R.id.image_profile );
           username=itemView.findViewById( R.id.username );
           comment=itemView.findViewById( R.id.comment );

        }
    }

    private void getUserinfo(final ImageView imageView, final TextView textView, String publisher){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference( "Users" ).child( publisher );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with( mContext ).load( user.getImageUrl() ).into( imageView );
                textView.setText( user.getUserName() );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    private  void getPost(final ImageView imageView, String postid)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference( "Posts" ).child( postid );

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post=dataSnapshot.getValue(Post.class);
                Glide.with( mContext ).load( post.getPostImage() ).into( imageView );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
