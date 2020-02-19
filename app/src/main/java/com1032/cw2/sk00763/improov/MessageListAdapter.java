package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by Stelios on 17/02/2020.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    public static final int MSG_TYPE_SENT = 0;
    public static final int MSG_TYPE_RECEIVED = 1;
    private Context mContext;
    private List<Message> messageList;

    FirebaseUser m_user;

    public MessageListAdapter(Context mContext, List<Message> messageList) {
        this.messageList = messageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_SENT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_sent, parent, false);
            return new MessageListAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_received, parent, false);
            return new MessageListAdapter.ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.ViewHolder holder, int position) {

        Message message = messageList.get(position);

        holder.showMessage.setText(message.getMessage());
        holder.messageTime.setText(message.getTime());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView showMessage;
        public TextView messageTime;

        public ViewHolder(View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            messageTime = itemView.findViewById(R.id.message_time);
        }

    }

    @Override
    public int getItemViewType(int position) {
         m_user = FirebaseAuth.getInstance().getCurrentUser();
        if (messageList.get(position).getSender().equals(m_user.getUid())) {
            return MSG_TYPE_SENT;
        } else {
            return MSG_TYPE_RECEIVED;
        }
    }
}
