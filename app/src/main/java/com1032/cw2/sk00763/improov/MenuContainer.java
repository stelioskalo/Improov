package com1032.cw2.sk00763.improov;
/**
 * Last edited by Stelios at 05/03/2019
 */

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The MenuContainer activity simply holds the layout of the menu.
 */
public class MenuContainer extends AppCompatActivity {

    FragmentTransaction m_Transaction;
    TextView m_hometext = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;


    /**
     * onCreate will set the view to menu_container layout
     * and begin a transaction between fragments and give functionality
     * to change the view of the relative layout when something is pressed
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_menu_container);
        m_hometext = (TextView) findViewById(R.id.hometext);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        ArticlePage articleFragment = new ArticlePage();
        m_Transaction = getFragmentManager().beginTransaction();
        m_hometext.setTextColor(Color.parseColor("#000000"));
        m_Transaction.replace(R.id.display, articleFragment);
        m_Transaction.commit();

        Thread thread = new Thread() {
            @Override
            public void run() {

                String userId = m_user.getUid();
                m_ref.child("user").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (!user.getRinging().matches("")) {
                            Log.d("empikame", "nai");
                            Intent intent = new Intent(MenuContainer.this, CallActivity.class);
                            intent.putExtra("receiver", m_user.getUid());
                            intent.putExtra("sender", user.getRinging());
                            intent.putExtra("for", dataSnapshot.child("call").getValue().toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        thread.start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {

                String userId = m_user.getUid();
                m_ref.child("user").child(userId).child("notification").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Notification notification = ds.getValue(Notification.class);
                            if (notification.getType() != null) {
                                if (notification.getType().matches("acceptedRequest") && notification.getPending().matches("yes")) {
                                    Log.d("empikame", "nai");
                                    Intent intent = new Intent(MenuContainer.this, ReadyToPayActivity.class);
                                    intent.putExtra("from", notification.getFrom());
                                    intent.putExtra("program", notification.getProgram());
                                    intent.putExtra("date", notification.getDate());
                                    intent.putExtra("hour", notification.getHour());
                                    intent.putExtra("notification", notification.getNotificationId());
                                    intent.putExtra("programid", notification.getProgramid());
                                    intent.putExtra("topay", notification.getTopay());
                                    intent.putExtra("session", notification.getSession());
                                    startActivity(intent);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        thread2.start();

    }
}
