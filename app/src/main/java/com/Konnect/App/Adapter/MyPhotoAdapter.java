package com.Konnect.App.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.Konnect.App.Fragment.PostDetailFragment;
import com.Konnect.App.Model.Post;
import com.Konnect.App.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.ViewHolder>{


    private Context context;
    private List<Post> postList;

    public MyPhotoAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( context ).inflate( R.layout.photos_item,parent,false );
        return new MyPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Post post=postList.get( position );
        Glide.with( context ).load( post.getPostImage() ).into( holder.post_image );
        holder.post_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=context.getSharedPreferences( "PREPS",Context.MODE_PRIVATE ).edit();
                editor.putString( "postid",post.getPostId() );
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new PostDetailFragment() ).commit();

            }
        } );
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView post_image;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            post_image=itemView.findViewById( R.id.post_image );

        }
    }
}
