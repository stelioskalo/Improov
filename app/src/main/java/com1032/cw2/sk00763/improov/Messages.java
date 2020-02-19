package com1032.cw2.sk00763.improov;

import android.graphics.Canvas;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Messages extends Fragment {
    private View m_View = null;
    private RecyclerView list = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private List<String> userList = null;
    private List<User> users;
    private List<Message> lastMessages = null;
    private ChatListAdapter chatAdapter;
    private Message lastMessage;

    public Messages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.fragment_messages, container, false);
        list = m_View.findViewById(R.id.messageslist);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        userList = new ArrayList<String>();

        list.setHasFixedSize(true);
        list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        m_ref.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);
                    if(message.getSender() != null) {
                        if (message.getSender().equals(m_user.getUid())) {
                            userList.add(message.getReceiver());
                        }
                    }
                    if(message.getReceiver() != null) {
                        if (message.getReceiver().equals(m_user.getUid())) {
                            userList.add(message.getSender());
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        readChats();

        return m_View;
    }

    private void readChats() {
        users = new ArrayList<>();
        lastMessages = new ArrayList<>();
        m_ref = FirebaseDatabase.getInstance().getReference();

        m_ref.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final User user = snapshot.getValue(User.class);
                    for (String id : userList) {
                        if (user.getId().equals(id)) {
                            if (users.size() != 0) {
                                for (User user2 : users) {
                                    if (!user.getId().equals(user2.getId())) {
                                        users.add(user);
                                        m_ref.child("message").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Message message = snapshot.getValue(Message.class);
                                                    if ((message.getSender().equals(user.getId()) && message.getReceiver().equals(m_user.getUid())) ||
                                                            (message.getReceiver().equals(user.getId()) && message.getSender().equals(m_user.getUid()))) {
                                                        lastMessages.add(message);
                                                    }
                                                }
                                                chatAdapter = new ChatListAdapter(getActivity().getApplicationContext(), users, lastMessages);
                                                list.setAdapter(chatAdapter);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                            } else {
                                users.add(user);
                                m_ref.child("message").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Message message = snapshot.getValue(Message.class);
                                            if ((message.getSender().equals(user.getId()) && message.getReceiver().equals(m_user.getUid())) ||
                                                    (message.getReceiver().equals(user.getId()) && message.getSender().equals(m_user.getUid()))) {
                                                lastMessages.add(message);
                                            }
                                        }
                                        chatAdapter = new ChatListAdapter(getActivity().getApplicationContext(), users, lastMessages);
                                        list.setAdapter(chatAdapter);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }

//    public Message getLastMessage(final String userId) {
//        DatabaseReference m_ref = FirebaseDatabase.getInstance().getReference();
//        m_ref.child("message").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Message message = snapshot.getValue(Message.class);
//                    if ((message.getSender().equals(userId) && message.getReceiver().equals(m_user.getUid())) || (message.getReceiver().equals(userId) && message.getSender().equals(m_user.getUid()))) {
//                        lastMessage = message;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        return lastMessage;
//    }

}
