package com1032.cw2.sk00763.improov;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ReportUser extends Activity {
    private EditText message = null;
    private TextView submit = null;
    private TextView cancel = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        message = findViewById(R.id.reportmessage);
        submit = findViewById(R.id.submitreport);
        cancel = findViewById(R.id.cancelreport);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
                finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));
    }

    public void report(){
        if(!message.getText().toString().matches("")){
            Date currentDate = Calendar.getInstance().getTime();
            String reportId = RandomNumber.generateUID();
            m_ref.child("report").child(reportId).child("from").setValue(m_user.getUid());
            m_ref.child("report").child(reportId).child("to").setValue(getIntent().getStringExtra("from"));
            m_ref.child("report").child(reportId).child("message").setValue(message.getText().toString());
            m_ref.child("report").child(reportId).child("date").setValue(currentDate.toString());
            Toasty.success(ReportUser.this, "Report has been sent.", Toast.LENGTH_LONG).show();
        }
        else {
            Toasty.error(ReportUser.this, "Please Enter a message.", Toast.LENGTH_LONG).show();
        }
    }

}
