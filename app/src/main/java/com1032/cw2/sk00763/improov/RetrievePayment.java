package com1032.cw2.sk00763.improov;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrievePayment extends Activity {
    private TextView balance = null;
    private EditText email = null;
    private CardView retrieve = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_payment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        balance = findViewById(R.id.retrievebalance);
        email = findViewById(R.id.retrieveemail);
        retrieve = findViewById(R.id.retrieve);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();


        m_ref.child("payments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Payments payment = ds.getValue(Payments.class);
                    Log.d("gamoto", payment.getCoachpaid().toString());
                    if(payment.getCoachpaid().matches("no") && payment.getCoach().matches(m_user.getUid())){
                        total += (Double.parseDouble(payment.getHowmuch()) * 0.90);

                    }
                }
                balance.setText(String.valueOf(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().matches("^[a-zA-Z0-9]*\\@[a-zA-Z0-9]*.com$")){
                    Toast.makeText(RetrievePayment.this, "Please enter your email", Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    Intent intent = new Intent(RetrievePayment.this, ConfirmationActivity.class);
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                }
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));
    }

}
