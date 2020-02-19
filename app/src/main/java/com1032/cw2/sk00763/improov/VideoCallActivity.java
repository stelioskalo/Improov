package com1032.cw2.sk00763.improov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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


public class VideoCallActivity extends AppCompatActivity {
    private ImageView image = null;
    private TextView name = null;
    private TextView surname = null;
    private ImageView accept = null;
    private ImageView decline = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private String senderId = null;
    private String senderimage = null;
    private String sendername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        image = findViewById(R.id.videocallimage);
        name = findViewById(R.id.videocallname);
        surname = findViewById(R.id.videocallsurname);
        accept = findViewById(R.id.acceptvideocall);
        decline = findViewById(R.id.declinevideocall);

        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        senderId = m_auth.getCurrentUser().getUid();

        loadUserData();
    }

    public void loadUserData() {
        final String userId = getIntent().getStringExtra("receiver");

        m_ref.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userimage = dataSnapshot.child(userId).child("image").getValue().toString();
                String username = dataSnapshot.child(userId).child("name").getValue().toString();
                String usersurname = dataSnapshot.child(userId).child("surname").getValue().toString();
                name.setText(username);
                surname.setText(usersurname);

                try {
                    image.setImageBitmap(decodeFromFirebaseBase64(userimage));
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
}
