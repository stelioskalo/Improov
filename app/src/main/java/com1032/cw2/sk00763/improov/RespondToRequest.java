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

                String messageId = RandomNumber.generateUID();
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
                    m_ref.child("message").push().setValue(hashMap);
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

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }


}
