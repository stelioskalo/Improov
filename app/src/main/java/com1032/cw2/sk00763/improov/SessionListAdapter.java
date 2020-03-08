package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

/**
 * Created by Stelios on 01/03/2020.
 */

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SearchViewHolder> {
    //context object
    private Context m_context = null;
    //list of events
    private List<Session> m_sessions = null;
    //custom click listener for events
    private SessionListAdapter.SessionActionListener m_sessionListener = null;

    /**
     * Constructor for adapter
     * @param context: context of app
     * @param sessions: list of discussions
     * @param eventActionListener: custom  listener for our discussions list
     */
    public SessionListAdapter(Context context, List<Session> sessions, SessionListAdapter.SessionActionListener eventActionListener) {
        super();
        this.m_context = context;
        this.m_sessions = sessions;
        this.m_sessionListener = eventActionListener;
    }

    @Override
    public SessionListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.session_layout, parent, false);
        return new SessionListAdapter.SearchViewHolder(view, m_sessionListener);
    }

    @Override
    public void onBindViewHolder(final SessionListAdapter.SearchViewHolder holder, int position) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("session").child(m_sessions.get(position).getSessionId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseAuth m_auth = FirebaseAuth.getInstance();
                FirebaseUser m_user = m_auth.getCurrentUser();
                Session session = dataSnapshot.getValue(Session.class);
                if(session.getStudent().matches(m_user.getUid()) && !session.getCoach().matches(m_user.getUid())){
                    dbRef.child("user").child(session.getCoach()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            holder.m_name.setText(user.getName() + " " + user.getSurname());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    dbRef.child("user").child(session.getStudent()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            holder.m_name.setText(user.getName() + " " + user.getSurname());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                dbRef.child("program").child(session.getProgram()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Program program = dataSnapshot.getValue(Program.class);
                        holder.m_program.setText(program.getName());
                        String image = program.getProgramImage();
                        try {
                            holder.m_image.setImageBitmap(decodeFromFirebaseBase64(image));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(session.getMarkcompletecoach().matches("yes") && session.getCoach().matches(m_user.getUid())){
                    holder.card.setBackgroundColor(Color.parseColor("#5DE744"));
                }
                else {
                    holder.card.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if(session.getMarkcompletestudent().matches("yes") && session.getStudent().matches(m_user.getUid())){
                    holder.card.setBackgroundColor(Color.parseColor("#5DE744"));
                }
                else {
                    holder.card.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
    /**
     * Method to return number of discussions
     * @return: number of discussions
     */
    @Override
    public int getItemCount() {
        return m_sessions.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //event name
        private TextView m_program = null;
        //event category
        private TextView m_name = null;
        //event image
        private ImageView m_image = null;

        private CardView card = null;
        //on event click listener
        private SessionListAdapter.SessionActionListener m_listener = null;

        private SearchViewHolder(View itemView, SessionListAdapter.SessionActionListener sessionActionListener) {
            super(itemView);

            //inflate widgets
            m_name = (TextView) itemView.findViewById(R.id.sessionperson);
            m_program = (TextView) itemView.findViewById(R.id.sessionname);
            m_image = (ImageView) itemView.findViewById(R.id.programimage);
            card = (CardView) itemView.findViewById(R.id.sessioncv);

            m_listener = sessionActionListener;

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onSessionClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface SessionActionListener{
        /** on event click */
        void onSessionClick(int position);
    }
}
