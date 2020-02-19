package com1032.cw2.sk00763.improov;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;


public class DiscussionAndCoach extends Fragment implements DiscussionListAdapter.DiscussionActionListener, ProgramListAdapter.ProgramActionListener {

    private View m_View = null;
    private Button discussions = null;
    private Button coaches = null;
    private RecyclerView list1 = null;
    private TextView createDiscussion = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference reference = null;
    private DiscussionListAdapter m_adapter = null;
    private ProgramListAdapter m_adapter2 = null;
    List<Discussion> topicList = new ArrayList<Discussion>();
    List<Program> programList = null;
    private int counter1 = 0;
    private EditText search = null;
    private ImageView searchpic = null;


    public DiscussionAndCoach() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.fragment_discussion_and_coach, container, false);
        discussions = (Button) m_View.findViewById(R.id.finddiscussions);
        coaches = (Button) m_View.findViewById(R.id.findcoaches);
        list1 = (RecyclerView) m_View.findViewById(R.id.discussionlist1);
        createDiscussion = (TextView) m_View.findViewById(R.id.creatediscussion);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        search = (EditText)m_View.findViewById(R.id.searchdiscussion);
        searchpic = (ImageView)m_View.findViewById(R.id.searchpic);
        programList = new ArrayList<Program>();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        coaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("patoto","epatithike");
                coaches.setTextColor(Color.parseColor("#000000"));
                coaches.setTypeface(Typeface.DEFAULT_BOLD);
                discussions.setTypeface(Typeface.DEFAULT);
                discussions.setTextColor(Color.parseColor("#A1A1A1"));
                createDiscussion.setTextColor(Color.parseColor("#FFFFFF"));
                populateLists("", 1);
            }
        });

        discussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discussions.setTextColor(Color.parseColor("#000000"));
                discussions.setTypeface(Typeface.DEFAULT_BOLD);
                coaches.setTypeface(Typeface.DEFAULT);
                coaches.setTextColor(Color.parseColor("#A1A1A1"));
                createDiscussion.setTextColor(Color.parseColor("#3253F9"));
                populateLists("", 1);
            }
        });



        list1.setHasFixedSize(true);
        list1.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        list1.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        createDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EGKALA", String.valueOf(discussions.getCurrentTextColor()));
                if(discussions.getCurrentTextColor() == -16777216){
                    Intent i = new Intent(getActivity(), CreateDiscussion.class);
                    startActivity(i);

                }
            }
        });

        populateLists("", 1);

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
                    list1.removeAllViews();
                    populateLists(editable.toString(), 2);
                } else {
                    list1.removeAllViews();
                    populateLists("", 1);
                }
            }
        });

        return m_View;
    }

    public void populateLists(final String searchtext, final int searchtype){
        topicList.clear();
        programList.clear();
        list1.removeAllViews();
        final String lowerCaseSearch = searchtext.toLowerCase();
        if(discussions.getCurrentTextColor() == -16777216){
          reference.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topicList.clear();
                list1.removeAllViews();
                  final User user = dataSnapshot.getValue(User.class);
                  counter1 = 0;
                  reference.child("discussion").addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          for (DataSnapshot ds : dataSnapshot.getChildren()) {
                              final Discussion d = ds.getValue(Discussion.class);
                              if (d.getTopic().matches(user.getTopic1()) || d.getTopic().matches(user.getTopic2()) || d.getTopic().matches(user.getTopic3())
                                      || d.getTopic().matches(user.getTopic4()) || d.getTopic().matches(user.getTopic5())) {
                                  if(searchtype == 2 && d.getAbout().toLowerCase().contains(lowerCaseSearch)) {
                                      topicList.add(d);
                                  }
                                  else if(searchtype == 1) {
                                      topicList.add(d);
                                  }

                              }
                          }
                          Log.d("DAMESADA", String.valueOf(topicList.size()));
                          Collections.shuffle(topicList);
                          m_adapter = new DiscussionListAdapter(getActivity().getApplicationContext(), topicList, DiscussionAndCoach.this);
                          list1.setAdapter(m_adapter);
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
        }
        else if(coaches.getCurrentTextColor() == -16777216){
            reference.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    programList.clear();
                    list1.removeAllViews();
                    final User user = dataSnapshot.getValue(User.class);
                    reference.child("program").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            programList.clear();
                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                final Program program = ds.getValue(Program.class);
                                if(program.getTopic().matches(user.getTopic1()) || program.getTopic().matches(user.getTopic2()) || program.getTopic().matches(user.getTopic3()) ||
                                        program.getTopic().matches(user.getTopic4()) || program.getTopic().matches(user.getTopic5())) {
                                    if(!program.getCoach().matches(m_user.getUid())) {
                                        if (searchtype == 2 && program.getName().toLowerCase().contains(lowerCaseSearch)) {
                                            programList.add(program);
                                        } else if (searchtype == 1) {
                                            programList.add(program);
                                        }
                                    }

                                }
                                Log.d("SIZETOU", String.valueOf(programList.size()));
                            }
                            m_adapter2 = new ProgramListAdapter(getActivity().getApplicationContext(), programList, DiscussionAndCoach.this);
                            list1.setAdapter(m_adapter2);
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

        }

    }

    @Override
    public void onDiscussionClick(int position) {
        Intent i = new Intent(getActivity(), ViewDiscussion.class);
        i.putExtra("id", topicList.get(position).getDiscussionId());
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateLists("", 1);
    }

    @Override
    public void onProgramClick(int position) {
        Intent i  = new Intent(getActivity(), ViewProgram.class);
        i.putExtra("id", programList.get(position).getProgramId());
        i.putExtra("coachid", programList.get(position).getCoach());
        startActivity(i);
    }

}
