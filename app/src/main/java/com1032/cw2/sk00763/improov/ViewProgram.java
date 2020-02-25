package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ViewProgram extends Activity {
    private TextView back = null;
    private ImageView coachpic = null;
    private TextView coachname = null;
    private TextView coachsurname = null;
    private TextView name = null;
    private TextView description = null;
    private TextView hourrate = null;
    private TextView monthrate = null;
    private Button hoursession = null;
    private Button monthaccess = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private String programId = null;
    private LinearLayout layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_program);
        back = (TextView) findViewById(R.id.backfromprogram);
        coachpic = (ImageView) findViewById(R.id.coachpic);
        coachname = (TextView) findViewById(R.id.coachnameprogram);
        coachsurname = (TextView) findViewById(R.id.coachsurnameprogram);
        name = (TextView) findViewById(R.id.viewprogramname);
        description = (TextView) findViewById(R.id.viewprogramdesc);
        hourrate = (TextView) findViewById(R.id.hourrate);
        monthrate = (TextView) findViewById(R.id.monthrate);
        hoursession = (Button) findViewById(R.id.requesthoursession);
        monthaccess = (Button) findViewById(R.id.requestmonthlyaccess);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        layout = (LinearLayout) findViewById(R.id.layout_view_program);

        programId = getIntent().getStringExtra("id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        coachpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewProgram.this, ViewCoach.class);
                startActivity(i);
            }
        });

        hoursession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewProgram.this, RequestHourSession.class);
                i.putExtra("coachid", getIntent().getStringExtra("coachid"));
                i.putExtra("programname", name.getText().toString());
                i.putExtra("programid", getIntent().getStringExtra("programid"));
                i.putExtra("type", "hourSession");
                i.putExtra("hourpay", getIntent().getStringExtra("hourpay"));
                startActivity(i);
                layout.setBackgroundColor(Color.parseColor("#A1A1A1"));
            }
        });

        monthaccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewProgram.this, RequestHourSession.class);
                i.putExtra("coachid", getIntent().getStringExtra("coachid"));
                i.putExtra("programname", name.getText().toString());
                i.putExtra("programid", getIntent().getStringExtra("programid"));
                i.putExtra("type", "monthSession");
                i.putExtra("monthpay", getIntent().getStringExtra("monthpay"));
                startActivity(i);
                layout.setBackgroundColor(Color.parseColor("#A1A1A1"));
            }
        });

        loaddata();
    }

    public void loaddata(){
        m_ref.child("program").child(programId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Program program = dataSnapshot.getValue(Program.class);

                m_ref.child("user").child(program.getCoach()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        coachname.setText(user.getName());
                        coachsurname.setText(user.getSurname());
                        if(user.getImage() != null){
                            try {
                                coachpic.setImageBitmap(decodeFromFirebaseBase64(user.getImage()));
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        name.setText(program.getName());
                        description.setText(program.getDescription());
                        hourrate.setText("£" + program.getHourRate());
                        monthrate.setText("£" + program.getMonthRate());
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

    @Override
    protected void onResume() {
        super.onResume();
        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

}
