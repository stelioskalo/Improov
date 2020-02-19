package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
 * Created by Stelios on 16/02/2020.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.SearchViewHolder> {

    private Context m_context;
    private List<User> usersList;
    private List<Message> lastMessages;
    private ChatListAdapter.ChatActionListener m_chatListener = null;

    public ChatListAdapter(Context m_context, List<User> usersList, List<Message> lastMessages) {
        this.usersList = usersList;
        this.m_context = m_context;
        this.lastMessages = lastMessages;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.chat_layout, parent, false);
        return new ChatListAdapter.SearchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {

        final User user = usersList.get(position);
        holder.name.setText(user.getName());
        final String id = usersList.get(position).getId();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("user").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();
                try {
                    holder.image.setImageBitmap(decodeFromFirebaseBase64(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (lastMessages.get(position) != null) {
            holder.lastMessage.setText(lastMessages.get(position).getMessage());
            holder.lasttime.setText(lastMessages.get(position).getTime());
        } else {
            holder.lastMessage.setText("");
            holder.lasttime.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(m_context, Chat.class);
                intent.putExtra("userid", user.getId());
                intent.putExtra("name", user.getName());
                intent.putExtra("image", user.getImage());
                m_context.startActivity(intent);
            }
        });

    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private ImageView image;
        private TextView lastMessage;
        private TextView lasttime;
        //on event click listener
        private ChatListAdapter.ChatActionListener m_listener = null;

        private SearchViewHolder(View itemView) {
            super(itemView);

            //inflate widgets
            name = itemView.findViewById(R.id.chatname);
            image = itemView.findViewById(R.id.chatimage);
            lastMessage = itemView.findViewById(R.id.chatlastmessage);
            lasttime = itemView.findViewById(R.id.lastmessagetime);

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onChatClick(getAdapterPosition());
        }
    }

    public interface ChatActionListener{
        /** on event click */
        void onChatClick(int position);
    }
}
