package com1032.cw2.sk00763.improov;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateDiscussion extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner = null;
    private String topic = null;
    private TextView cancel = null;
    private TextView save = null;
    private EditText about = null;
    private EditText post = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private String discussionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discussion);
        spinner = findViewById(R.id.topicpinner);
        cancel = (TextView) findViewById(R.id.canceldiscussion);
        save = (TextView) findViewById(R.id.savediscussion);
        about = (EditText) findViewById(R.id.discussionabout);
        post = (EditText) findViewById(R.id.post);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();

            }
        });

        setSpinner();

    }


    private void setSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.topics_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        //An item in spinner was selected
        topic = (String) adapterView.getItemAtPosition(pos);
        }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        }

    public void save(){
        discussionId = RandomNumber.generateUID();
        if(about.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,30}$")){
            if(post.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,400}$")){
                m_ref.child("discussion").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      m_ref.child("discussion").child(discussionId).child("about").setValue(about.getText().toString());
                      m_ref.child("discussion").child(discussionId).child("post1").child("post").setValue(post.getText().toString());
                      m_ref.child("discussion").child(discussionId).child("post1").child("postuser").setValue(m_user.getUid());
                      m_ref.child("discussion").child(discussionId).child("topic").setValue(topic);
                      m_ref.child("discussion").child(discussionId).child("creatorid").setValue(m_user.getUid());
                      m_ref.child("discussion").child(discussionId).child("discussionId").setValue(discussionId);
                      m_ref.child("user").child(m_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              User user = dataSnapshot.getValue(User.class);
                              m_ref.child("discussion").child(discussionId).child("creatorname").setValue(user.getName());
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
                finish();
            }
            else {
                Toast.makeText(CreateDiscussion.this, "Please enter a post. No extra spaces allowed. Only 400 words allowed ", Toast.LENGTH_LONG)
                        .show();
            }
        }
        else {
            Toast.makeText(CreateDiscussion.this, "Please enter what the discussion is about. No extra spaces allowed. Only 30 words allowed", Toast.LENGTH_LONG)
                    .show();
        }

    }

}
