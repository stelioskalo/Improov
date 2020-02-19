package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by Stelios on 20/12/2019.
 */

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.SearchViewHolder>  {

    //context object
    private Context m_context = null;
    //list of events
    private List<Program> m_programs = null;
    //custom click listener for events
    private ProgramListAdapter.ProgramActionListener m_programListener = null;

    public ProgramListAdapter(Context context, List<Program> programs, ProgramListAdapter.ProgramActionListener programActionListener) {
        super();
        this.m_context = context;
        this.m_programs = programs;
        this.m_programListener = programActionListener;
    }

    @Override
    public ProgramListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.program_layout, parent, false);
        return new ProgramListAdapter.SearchViewHolder(view, m_programListener);
    }

    @Override
    public void onBindViewHolder(final ProgramListAdapter.SearchViewHolder holder, int position) {
        //set the event details
        holder.m_name.setText(m_programs.get(position).getName());

        //get the eventId of the event
        final String coachid = m_programs.get(position).getCoach();
        final String programid = m_programs.get(position).getProgramId();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("user").child(coachid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.m_coach.setText(dataSnapshot.child("name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbRef.child("program").child(programid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("programImage").getValue().toString();
                try {
                    holder.m_image.setImageBitmap(decodeFromFirebaseBase64(image));
                }
                catch(IOException e){
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
    /**
     * Method to return number of events
     * @return: number of events
     */
    @Override
    public int getItemCount() {
        return m_programs.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //event name
        private TextView m_name = null;
        //event category
        private TextView m_coach = null;
        //event image
        private ImageView m_image = null;
        //on event click listener
        private ProgramListAdapter.ProgramActionListener m_listener = null;

        private SearchViewHolder(View itemView, ProgramListAdapter.ProgramActionListener programActionListener) {
            super(itemView);

            //inflate widgets
            m_name = (TextView) itemView.findViewById(R.id.programname);
            m_coach = (TextView) itemView.findViewById(R.id.coachName);
            m_image = (ImageView) itemView.findViewById(R.id.programpic);

            m_listener = programActionListener;

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onProgramClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface ProgramActionListener{
        /** on event click */
        void onProgramClick(int position);
    }
}
