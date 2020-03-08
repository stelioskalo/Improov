package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Sessions extends Fragment implements SessionListAdapter.SessionActionListener {
    private RecyclerView list = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private List<Session> sessions = null;
    private SessionListAdapter m_adapter = null;

    public Sessions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View m_View = inflater.inflate(R.layout.fragment_sessions, container, false);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        sessions = new ArrayList<Session>();
        list = m_View.findViewById(R.id.sessionlist);

        list.setHasFixedSize(true);
        list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        //populateList();

        return m_View;
    }

    public void populateList(){
        list.removeAllViews();
        sessions.clear();
        m_ref.child("session").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Session session = ds.getValue(Session.class);
                    if(session.getCoach().matches(m_user.getUid()) || session.getStudent().matches(m_user.getUid())){
                        sessions.add(session);
                    }
                }
                m_adapter = new SessionListAdapter(getActivity().getApplicationContext(), sessions, Sessions.this);
                list.setAdapter(m_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public void onSessionClick(int position) {
        Intent intent = new Intent(getActivity(), ViewSession.class);
        intent.putExtra("sessionId", sessions.get(position).getSessionId());
        intent.putExtra("coach", sessions.get(position).getCoach());
        intent.putExtra("student", sessions.get(position).getStudent());
        intent.putExtra("paid", sessions.get(position).getPaid());
        intent.putExtra("programid", sessions.get(position).getProgram());
        intent.putExtra("date", sessions.get(position).getDate());
        intent.putExtra("howlong", sessions.get(position).getHowlong());
        startActivity(intent);
    }
}
