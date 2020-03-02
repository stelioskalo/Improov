package com1032.cw2.sk00763.improov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Base64;
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

public class ViewSession extends Activity {
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

}
