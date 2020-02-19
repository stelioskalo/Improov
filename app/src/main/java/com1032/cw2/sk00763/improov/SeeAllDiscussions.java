package com1032.cw2.sk00763.improov;

import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeeAllDiscussions extends Activity implements DiscussionListAdapter.DiscussionActionListener {
    private TextView topicName = null;
    private RecyclerView seeAllList = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private TextView back = null;
    private DiscussionListAdapter m_adapter = null;
    private List<Discussion> discussions = null;
    private EditText search = null;
    private ImageView searchpic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_discussions);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        topicName = (TextView)findViewById(R.id.seeallname);
        seeAllList = (RecyclerView) findViewById(R.id.seealllist);
        searchpic = (ImageView) findViewById(R.id.searchpic);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        back = findViewById(R.id.backseeall);
        discussions = new ArrayList<Discussion>();
        search = (EditText)findViewById(R.id.searchdiscussion);
        topicName.setText(getIntent().getStringExtra("from"));

        seeAllList.setHasFixedSize(true);
        seeAllList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        seeAllList.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        populatelist("", 1);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    seeAllList.removeAllViews();
                    populatelist(editable.toString(), 2);
                } else {
                    seeAllList.removeAllViews();
                    populatelist("", 1);
                }
            }
        });


    }

    public void populatelist(String search, final int searchType){
        final String searchLowercase = search.toLowerCase();
        String userId = m_user.getUid();
        final String topic = getIntent().getStringExtra("from");
        m_ref.child("discussion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discussions.clear();
                seeAllList.removeAllViews();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Discussion d = ds.getValue(Discussion.class);
                    if(d.getTopic().toString().matches(topic)){
                        if(searchType == 2 && d.getAbout().toLowerCase().contains(searchLowercase)) {
                            discussions.add(d);
                        }
                        else if(searchType == 1) {
                            discussions.add(d);
                        }
                    }

                }

                m_adapter = new DiscussionListAdapter(SeeAllDiscussions.this, discussions, SeeAllDiscussions.this);
                seeAllList.setAdapter(m_adapter);
                Log.d("DAME", String.valueOf(discussions.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDiscussionClick(int position) {

    }
}
