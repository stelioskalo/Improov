package com1032.cw2.sk00763.improov;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewProfile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextView back = null;
    private ImageView pic = null;
    private ImageView report = null;
    private ImageView popup = null;
    private TextView name = null;
    private TextView surname = null;
    private TextView about = null;
    private TextView qualifications = null;
    private ImageView topicpic1 = null;
    private ImageView topicpic2 = null;
    private ImageView topicpic3 = null;
    private ImageView topicpic4 = null;
    private ImageView topicpic5 = null;
    private TextView topic1 = null;
    private TextView topic2 = null;
    private TextView topic3 = null;
    private TextView topic4 = null;
    private TextView topic5 = null;
    private TextView postReview = null;
    private TextView reviewNo = null;
    private ImageView star1 = null;
    private ImageView star2 = null;
    private ImageView star3 = null;
    private ImageView star4 = null;
    private ImageView star5 = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        back = (TextView) findViewById(R.id.backfromviewprofile);
        pic = (ImageView) findViewById(R.id.viewprofilepic);
        name = (TextView) findViewById(R.id.viewprofname);
        surname = (TextView) findViewById(R.id.viewprofsurname);
        about = (TextView) findViewById(R.id.aboutme);
        popup = (ImageView) findViewById(R.id.popupmenu);
        report = (ImageView) findViewById(R.id.reportuser);
        qualifications = (TextView) findViewById(R.id.viewqualifications);
        topicpic1 = (ImageView) findViewById(R.id.viewtopicpic1);
        topicpic2 = (ImageView) findViewById(R.id.viewtopicpic2);
        topicpic3 = (ImageView) findViewById(R.id.viewtopicpic3);
        topicpic4 = (ImageView) findViewById(R.id.viewtopicpic4);
        topicpic5 = (ImageView) findViewById(R.id.viewtopicpic5);
        topic1 = (TextView) findViewById(R.id.viewtopic1);
        topic2 = (TextView) findViewById(R.id.viewtopic2);
        topic3 = (TextView) findViewById(R.id.viewtopic3);
        topic4 = (TextView) findViewById(R.id.viewtopic4);
        topic5 = (TextView) findViewById(R.id.viewtopic5);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        reviewNo = (TextView) findViewById(R.id.reviewno);
        postReview = (TextView) findViewById(R.id.postareview);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(!getIntent().getStringExtra("from").matches(m_user.getUid())){
            popup.setVisibility(View.GONE);
            report.setVisibility(View.VISIBLE);
            if(getIntent().getStringExtra("intent") != null){
                postReview.setVisibility(View.VISIBLE);
            }
        }

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewProfile.this, ReportUser.class);
                i.putExtra("from", getIntent().getStringExtra("from"));
                startActivity(i);
            }
        });

        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewProfile.this, ReviewUser.class);
                i.putExtra("from", getIntent().getStringExtra("from"));
                startActivity(i);
            }
        });

        reviewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewProfile.this, ViewReview.class);
                i.putExtra("from", getIntent().getStringExtra("from"));
                startActivity(i);
            }
        });

        populateViews();

    }

    public void populateViews() {
        final String userId = getIntent().getStringExtra("from");
        m_ref.child("user").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                topic1.setText(user.getTopic1());
                topic2.setText(user.getTopic2());
                topic3.setText(user.getTopic3());
                topic4.setText(user.getTopic4());
                topic5.setText(user.getTopic5());
                name.setText(user.getName());
                surname.setText(user.getSurname());
                String topicpicture1 = user.getTopicpic1();
                String topicpicture2 = user.getTopicpic2();
                String topicpicture3 = user.getTopicpic3();
                String topicpicture4 = user.getTopicpic4();
                String topicpicture5 = user.getTopicpic5();
                String profilepic = user.getImage();
                try {
                    topicpic1.setImageBitmap(decodeFromFirebaseBase64(topicpicture1));
                    topicpic2.setImageBitmap(decodeFromFirebaseBase64(topicpicture2));
                    topicpic3.setImageBitmap(decodeFromFirebaseBase64(topicpicture3));
                    topicpic4.setImageBitmap(decodeFromFirebaseBase64(topicpicture4));
                    topicpic5.setImageBitmap(decodeFromFirebaseBase64(topicpicture5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(dataSnapshot.hasChild("image")){
                    try {
                        pic.setImageBitmap(decodeFromFirebaseBase64(profilepic));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(dataSnapshot.hasChild("bio")){
                    about.setText(user.getBio());
                    about.setTextColor(Color.parseColor("#000000"));
                }
                if(dataSnapshot.hasChild("qualifications")){
                    qualifications.setText(user.getQualifications());
                    qualifications.setTextColor(Color.parseColor("#000000"));
                }

                if(dataSnapshot.hasChild("review")){
                    m_ref.child("user").child(userId).child("review").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int reviewNum = 0;
                            double accumulation = 0;
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Review review = ds.getValue(Review.class);
                                accumulation += Double.parseDouble(review.getStars());
                                reviewNum++;
                            }

                            setStars(accumulation / reviewNum);
                            reviewNo.setText("(" + String.valueOf(reviewNum) + " reviews)");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void popup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i = new Intent(ViewProfile.this, EditProfile.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent i2 = new Intent(ViewProfile.this, CreateProgram.class);
                i2.putExtra("from", "profile");
                startActivity(i2);
                return true;
            case R.id.item3:
                Intent i3 = new Intent(ViewProfile.this, CreateDiscussion.class);
                i3.putExtra("from", "profile");
                startActivity(i3);
                return true;
            case R.id.item4:
                Intent i4 = new Intent(ViewProfile.this, SavedArticles.class);
                startActivity(i4);
                return true;
            case R.id.item5:
                m_auth.signOut();
                Intent i5 = new Intent(ViewProfile.this, Login.class);
                startActivity(i5);
                finish();
                return true;
            case R.id.item6:
                m_ref.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("payments")){
                            Intent i6 = new Intent(ViewProfile.this, RetrievePayment.class);
                            startActivity(i6);
                        }
                        else {
                            Toast.makeText(ViewProfile.this, "You have no payments", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            default:
                return false;
        }
    }

    public void setStars(double average){
        if(average >= 0 && average < 1){
            star1.setImageResource(R.drawable.half_star);
            star2.setImageResource(R.drawable.star_white);
            star3.setImageResource(R.drawable.star_white);
            star4.setImageResource(R.drawable.star_white);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average == 1){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_white);
            star3.setImageResource(R.drawable.star_white);
            star4.setImageResource(R.drawable.star_white);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average > 1 && average < 2){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.half_star);
            star3.setImageResource(R.drawable.star_white);
            star4.setImageResource(R.drawable.star_white);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average == 2){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.star_white);
            star4.setImageResource(R.drawable.star_white);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average > 2 && average < 3){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.half_star);
            star4.setImageResource(R.drawable.star_white);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average == 3){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.star_yellow);
            star4.setImageResource(R.drawable.star_white);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average > 3 && average < 4){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.star_yellow);
            star4.setImageResource(R.drawable.half_star);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average == 4){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.star_yellow);
            star4.setImageResource(R.drawable.star_yellow);
            star5.setImageResource(R.drawable.star_white);
        }
        else if(average > 4 && average < 5){
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.star_yellow);
            star4.setImageResource(R.drawable.star_yellow);
            star5.setImageResource(R.drawable.half_star);
        }
        else {
            star1.setImageResource(R.drawable.star_yellow);
            star2.setImageResource(R.drawable.star_yellow);
            star3.setImageResource(R.drawable.star_yellow);
            star4.setImageResource(R.drawable.star_yellow);
            star5.setImageResource(R.drawable.star_yellow);
        }
    }

}
