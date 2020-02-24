package com1032.cw2.sk00763.improov;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
    private static String API_KEY = "46516972";
    private static String SESSION_ID = "1_MX40NjUxNjk3Mn5-MTU4MjEzNzU0MTcwOH4vWjMxK1Q5MkoySzB5bkQvZmNZMTF3ZTZ-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjUxNjk3MiZzaWc9NTJiYWNhYTExNGM2NTA2NzY3NDJhMzVlMDNmNzU0ZDU0Y2NmNzcyODpzZXNzaW9uX2lkPTFfTVg0ME5qVXhOamszTW41LU1UVTRNakV6TnpVME1UY3dPSDR2V2pNeEsxUTVNa295U3pCNWJrUXZabU5aTVRGM1pUWi1mZyZjcmVhdGVfdGltZT0xNTgyMTM3NjIwJm5vbmNlPTAuODQ3NTY1NTE1ODgzOTQ1OSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTg0NzI2MDE5JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ImageView close = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        close = findViewById(R.id.endchat);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCall();
            }
        });

        m_ref.child("user").child(m_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getCalling().matches("") && user.getRinging().matches("")){
                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }
                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }
                    mSession.disconnect();
                    Intent intent = new Intent(VideoCallActivity.this, MenuContainer.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        requestPermissions();
    }

    public void closeCall() {

        m_ref.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (!user.getRinging().matches("")) {
                    final String caller = user.getRinging();
                    m_ref.child("user").child(m_user.getUid()).child("ringing").setValue("");
                    m_ref.child("user").child(m_user.getUid()).child("picked").removeValue();
                    m_ref.child("user").child(m_user.getUid()).child("call").removeValue();
                    m_ref.child("user").child(caller).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user2 = dataSnapshot.getValue(User.class);
                            m_ref.child("user").child(caller).child("calling").setValue("");
                            m_ref.child("user").child(caller).child("picked").removeValue();
                            if (mPublisher != null) {
                                mPublisher.destroy();
                            }
                            if (mSubscriber != null) {
                                mSubscriber.destroy();
                            }
                            mSession.disconnect();
                            Intent intent = new Intent(VideoCallActivity.this, MenuContainer.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else if (!user.getCalling().matches("")) {
                    final String receiver = user.getCalling();
                    m_ref.child("user").child(m_user.getUid()).child("calling").setValue("");
                    m_ref.child("user").child(m_user.getUid()).child("picked").removeValue();
                    m_ref.child("user").child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user2 = dataSnapshot.getValue(User.class);
                            m_ref.child("user").child(receiver).child("ringing").setValue("");
                            m_ref.child("user").child(receiver).child("picked").removeValue();
                            m_ref.child("user").child(receiver).child("call").removeValue();
                            if (mPublisher != null) {
                                mPublisher.destroy();
                            }
                            if (mSubscriber != null) {
                                mSubscriber.destroy();
                            }
                            mSession.disconnect();
                            Intent intent = new Intent(VideoCallActivity.this, MenuContainer.class);
                            startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);

            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);

            // initialize and connect to the session


        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onConnected(Session session) {
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}