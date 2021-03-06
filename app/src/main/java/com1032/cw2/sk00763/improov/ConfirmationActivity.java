package com1032.cw2.sk00763.improov;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ConfirmationActivity extends Activity {
    private TextView confirm = null;
    private TextView reject = null;
    private TextView done = null;
    private TextView message = null;
    private ProgressDialog progressDialog;
    private DatabaseReference m_ref = null;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        confirm = findViewById(R.id.confirmemail);
        reject = findViewById(R.id.rejectemail);
        done = findViewById(R.id.donepayout);
        message = findViewById(R.id.confirmation);
        final String forwhat = getIntent().getStringExtra("for");

        m_ref = FirebaseDatabase.getInstance().getReference();

        if(forwhat.matches("cancel")){
            message.setText("Are you sure you want to cancel your session?");
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(forwhat.matches("retrieve")){
                    payoutRequest();
                }
                else if(forwhat.matches("cancel")){
                    cancelSession();
                }
                done.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.3));

    }

    public void payoutRequest(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing your payout");
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final OkHttpClient client = new OkHttpClient();
        JSONObject postData = new JSONObject();
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        final FirebaseUser m_user = m_auth.getCurrentUser();
        Log.d("userid", m_user.getUid().toString());

        try {
            postData.put("uid", m_user.getUid().toString());
            postData.put("email", getIntent().getStringExtra("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postData.toString());

        final Request request = new Request.Builder().url("https://us-central1-improov-6972e.cloudfunctions.net/payout")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", "Your Token")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                if(response.isSuccessful()){
                    switch(responseCode){
                        case 200:
                            m_ref.child("payments").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    double total = 0;
                                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                        Payments payment = ds.getValue(Payments.class);
                                        if(payment.getCoachpaid().matches("no") && payment.getCoach().matches(m_user.getUid())){
                                            m_ref.child("payments").child(ds.getKey()).child("coachpaid").setValue("yes");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            break;
                        case 500:
                            Snackbar.make(findViewById(R.id.confirm), "Could not complete payout", Snackbar.LENGTH_LONG).show();
                            break;
                        default:
                            Snackbar.make(findViewById(R.id.confirm), "Could not complete payout", Snackbar.LENGTH_LONG).show();
                            break;
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    public void cancelSession(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        Date currentTime = Calendar.getInstance().getTime();

        reference.child("session").child(getIntent().getStringExtra("session")).removeValue();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("date", getIntent().getStringExtra("date"));
        hashMap.put("dateofrequest", currentTime.toString());
        hashMap.put("from", getIntent().getStringExtra("user"));
        hashMap.put("hour", String.valueOf(hour) + ":" + String.valueOf(minute));
        hashMap.put("howlong", getIntent().getStringExtra("howlong"));
        hashMap.put("pending", "");
        hashMap.put("program", "");
        hashMap.put("programid", getIntent().getStringExtra("program"));
        hashMap.put("session", "");
        hashMap.put("topay", "");
        hashMap.put("type", "cancelSession");
        hashMap.put("notificationId", "");

        reference.child("user").child(getIntent().getStringExtra("user")).child("notification").push().setValue(hashMap);
    }

}
