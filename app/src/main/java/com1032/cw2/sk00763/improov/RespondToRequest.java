package com1032.cw2.sk00763.improov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RespondToRequest extends Activity {
    private ImageView pic = null;
    private TextView program = null;
    private TextView date = null;
    private TextView type = null;
    private TextView accept = null;
    private TextView reject = null;
    private TextView seesession = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        pic = findViewById(R.id.viewUser);
        program = findViewById(R.id.whichProgram);
        date = findViewById(R.id.dateOfSession);
        type = findViewById(R.id.hours);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        seesession = findViewById(R.id.seesessions);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        program.setText(getIntent().getStringExtra("program"));
        date.setText(getIntent().getStringExtra("date"));
        if(getIntent().getStringExtra("type").matches("requestForSession1hr")){
            type.setText("1 hour session");
        }

        final String pending = getIntent().getStringExtra("pending");
        Log.d("pending", pending);
        if(pending.matches("no")){
            seesession.setTextColor(Color.parseColor("#000000"));
            seesession.setText("Manage this session at the session tab");
            accept.setTextColor(Color.parseColor("#C6ECFF"));
            reject.setTextColor(Color.parseColor("#C6ECFF"));
        }
        else {
            seesession.setTextColor(Color.parseColor("#C6ECFF"));
            accept.setTextColor(Color.parseColor("#5CEA4D"));
            reject.setTextColor(Color.parseColor("#E9442D"));
        }

        m_ref.child("user").child(getIntent().getStringExtra("from")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(dataSnapshot.hasChild("image")){
                    try {
                        pic.setImageBitmap(decodeFromFirebaseBase64(user.getImage()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                Date currentTime = Calendar.getInstance().getTime();

                String id = getIntent().getStringExtra("id");

                if(pending.matches("yes")){
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(id).child("pending").setValue("no");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", m_user.getUid());
                    hashMap.put("receiver", getIntent().getStringExtra("from"));
                    hashMap.put("message", "This is an automated reply. I have accepted your request for a session " +
                            "for " + "'" + getIntent().getStringExtra("program") + "'" + " on the " + getIntent().getStringExtra("date") +
                            " and we will be in touch shortly!");
                    hashMap.put("time", String.valueOf(hour) + ":" + String.valueOf(minute));
                    hashMap.put("date", currentTime.toString());
                    hashMap.put("senderName", m_user.getEmail());

                    notifyUserAccept();

                    finish();
                }
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");
                if(pending.matches("yes")){
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(id).child("pending").setValue("no");
                    notifyUserReject();
                    finish();
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));
    }

    public void notifyUserAccept(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        Date currentTime = Calendar.getInstance().getTime();

        String notificationId = RandomNumber.generateUID();
        String sessionId = RandomNumber.generateUID();

        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("type").setValue("acceptedRequest");
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("from").setValue(m_user.getUid());
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("date").setValue(currentTime.toString());
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("hour").setValue(String.valueOf(hour) + ":" + String.valueOf(minute));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("program").setValue(getIntent().getStringExtra("program"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("dateofrequest").setValue(getIntent().getStringExtra("date"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("notificationId").setValue(notificationId);
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("programid").setValue(getIntent().getStringExtra("programid"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("pending").setValue("yes");
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("topay").setValue(getIntent().getStringExtra("topay"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("session").setValue(sessionId);


        m_ref.child("session").child(sessionId).child("coach").setValue(m_user.getUid());
        m_ref.child("session").child(sessionId).child("student").setValue(getIntent().getStringExtra("from"));
        m_ref.child("session").child(sessionId).child("program").setValue(getIntent().getStringExtra("programid"));
        m_ref.child("session").child(sessionId).child("paid").setValue("no");
        m_ref.child("session").child(sessionId).child("sessionId").setValue(sessionId);
        m_ref.child("session").child(sessionId).child("date").setValue(getIntent().getStringExtra("date"));
        m_ref.child("session").child(sessionId).child("howlong").setValue(getIntent().getStringExtra("howlong"));

        String howlong = getIntent().getStringExtra("howlong");
        if(howlong.matches("hour")){
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("howlong").setValue("hour");
        }
        else {
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("howlong").setValue("month");
        }
    }

    public void notifyUserReject(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        Date currentTime = Calendar.getInstance().getTime();

        String notificationId = RandomNumber.generateUID();
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("type").setValue("rejectedRequest");
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("from").setValue(m_user.getUid());
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("date").setValue(currentTime.toString());
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("hour").setValue(String.valueOf(hour) + ":" + String.valueOf(minute));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("program").setValue(getIntent().getStringExtra("program"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("dateofrequest").setValue(getIntent().getStringExtra("date"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("notificationId").setValue(notificationId);
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("programid").setValue(getIntent().getStringExtra("programid"));
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("pending").setValue(notificationId);
        m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("topay").setValue(getIntent().getStringExtra("topay"));

        String howlong = getIntent().getStringExtra("howlong");

        if(howlong.matches("hour")){
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("howlong").setValue("hour");
        }
        else {
            m_ref.child("user").child(getIntent().getStringExtra("from")).child("notification").child(notificationId).child("howlong").setValue("month");
        }
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }


}
