package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by Stelios on 13/12/2019.
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.SearchViewHolder> {

    //context object
    private Context m_context = null;
    //list of events
    private List<Post> m_posts = null;
    //custom click listener for events
    private PostListAdapter.PostActionListener m_postListener = null;

    /**
     * Constructor for adapter
     * @param context: context of app
     * @param posts: list of events
     * @param eventActionListener: custom  listener for our events list
     */
    public PostListAdapter(Context context, List<Post> posts, PostListAdapter.PostActionListener eventActionListener) {
        super();
        this.m_context = context;
        this.m_posts = posts;
        this.m_postListener = eventActionListener;
    }

    @Override
    public PostListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.post_layout, parent, false);
        return new PostListAdapter.SearchViewHolder(view, m_postListener);
    }

    @Override
    public void onBindViewHolder(final PostListAdapter.SearchViewHolder holder, int position) {
        //set the event details
        holder.m_post.setText(m_posts.get(position).getPost());

        //get the eventId of the event
        final String userId = m_posts.get(position).getPostuser();


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("user").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.m_user.setText(dataSnapshot.child("name").getValue().toString());
                String image = dataSnapshot.child("image").getValue().toString();
                try {
                    holder.m_image.setImageBitmap(decodeFromFirebaseBase64(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
    /**
     * Method to return number of events
     * @return: number of events
     */
    @Override
    public int getItemCount() {
        return m_posts.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //event name
        private TextView m_post = null;
        //event category
        private TextView m_user = null;
        //event image
        private ImageView m_image = null;
        //on event click listener
        private PostListAdapter.PostActionListener m_listener = null;

        private SearchViewHolder(View itemView, PostListAdapter.PostActionListener postActionListener) {
            super(itemView);

            //inflate widgets
            m_user = (TextView) itemView.findViewById(R.id.username);
            m_post = (TextView) itemView.findViewById(R.id.postcontent);
            m_image = (ImageView) itemView.findViewById(R.id.userpic);

            m_listener = postActionListener;

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onPostClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface PostActionListener{
        /** on event click */
        void onPostClick(int position);
    }

}
