package com1032.cw2.sk00763.improov;

import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewReview extends Activity implements ReviewListAdapter.ReviewActionListener{
    private TextView noreviews = null;
    private RecyclerView list = null;
    private ImageView back = null;
    private ReviewListAdapter m_adapter = null;
    private List<Review> reviews = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        noreviews = findViewById(R.id.noreviews);
        list = findViewById(R.id.reviewlist);
        reviews = new ArrayList<Review>();
        m_auth = FirebaseAuth.getInstance();
        back = findViewById(R.id.backfromviewreview);
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        list.setHasFixedSize(true);
        list.addItemDecoration(new DividerItemDecoration(ViewReview.this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(ViewReview.this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        populateView();
    }

    public void populateView(){
        list.removeAllViews();
        reviews.clear();
        String id = getIntent().getStringExtra("from");
        m_ref.child("user").child(id).child("review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Review review = ds.getValue(Review.class);
                    reviews.add(review);
                }
                m_adapter = new ReviewListAdapter(ViewReview.this, reviews, ViewReview.this);
                list.setAdapter(m_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onReviewClick(int position) {

    }
}
