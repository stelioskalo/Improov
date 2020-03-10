package com1032.cw2.sk00763.improov;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RequestHourSession extends Activity {
    private EditText date = null;
    private EditText time = null;
    private Button request = null;
    Date requestDate = null;
    private FirebaseAuth m_suth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    final Date currentDate = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_hour_session);
        date = (EditText) findViewById(R.id.selectdate);
        time = (EditText) findViewById(R.id.selecthour);
        request = (Button) findViewById(R.id.requestsession);

        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        date.setKeyListener(null);
        time.setKeyListener(null);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d("tifkalli", myCalendar.getTime().toString());
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                requestDate = myCalendar.getTime();
                date.setText(sdf.format(myCalendar.getTime()));
            }

        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RequestHourSession.this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RequestHourSession.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("for").matches("first")) {
                    requestSession();
                } else if (getIntent().getStringExtra("for").matches("change")) {
                    request.setText("Request change");
                    requestChange();
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));


    }

    public void requestSession() {
        if (!requestDate.before(currentDate) && !time.getText().toString().matches("")) {
            if (getIntent().getStringExtra("type").matches("hourSession")) {
                Toast.makeText(RequestHourSession.this, "Session Requested!", Toast.LENGTH_LONG)
                        .show();
                m_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String notificationId = RandomNumber.generateUID();
                        String notificationId2 = RandomNumber.generateUID();
                        String coachId = getIntent().getStringExtra("coachid");
                        String topay = getIntent().getStringExtra("hourpay");

                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("from").setValue(m_user.getUid());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("type").setValue("requestForSession1hr");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("date").setValue(requestDate.toString());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("hour").setValue(time.getText().toString());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("program").setValue(getIntent().getStringExtra("programname"));
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("dateofrequest").setValue(currentDate.toString());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("notificationId").setValue(notificationId);
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("programid").setValue(getIntent().getStringExtra("programid"));
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("howlong").setValue("hour");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("pending").setValue("yes");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("topay").setValue(topay);
                        if(getIntent().getStringExtra("free").matches("yes")){
                            m_ref.child("user").child(coachId).child("notification").child(notificationId).child("free").setValue("yes");
                        }
                        else {
                            m_ref.child("user").child(coachId).child("notification").child(notificationId).child("free").setValue("no");
                        }
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("session").setValue("");

                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("type").setValue("requestedSession");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("from").setValue(coachId);
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("date").setValue(requestDate.toString());
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("hour").setValue(time.getText().toString());
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("program").setValue(getIntent().getStringExtra("programname"));
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("dateofrequest").setValue(currentDate.toString());
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("notificationId").setValue(notificationId2);
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("programid").setValue(getIntent().getStringExtra("programid"));
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("pending").setValue("yes");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("howlong").setValue("hour");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("topay").setValue(topay);
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("session").setValue("");
                        if(getIntent().getStringExtra("free").matches("yes")){
                            m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("free").setValue("yes");
                        }
                        else {
                            m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("free").setValue("no");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            } else {
                Toast.makeText(RequestHourSession.this, "Session Requested!", Toast.LENGTH_LONG)
                        .show();
                m_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String notificationId = RandomNumber.generateUID();
                        String notificationId2 = RandomNumber.generateUID();
                        String coachId = getIntent().getStringExtra("coachid");
                        String topay = getIntent().getStringExtra("monthpay");

                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("from").setValue(m_user.getUid());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("type").setValue("requestForMonthSession");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("date").setValue(requestDate.toString());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("hour").setValue(time.getText().toString());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("program").setValue(getIntent().getStringExtra("programname"));
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("dateofrequest").setValue(currentDate.toString());
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("notificationId").setValue(notificationId);
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("programid").setValue(getIntent().getStringExtra("programid"));
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("pending").setValue("yes");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("topay").setValue(topay);
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("session").setValue("");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("howlong").setValue("month");
                        m_ref.child("user").child(coachId).child("notification").child(notificationId).child("free").setValue("no");


                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("type").setValue("requestedSession");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("from").setValue(coachId);
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("date").setValue(requestDate.toString());
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("hour").setValue(time.getText().toString());
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("program").setValue(getIntent().getStringExtra("programname"));
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("dateofrequest").setValue(currentDate.toString());
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("programid").setValue(getIntent().getStringExtra("programid"));
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId).child("howlong").setValue("month");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("pending").setValue("yes");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("topay").setValue(topay);
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("session").setValue("");
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("notificationId").setValue(notificationId2);
                        m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("free").setValue("no");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        } else {
            Toast.makeText(RequestHourSession.this, "Please enter a valid date and time", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void requestChange() {
        if (!requestDate.before(currentDate) && !time.getText().toString().matches("")) {
            Toast.makeText(RequestHourSession.this, "Change Requested!", Toast.LENGTH_LONG)
                    .show();
            m_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String notificationId = RandomNumber.generateUID();
                    String notificationId2 = RandomNumber.generateUID();
                    String to = getIntent().getStringExtra("to");

                    m_ref.child("user").child(to).child("notification").child(notificationId).child("from").setValue(m_user.getUid());
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("type").setValue("requestForChange");
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("date").setValue(requestDate.toString());
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("hour").setValue(time.getText().toString());
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("program").setValue("");
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("dateofrequest").setValue(currentDate.toString());
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("notificationId").setValue(notificationId);
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("programid").setValue(getIntent().getStringExtra("programid"));
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("howlong").setValue("");
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("pending").setValue("yes");
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("topay").setValue("");
                    m_ref.child("user").child(to).child("notification").child(notificationId).child("session").setValue(getIntent().getStringExtra("session"));

                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("type").setValue("requestedChange");
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("from").setValue(to);
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("date").setValue(requestDate.toString());
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("hour").setValue(time.getText().toString());
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("program").setValue("");
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("dateofrequest").setValue(currentDate.toString());
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("notificationId").setValue(notificationId2);
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("programid").setValue(getIntent().getStringExtra("programid"));
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("pending").setValue("yes");
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("topay").setValue("");
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("howlong").setValue("");
                    m_ref.child("user").child(m_user.getUid()).child("notification").child(notificationId2).child("session").setValue(getIntent().getStringExtra("session"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            finish();
        }
    }


}
