package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.view.Menu;
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

import es.dmoral.toasty.Toasty;

public class ViewSession extends Activity implements PopupMenu.OnMenuItemClickListener {
    private ImageView back = null;
    private ImageView options = null;
    private ImageView image = null;
    private TextView program = null;
    private TextView name = null;
    private TextView surname = null;
    private TextView programdesc = null;
    private TextView date = null;
    private TextView payment = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private String oppositeUser = null;
    private CardView card = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_session);

        back = findViewById(R.id.backfromviewsession);
        options = findViewById(R.id.sessionoptions);
        image = findViewById(R.id.sessioncoachpic);
        program = findViewById(R.id.viewsessionname);
        name = findViewById(R.id.sessioncoachname);
        surname = findViewById(R.id.sessioncoachsurname);
        programdesc = findViewById(R.id.sessiondescription);
        date = findViewById(R.id.sessiondateandtime);
        payment = findViewById(R.id.sessionpaidstatus);
        card = findViewById(R.id.sessioncv);

        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        populateView();
    }

    public void populateView(){
        String paid = getIntent().getStringExtra("paid");

        if(paid.matches("yes")){
            payment.setText("Session Paid");
        }
        else {
            payment.setText("Not yet paid");
        }

        if(getIntent().getStringExtra("howlong").matches("hour")){
            date.setText(getIntent().getStringExtra("date") + " for an " + getIntent().getStringExtra("howlong") + " session");
        }
        else {
            date.setText(getIntent().getStringExtra("date") + " for a " + getIntent().getStringExtra("howlong") + " session");
        }

        String programId = getIntent().getStringExtra("program");
        m_ref.child("program").child(programId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Program program2 = dataSnapshot.getValue(Program.class);
                program.setText(program2.getName());
                programdesc.setText(program2.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(getIntent().getStringExtra("student").matches(m_user.getUid()) && !getIntent().getStringExtra("coach").matches(m_user.getUid())){
            m_ref.child("user").child(getIntent().getStringExtra("coach")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getName());
                    surname.setText(user.getSurname());
                    String pic = user.getImage();

                    try {
                        image.setImageBitmap(decodeFromFirebaseBase64(pic));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            m_ref.child("user").child(getIntent().getStringExtra("student")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getName());
                    surname.setText(user.getSurname());
                    String pic = user.getImage();

                    try {
                        image.setImageBitmap(decodeFromFirebaseBase64(pic));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void popup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.session_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                Intent i = new Intent(ViewSession.this, ConfirmationActivity.class);
                i.putExtra("for", "cancel");
                i.putExtra("user", m_user.getUid());
                i.putExtra("date", getIntent().getStringExtra("date"));
                i.putExtra("howlong", getIntent().getStringExtra("howlong"));
                i.putExtra("program", getIntent().getStringExtra("program"));
                i.putExtra("session",getIntent().getStringExtra("sessionId"));
                startActivity(i);
                return true;
            case R.id.payoption:
                if(!getIntent().getStringExtra("coach").matches(m_user.getUid())){
                    final String sessionId = getIntent().getStringExtra("sessionId");
                    m_ref.child("user").child(m_user.getUid()).child("notification").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Notification notification = ds.getValue(Notification.class);
                                if(notification.getSession().matches(sessionId) && getIntent().getStringExtra("paid").matches("no")){
                                    Intent i2 = new Intent(ViewSession.this, ReadyToPayActivity.class);
                                    i2.putExtra("from", notification.getFrom());
                                    i2.putExtra("program", notification.getProgram());
                                    i2.putExtra("date", notification.getDate());
                                    i2.putExtra("hour", notification.getHour());
                                    i2.putExtra("notification", notification.getNotificationId());
                                    i2.putExtra("programid", notification.getProgramid());
                                    i2.putExtra("topay", notification.getTopay());
                                    i2.putExtra("session", notification.getSession());
                                    startActivity(i2);
                                }
                                else if(notification.getSession().matches(sessionId) && getIntent().getStringExtra("paid").matches("yes")){
                                    Toasty.warning(ViewSession.this, "You have already paid!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toasty.warning(ViewSession.this, "You are the coach you can't pay!", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.complete:
                m_ref.child("session").child(getIntent().getStringExtra("sessionId")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Session session = dataSnapshot.getValue(Session.class);
                        if(session.getMarkcompletecoach().matches("no") && session.getCoach().matches(m_user.getUid())){
                            m_ref.child("session").child(getIntent().getStringExtra("sessionId")).child("markcompletecoach").setValue("yes");
                        }
                        else {
                            m_ref.child("session").child(getIntent().getStringExtra("sessionId")).child("markcompletecoach").setValue("no");
                        }
                        if(session.getMarkcompletestudent().matches("no") && session.getStudent().matches(m_user.getUid())){
                            m_ref.child("session").child(getIntent().getStringExtra("sessionId")).child("markcompletestudent").setValue("yes");
                        }
                        else {
                            m_ref.child("session").child(getIntent().getStringExtra("sessionId")).child("markcompletestudent").setValue("no");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
            case R.id.modify:
                Intent i3 = new Intent(ViewSession.this, RequestHourSession.class);
                i3.putExtra("for", "change");
                if(getIntent().getStringExtra("coach").matches(m_user.getUid())){
                    i3.putExtra("to", getIntent().getStringExtra("student"));
                }
                else {
                    i3.putExtra("to", getIntent().getStringExtra("coach"));
                }
                i3.putExtra("coachid", getIntent().getStringExtra("coach"));
                i3.putExtra("programid", getIntent().getStringExtra("programid"));
                i3.putExtra("session", getIntent().getStringExtra("sessionId"));
                startActivity(i3);
                return true;
            default:
                return false;
        }
    }

}
