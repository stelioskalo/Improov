package com1032.cw2.sk00763.improov;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Chat extends Activity {
    private ImageView back = null;
    private ImageView prof = null;
    private ImageView audio = null;
    private ImageView video = null;
    private TextView name = null;
    private RecyclerView list = null;
    private EditText message = null;
    private ImageView send = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private NotificationManagerCompat notificationManager;
    private boolean notify = false;
    private static final String CHANNEL_ID = "channel_id";
    private List<Message> messageList;
    private MessageListAdapter m_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        createNotificationChannel();

        back = findViewById(R.id.backchat);
        prof = findViewById(R.id.messangericon);
        audio = findViewById(R.id.audiocall);
        video = findViewById(R.id.videocall);
        name = findViewById(R.id.messangername);
        list = findViewById(R.id.chatlist);
        message = findViewById(R.id.typemessage);
        send = findViewById(R.id.sendmessage);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        messageList = new ArrayList<Message>();

        notificationManager = NotificationManagerCompat.from(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String userid = getIntent().getStringExtra("userid");
        Log.d("imastedame", getIntent().getStringExtra("name"));
        name.setText(getIntent().getStringExtra("name"));
        String image = getIntent().getStringExtra("image");
        try {
            prof.setImageBitmap(decodeFromFirebaseBase64(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message2 = message.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                //String dateTime = currentTime.toString();
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String time = dateFormat.format(currentTime);
                sendMessage(m_user.getUid(), userid, message2, time, currentTime.toString(), m_user.getEmail());
                message.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, CallActivity.class);
                intent.putExtra("receiver", userid);
                intent.putExtra("sender", m_user.getUid());
                intent.putExtra("for", "audiocall");
                startActivity(intent);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, CallActivity.class);
                intent.putExtra("receiver", userid);
                intent.putExtra("sender", m_user.getUid());
                intent.putExtra("for", "videocall");
                startActivity(intent);
            }
        });

        readMessages(m_user.getUid(), userid);


    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private void sendMessage(String sender, final String receiver, String message, String dateTime, String date, String senderName) {
        messageList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("time", dateTime);
        hashMap.put("date", date);
        hashMap.put("senderName", senderName);

        reference.child("message").push().setValue(hashMap);
    }

    private void readMessages(final String myid, final String userid) {
        messageList.clear();
        // messages = new Messages(myid, userid);

        m_ref.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Damee", "Edo");
                    Message message = snapshot.getValue(Message.class);
                    if ((message.getReceiver().equals(myid) && message.getSender().equals(userid))) {
                        //  sendNotification(message);
                        // message.setNotify(false)
                        messageList.add(message);
                        //messages.adduser2(message);


                    } else if ((message.getReceiver().equals(userid) && message.getSender().equals(myid))) {
                        messageList.add(message);
                        // messages.adduser1(message);
                    }

                }

                m_adapter = new MessageListAdapter(Chat.this, messageList);
                list.setAdapter(m_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
