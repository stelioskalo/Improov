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

public class DiscussionListAdapter extends RecyclerView.Adapter<DiscussionListAdapter.SearchViewHolder> {

    //context object
    private Context m_context = null;
    //list of events
    private List<Discussion> m_discussions = null;
    //custom click listener for events
    private DiscussionActionListener m_discussionListener = null;

    /**
     * Constructor for adapter
     * @param context: context of app
     * @param discussions: list of discussions
     * @param eventActionListener: custom  listener for our discussions list
     */
    public DiscussionListAdapter(Context context, List<Discussion> discussions, DiscussionActionListener eventActionListener) {
        super();
        this.m_context = context;
        this.m_discussions = discussions;
        this.m_discussionListener = eventActionListener;
    }

    @Override
    public DiscussionListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.item_layout, parent, false);
        return new DiscussionListAdapter.SearchViewHolder(view, m_discussionListener);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        //set the event details
        holder.m_about.setText(m_discussions.get(position).getAbout());
        //get the eventId of the event
        final String id = m_discussions.get(position).getCreatorid();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("user").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.m_name.setText(dataSnapshot.child("name").getValue().toString());
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
     * Method to return number of discussions
     * @return: number of discussions
     */
    @Override
    public int getItemCount() {
        return m_discussions.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //event name
        private TextView m_name = null;
        //event category
        private TextView m_about = null;
        //event image
        private ImageView m_image = null;
        //on event click listener
        private DiscussionActionListener m_listener = null;

        private SearchViewHolder(View itemView, DiscussionActionListener discussionActionListener) {
            super(itemView);

            //inflate widgets
            m_name = (TextView) itemView.findViewById(R.id.itemname);
            m_about = (TextView) itemView.findViewById(R.id.postAbout);
            m_image = (ImageView) itemView.findViewById(R.id.discussionpic);

            m_listener = discussionActionListener;

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onDiscussionClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface DiscussionActionListener{
        /** on event click */
        void onDiscussionClick(int position);
    }
}
