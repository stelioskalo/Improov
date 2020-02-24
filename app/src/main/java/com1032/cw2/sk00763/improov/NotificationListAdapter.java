package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Stelios on 14/02/2020.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.SearchViewHolder> {
    //context object
    private Context m_context = null;
    //list of notifications
    private List<Notification> m_notifications = null;
    //custom click listener for events
    private NotificationListAdapter.NotificationActionListener m_notificationListener = null;

    /**
     * Constructor for adapter
     *
     * @param context:             context of app
     * @param notifications:       list of notifications
     * @param eventActionListener: custom  listener for our notifications list
     */
    public NotificationListAdapter(Context context, List<Notification> notifications, NotificationListAdapter.NotificationActionListener eventActionListener) {
        super();
        this.m_context = context;
        this.m_notifications = notifications;
        this.m_notificationListener = eventActionListener;
    }

    @Override
    public NotificationListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.notification_layout, parent, false);
        return new NotificationListAdapter.SearchViewHolder(view, m_notificationListener);
    }

    @Override
    public void onBindViewHolder(final NotificationListAdapter.SearchViewHolder holder, final int position) {
        //set the event details
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        holder.m_date.setText(m_notifications.get(position).getDate());
        String type = m_notifications.get(position).getType();
        switch (type) {
            case "requestForSession1hr":
                dbRef.child("user").child(m_notifications.get(position).getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        holder.m_about.setText("You received a request from " + user.getName() + " " + user.getSurname() + " "
                                + "for a session for your " + "'" + m_notifications.get(position).getProgram() + "'" + "program on "
                                + m_notifications.get(position).getDate());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "requestForMonthSession":
                dbRef.child("user").child(m_notifications.get(position).getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        holder.m_about.setText("You received a request from " + user.getName() + " " + user.getSurname() + " "
                                + "for monthly access to your " + "'" + m_notifications.get(position).getProgram() + "'" + "program on "
                                + m_notifications.get(position).getDate());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "requestedSession":
                dbRef.child("user").child(m_notifications.get(position).getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        holder.m_about.setText("You have requested a session with " + user.getName() + " " + user.getSurname() + " "
                                + "for their " + "'" + m_notifications.get(position).getProgram() + "'" + " program on "
                                + m_notifications.get(position).getDate());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "acceptedRequest":
                dbRef.child("user").child(m_notifications.get(position).getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        holder.m_about.setText("Your requested session with " + user.getName() + " " + user.getSurname() + " "
                                + "for their " + "'" + m_notifications.get(position).getProgram() + "'" + " program on "
                                + m_notifications.get(position).getDate() + " has been accepted.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "rejectedRequest":
                dbRef.child("user").child(m_notifications.get(position).getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        holder.m_about.setText("Your requested session with " + user.getName() + " " + user.getSurname() + " "
                                + "for their " + "'" + m_notifications.get(position).getProgram() + "'" + " program on "
                                + m_notifications.get(position).getDate() + " has been rejected.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            default:
                // code block
                break;

        }

    }

    /**
     * Method to return number of notifications
     *
     * @return: number of events
     */
    @Override
    public int getItemCount() {
        return m_notifications.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //event name
        private TextView m_about = null;
        //event category
        //event image
        private TextView m_date = null;
        //on event click listener
        private NotificationListAdapter.NotificationActionListener m_listener = null;

        private SearchViewHolder(View itemView, NotificationListAdapter.NotificationActionListener notificationActionListener) {
            super(itemView);

            //inflate widgets
            m_about = (TextView) itemView.findViewById(R.id.notificationabout);
            m_date = (TextView) itemView.findViewById(R.id.date);

            m_listener = notificationActionListener;


            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onNotificationClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface NotificationActionListener {
        /**
         * on event click
         */
        void onNotificationClick(int position);
    }
}
