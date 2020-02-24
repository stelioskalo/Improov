package com1032.cw2.sk00763.improov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ImageView;
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

public class ReadyToPayActivity extends AppCompatActivity {
    private TextView program = null;
    private TextView paragraph = null;
    private TextView instructions = null;
    private ImageView image = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_to_pay);

        program = findViewById(R.id.payname);
        paragraph = findViewById(R.id.payparagraph);
        instructions = findViewById(R.id.payinstruct);
        image = findViewById(R.id.payimage);
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

        m_ref.child("user").child(m_user.getUid()).child("notification").child(getIntent().getStringExtra("notification")).child("pending").setValue("no");

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

    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }
}
