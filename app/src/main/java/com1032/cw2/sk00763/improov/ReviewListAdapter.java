package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
 * Created by Stelios on 10/03/2020.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.SearchViewHolder> {

    //context object
    private Context m_context = null;
    //list of events
    private List<Review> m_reviews = null;
    //custom click listener for events
    private ReviewListAdapter.ReviewActionListener m_reviewListener = null;

    /**
     * Constructor for adapter
     * @param context: context of app
     * @param reviews: list of discussions
     * @param eventActionListener: custom  listener for our discussions list
     */
    public ReviewListAdapter(Context context, List<Review> reviews, ReviewListAdapter.ReviewActionListener eventActionListener) {
        super();
        this.m_context = context;
        this.m_reviews = reviews;
        this.m_reviewListener = eventActionListener;
    }

    @Override
    public ReviewListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.review_layout, parent, false);
        return new ReviewListAdapter.SearchViewHolder(view, m_reviewListener);
    }

    @Override
    public void onBindViewHolder(final ReviewListAdapter.SearchViewHolder holder, int position) {
        //set the event details
        holder.m_review.setText(m_reviews.get(position).getMessage());
        String id = m_reviews.get(position).getFrom();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("user").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                holder.m_name.setText(user.getName() + " " + user.getSurname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int stars = Integer.parseInt(m_reviews.get(position).getStars());


        if(stars == 1){
            holder.m_star1.setImageResource(R.drawable.star_yellow);
            holder.m_star2.setImageResource(R.drawable.star_white);
            holder.m_star3.setImageResource(R.drawable.star_white);
            holder.m_star4.setImageResource(R.drawable.star_white);
            holder.m_star5.setImageResource(R.drawable.star_white);
        }
        else if(stars == 2){
            holder.m_star1.setImageResource(R.drawable.star_yellow);
            holder.m_star2.setImageResource(R.drawable.star_yellow);
            holder.m_star3.setImageResource(R.drawable.star_white);
            holder.m_star4.setImageResource(R.drawable.star_white);
            holder.m_star5.setImageResource(R.drawable.star_white);
        }
        else if(stars == 3){
            holder.m_star1.setImageResource(R.drawable.star_yellow);
            holder.m_star2.setImageResource(R.drawable.star_yellow);
            holder.m_star3.setImageResource(R.drawable.star_yellow);
            holder.m_star4.setImageResource(R.drawable.star_white);
            holder.m_star5.setImageResource(R.drawable.star_white);
        }
        else if(stars == 4){
            holder.m_star1.setImageResource(R.drawable.star_yellow);
            holder.m_star2.setImageResource(R.drawable.star_yellow);
            holder.m_star3.setImageResource(R.drawable.star_yellow);
            holder.m_star4.setImageResource(R.drawable.star_yellow);
            holder.m_star5.setImageResource(R.drawable.star_white);
        }
        else {
            holder.m_star1.setImageResource(R.drawable.star_yellow);
            holder.m_star2.setImageResource(R.drawable.star_yellow);
            holder.m_star3.setImageResource(R.drawable.star_yellow);
            holder.m_star4.setImageResource(R.drawable.star_yellow);
            holder.m_star5.setImageResource(R.drawable.star_yellow);
        }

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
        return m_reviews.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView m_name = null;
        private TextView m_review = null;
        private ImageView m_star1 = null;
        private ImageView m_star2 = null;
        private ImageView m_star3 = null;
        private ImageView m_star4 = null;
        private ImageView m_star5 = null;
        private ReviewListAdapter.ReviewActionListener m_listener = null;

        private SearchViewHolder(View itemView, ReviewListAdapter.ReviewActionListener reviewActionListener) {
            super(itemView);

            //inflate widgets
            m_name = (TextView) itemView.findViewById(R.id.reviewername);
            m_review = (TextView) itemView.findViewById(R.id.review);
            m_star1 = (ImageView) itemView.findViewById(R.id.viewreviewstar1);
            m_star2 = (ImageView) itemView.findViewById(R.id.viewreviewstar2);
            m_star3 = (ImageView) itemView.findViewById(R.id.viewreviewstar3);
            m_star4 = (ImageView) itemView.findViewById(R.id.viewreviewstar4);
            m_star5 = (ImageView) itemView.findViewById(R.id.viewreviewstar5);

            m_listener = reviewActionListener;

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onReviewClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface ReviewActionListener{
        /** on event click */
        void onReviewClick(int position);
    }
}
