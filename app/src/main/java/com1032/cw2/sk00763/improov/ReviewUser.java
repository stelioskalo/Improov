package com1032.cw2.sk00763.improov;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class ReviewUser extends Activity {
    private ImageView star1 = null;
    private ImageView star2 = null;
    private ImageView star3 = null;
    private ImageView star4 = null;
    private ImageView star5 = null;
    private TextView starNo = null;
    private EditText message = null;
    private TextView submit = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        star1 = findViewById(R.id.reviewstar1);
        star2 = findViewById(R.id.reviewstar2);
        star3 = findViewById(R.id.reviewstar3);
        star4 = findViewById(R.id.reviewstar4);
        star5 = findViewById(R.id.reviewstar5);
        message = findViewById(R.id.reviewmessage);
        starNo = findViewById(R.id.noofstars);
        submit = findViewById(R.id.submitreview);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_white);
                star3.setImageResource(R.drawable.star_white);
                star4.setImageResource(R.drawable.star_white);
                star5.setImageResource(R.drawable.star_white);
                starNo.setText("1");
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_white);
                star4.setImageResource(R.drawable.star_white);
                star5.setImageResource(R.drawable.star_white);
                starNo.setText("2");
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_yellow);
                star4.setImageResource(R.drawable.star_white);
                star5.setImageResource(R.drawable.star_white);
                starNo.setText("3");
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_yellow);
                star4.setImageResource(R.drawable.star_yellow);
                star5.setImageResource(R.drawable.star_white);
                starNo.setText("4");
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star_yellow);
                star2.setImageResource(R.drawable.star_yellow);
                star3.setImageResource(R.drawable.star_yellow);
                star4.setImageResource(R.drawable.star_yellow);
                star5.setImageResource(R.drawable.star_yellow);
                starNo.setText("5");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewUser();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));
    }

    public void reviewUser(){
        if(!starNo.getText().toString().matches("0") || !message.getText().toString().matches("")){
            String reviewId = RandomNumber.generateUID();
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("review").child(reviewId).child("from").setValue(m_user.getUid());
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("review").child(reviewId).child("message").setValue(message.getText().toString());
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("review").child(reviewId).child("stars").setValue(starNo.getText().toString());
            finish();
            Toasty.success(ReviewUser.this, "Review Submitted!", Toast.LENGTH_LONG).show();
        }
        else {
            Toasty.error(ReviewUser.this, "Please fill all fields.", Toast.LENGTH_LONG).show();
        }
    }

}
