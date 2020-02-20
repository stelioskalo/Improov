package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;


public class CallActivity extends AppCompatActivity {
    private ImageView image = null;
    private TextView name = null;
    private TextView surname = null;
    private ImageView accept = null;
    private ImageView decline = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private String senderimage = null;
    private String sendername = null;
    private MediaPlayer mediaPlayer = null;

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

        mediaPlayer = MediaPlayer.create(this, R.raw.ring);

        call();

        loadUserData();

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineCall();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                m_ref.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(!user.getRinging().matches("") || !user.getCalling().matches("")){
                            m_ref.child("user").child(getIntent().getStringExtra("sender")).child("picked").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()){
                                        Intent intent = new Intent(CallActivity.this, VideoCallActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(CallActivity.this, "Call has been dropped", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    public void loadUserData() {
        final String userId = getIntent().getStringExtra("receiver");
        final String senderId = getIntent().getStringExtra("sender");

        m_ref.child("user").child(m_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                if(!dataSnapshot.child("ringing").getValue().toString().matches("")) {
                    accept.setVisibility(View.VISIBLE);
                    String senderId = user.getRinging();
                    m_ref.child("user").child(senderId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User sender = dataSnapshot.getValue(User.class);
                            name.setText(sender.getName());
                            surname.setText(sender.getSurname());

                            try {
                                image.setImageBitmap(decodeFromFirebaseBase64(sender.getImage()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(!dataSnapshot.child("calling").getValue().toString().matches("")){
                    String receiverId = user.getCalling();
                    m_ref.child("user").child(receiverId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User receiver = dataSnapshot.getValue(User.class);
                            name.setText(receiver.getName());
                            surname.setText(receiver.getSurname());

                            try {
                                image.setImageBitmap(decodeFromFirebaseBase64(receiver.getImage()));
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        m_ref.child("user").child(m_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("picked")){
                        Log.d("eperasen", "nai");
                        mediaPlayer.stop();
                        Intent intent = new Intent(CallActivity.this, VideoCallActivity.class);
                        startActivity(intent);
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

    public void call(){
        mediaPlayer.start();
        final String receiverId = getIntent().getStringExtra("receiver");
        final String senderId = getIntent().getStringExtra("sender");
        m_ref.child("user").child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                if(dataSnapshot.child("calling").getValue().toString().matches("") && dataSnapshot.child("ringing").getValue().toString().matches("")) {
                    mediaPlayer.start();
                    m_ref.child("user").child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final User user2 = dataSnapshot.getValue(User.class);
                            m_ref.child("user").child(senderId).child("calling").setValue(receiverId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        m_ref.child("user").child(receiverId).child("ringing").setValue(senderId);
                                    }
                                }
                            });
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


    public void declineCall(){

        m_ref.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.getRinging().matches("")){
                    final String caller = user.getRinging();
                    m_ref.child("user").child(m_user.getUid()).child("ringing").setValue("");
                    m_ref.child("user").child(caller).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user2 = dataSnapshot.getValue(User.class);
                            m_ref.child("user").child(caller).child("calling").setValue("");
                            mediaPlayer.stop();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(!user.getCalling().matches("")){
                    final String receiver = user.getCalling();
                    m_ref.child("user").child(m_user.getUid()).child("calling").setValue("");
                    m_ref.child("user").child(m_user.getUid()).child("picked").removeValue();
                    m_ref.child("user").child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user2 = dataSnapshot.getValue(User.class);
                            m_ref.child("user").child(receiver).child("ringing").setValue("");
                            mediaPlayer.stop();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    mediaPlayer.stop();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
