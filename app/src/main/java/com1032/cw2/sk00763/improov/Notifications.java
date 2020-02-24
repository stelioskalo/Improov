package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Notifications extends Fragment implements NotificationListAdapter.NotificationActionListener {
    private RecyclerView list = null;
    private View m_View = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private List<Notification> notificationList = null;
    private NotificationListAdapter m_adapter = null;
    private LinearLayout layout = null;

    public Notifications() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.fragment_nottifications, container, false);
        list = m_View.findViewById(R.id.notificationlist);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        notificationList = new ArrayList<Notification>();
        layout = m_View.findViewById(R.id.notificationlayout);


        list.setHasFixedSize(true);
        list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        populatelist();

        return m_View;
    }

    public void populatelist(){
        list.removeAllViews();
        notificationList.clear();

        m_ref.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("notification")){
                    m_ref.child("user").child(m_user.getUid()).child("notification").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Notification notification = ds.getValue(Notification.class);
                                Log.d("Date", notification.getDate());
                                notificationList.add(notification);
                            }
                            m_adapter = new NotificationListAdapter(getActivity().getApplicationContext(), notificationList, Notifications.this);
                            list.setAdapter(m_adapter);
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
    public void onNotificationClick(int position) {
        if(notificationList.get(position).getType().matches("requestForSession1hr") || notificationList.get(position).getType().matches("requestForMonthSession")) {
            Intent i = new Intent(getActivity(), RespondToRequest.class);
            i.putExtra("from", notificationList.get(position).getFrom());
            i.putExtra("notificationId", notificationList.get(position).getNotificationId());
            i.putExtra("type", notificationList.get(position).getType());
            i.putExtra("date", notificationList.get(position).getDate());
            i.putExtra("program", notificationList.get(position).getProgram());
            i.putExtra("pending", notificationList.get(position).getPending());
            i.putExtra("id", notificationList.get(position).getNotificationId());
            startActivity(i);
        }
    }

}
