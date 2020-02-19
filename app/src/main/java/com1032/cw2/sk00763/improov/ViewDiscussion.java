package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewDiscussion extends Activity implements PostListAdapter.PostActionListener {
    private TextView back = null;
    private TextView creator = null;
    private TextView about = null;
    private TextView postIt = null;
    private ImageView pic = null;
    private RecyclerView posts = null;
    private EditText writePost = null;
    private String discussionId = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private List<Post> postArray = null;
    private PostListAdapter m_adapter = null;
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_discussion);

        back = (TextView) findViewById(R.id.backfromviewdiscussion);
        creator = (TextView) findViewById(R.id.viewcreatorname);
        about = (TextView) findViewById(R.id.viewdiscussionabout);
        postIt = (TextView) findViewById(R.id.postit);
        pic = (ImageView) findViewById(R.id.viewdiscussionpic);
        posts = (RecyclerView) findViewById(R.id.postlist);
        writePost = (EditText) findViewById(R.id.postindiscussion);
        discussionId = getIntent().getStringExtra("id");
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        postArray = new ArrayList<Post>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        posts.setHasFixedSize(true);
        posts.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        posts.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        m_ref.child("discussion").child(discussionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Discussion d = dataSnapshot.getValue(Discussion.class);
                creator.setText(d.getCreatorname());
                about.setText(d.getAbout());
                m_ref.child("user").child(d.getCreatorid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        try {
                            pic.setImageBitmap(decodeFromFirebaseBase64(image));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        populateList();

        postIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });


    }

    public void populateList() {

        m_ref.child("discussion").child(discussionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.removeAllViews();
                postArray.clear();
                counter = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().startsWith("post")) {
                        counter++;
                        m_ref.child("discussion").child(discussionId).child("post" + String.valueOf(counter)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Post post = dataSnapshot.getValue(Post.class);
                                postArray.add(post);
                                Log.d("PELLE", post.getPost());
                                m_adapter = new PostListAdapter(ViewDiscussion.this, postArray, ViewDiscussion.this);
                                posts.setAdapter(m_adapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                final Handler handler = new Handler();
                final int delay = 1000; //milliseconds

                handler.postDelayed(new Runnable(){
                    public void run(){
                        if(!postArray.isEmpty())//checking if the data is loaded or not
                        {
                            Log.d("SIZE:", String.valueOf(postArray.size()));

                        }
                        else
                            handler.postDelayed(this, delay);
                    }
                }, delay);

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

    public void postComment(){
        if(writePost.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+)*$")){
            m_ref.child("discussion").child(discussionId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int counter = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().startsWith("post")) {
                            counter++;
                        }
                    }
                    m_ref.child("discussion").child(discussionId).child("post" + String.valueOf(counter + 1)).child("post").setValue(writePost.getText().toString());
                    m_ref.child("discussion").child(discussionId).child("post" + String.valueOf(counter + 1)).child("postuser").setValue(m_user.getUid());
                    writePost.setText("");
                    populateList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(ViewDiscussion.this, "Please enter a post. No extra spaces allowed.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onPostClick(int position) {

    }
}
