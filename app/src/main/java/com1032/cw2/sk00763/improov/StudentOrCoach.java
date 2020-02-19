package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;


public class StudentOrCoach extends AppCompatActivity {
    private FirebaseAuth m_auth = null;
    CardView student = null;
    CardView coach = null;
    private int selected = 0;
    private String useris = null;
    private RelativeLayout next = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_or_coach);
        m_auth = FirebaseAuth.getInstance();
        student = (CardView) findViewById(R.id.card_student);
        coach = (CardView) findViewById(R.id.card_coach);
        next = findViewById(R.id.nextfromuser);
        useris = "student";
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useris = "Student";
                student.setCardBackgroundColor(Color.parseColor("#3253F9"));
                coach.setCardBackgroundColor(Color.parseColor("#94A9D8"));
            }
        });

        coach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useris = "Coach";
                coach.setCardBackgroundColor(Color.parseColor("#3253F9"));
                student.setCardBackgroundColor(Color.parseColor("#94A9D8"));

            }
        });
        String tempuserId = null;
        if(Profile.getCurrentProfile() != null) {
            final String fbuserId = Profile.getCurrentProfile().getId();
            tempuserId = fbuserId;
        }
        else {
            final String fireuserId = m_auth.getCurrentUser().getUid();
            tempuserId = fireuserId;
        }
        final String userId = tempuserId;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(useris.matches("Coach")){
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                reference.child("user").child(userId).child("usertype").setValue("Coach");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Intent i = new Intent(StudentOrCoach.this, CreateProgram.class);
                        startActivity(i);
                        finish();
                }
                else {
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reference.child("user").child(userId).child("usertype").setValue("Student");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    FirebaseUser user = m_auth.getCurrentUser();
                    reference.child("user").child(user.getUid()).child("firsttime").setValue("no");
                    Intent i = new Intent(StudentOrCoach.this, MenuContainer.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

}
