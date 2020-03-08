package com1032.cw2.sk00763.improov;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReadyToPayActivity extends AppCompatActivity {
    private TextView program = null;
    private TextView paragraph = null;
    private TextView instructions = null;
    private ImageView image = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private CardView pay =  null;
    private String paymentAmount = null;
    private static final int PAYPAL_REQUEST_CODE = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_to_pay);

        program = findViewById(R.id.payname);
        paragraph = findViewById(R.id.payparagraph);
        instructions = findViewById(R.id.payinstruct);
        image = findViewById(R.id.payimage);
        pay = findViewById(R.id.pay);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        program.setText(getIntent().getStringExtra("program"));
        paragraph.setText("Your request for " + program.getText().toString() + " has been accepted! " +
        "The session will take place at " + getIntent().getStringExtra("hour") + " on " + getIntent().getStringExtra("date") +
        " make sure to be online!");
        instructions.setText("In order for the chat to be unlocked with your coach, you must first pay the full amount. Make sure to do that at least 2 days" +
        " before the session.");

        m_ref.child("user").child(getIntent().getStringExtra("from")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String picture = user.getImage();

                try {
                    image.setImageBitmap(decodeFromFirebaseBase64(picture));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void getPayment(){
        paymentAmount = getIntent().getStringExtra("topay");
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "GBP", "Paying coach",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        JSONObject jsonObject = new JSONObject(confirm.toJSONObject().toString());
                        String paymentDetails = jsonObject.getJSONObject("response").getString("state");

                        if(paymentDetails.matches("approved")){
                            String paymentid = RandomNumber.generateUID();
                            m_ref.child("payments").child(paymentid).child("customer").setValue(m_user.getUid());
                            m_ref.child("payments").child(paymentid).child("coach").setValue(getIntent().getStringExtra("from"));
                            m_ref.child("payments").child(paymentid).child("howmuch").setValue(paymentAmount);
                            m_ref.child("payments").child(paymentid).child("coachpaid").setValue("no");
                            m_ref.child("user").child(getIntent().getStringExtra("from")).child("payments").child(paymentid).setValue(true);
                            m_ref.child("user").child(m_user.getUid()).child("notification").child(getIntent().getStringExtra("notification")).child("pending").setValue("no");
                            m_ref.child("session").child(getIntent().getStringExtra("session")).child("paid").setValue("yes");

                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            Date currentTime = Calendar.getInstance().getTime();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("sender", getIntent().getStringExtra("from"));
                            hashMap.put("receiver", m_user.getUid());
                            hashMap.put("message", "This is an automated reply. I have accepted your request for a session " +
                                    "for " + "'" + getIntent().getStringExtra("program") + "'" + " on the " + getIntent().getStringExtra("date") +
                                    " and we will be in touch shortly!");
                            hashMap.put("time", String.valueOf(hour) + ":" + String.valueOf(minute));
                            hashMap.put("date", currentTime.toString());
                            hashMap.put("senderName", getIntent().getStringExtra("from"));

                            m_ref.child("message").push().setValue(hashMap);
                        }
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, MenuContainer.class));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
}
