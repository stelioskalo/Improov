package com1032.cw2.sk00763.improov;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewArticle extends Activity {
    private TextView title = null;
    private ImageView image = null;
    private CheckBox save = null;
    private TextView url = null;
    private ImageView back = null;
    private TextView p1 = null;
    private TextView p2 = null;
    private TextView p3 = null;
    private TextView p4 = null;
    private TextView p5 = null;
    private TextView p6 = null;
    private TextView p7 = null;
    private TextView p8 = null;
    private TextView p9 = null;
    private TextView p10 = null;
    private TextView p11 = null;
    private TextView p12 = null;
    private TextView p13 = null;
    private TextView p14 = null;
    private TextView p15 = null;
    private TextView p16 = null;
    private TextView p17 = null;
    private TextView p18 = null;
    private TextView p19 = null;
    private TextView p20 = null;
    private TextView p21 = null;
    private TextView p22 = null;
    private TextView p23 = null;
    private TextView p24 = null;
    private TextView p25 = null;
    private TextView p26 = null;
    private TextView p27 = null;
    private TextView p28 = null;
    private TextView p29 = null;
    private TextView p30 = null;
    private TextView p31 = null;
    private TextView p32 = null;
    private TextView p33 = null;
    private TextView p34 = null;
    private TextView p35 = null;
    private TextView p36 = null;
    private TextView p37 = null;
    private TextView p38 = null;
    private TextView p39 = null;
    private TextView p40 = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_article);

        title = findViewById(R.id.articlename);
        image = findViewById(R.id.viewarticleimage);
        save = findViewById(R.id.savearticle);
        url = findViewById(R.id.articleurl);
        back = findViewById(R.id.backfromviewarticle);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        p6 = findViewById(R.id.p6);
        p7 = findViewById(R.id.p7);
        p8 = findViewById(R.id.p8);
        p9 = findViewById(R.id.p9);
        p10 = findViewById(R.id.p10);
        p11 = findViewById(R.id.p11);
        p12 = findViewById(R.id.p12);
        p13 = findViewById(R.id.p13);
        p14 = findViewById(R.id.p14);
        p15 = findViewById(R.id.p15);
        p16 = findViewById(R.id.p16);
        p17 = findViewById(R.id.p17);
        p18 = findViewById(R.id.p18);
        p19 = findViewById(R.id.p19);
        p20 = findViewById(R.id.p20);
        p21 = findViewById(R.id.p21);
        p22 = findViewById(R.id.p22);
        p23 = findViewById(R.id.p23);
        p24 = findViewById(R.id.p24);
        p25 = findViewById(R.id.p25);
        p26 = findViewById(R.id.p26);
        p27 = findViewById(R.id.p27);
        p28 = findViewById(R.id.p28);
        p29 = findViewById(R.id.p29);
        p30 = findViewById(R.id.p30);
        p31 = findViewById(R.id.p31);
        p32 = findViewById(R.id.p32);
        p33 = findViewById(R.id.p33);
        p34 = findViewById(R.id.p34);
        p35 = findViewById(R.id.p35);
        p36 = findViewById(R.id.p36);
        p37 = findViewById(R.id.p37);
        p38 = findViewById(R.id.p38);
        p39 = findViewById(R.id.p39);
        p40 = findViewById(R.id.p40);

        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkIfSaved();

        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveArticle();
            }
        });

        populateView();

    }

    public void populateView(){
        title.setText(getIntent().getStringExtra("title"));
        url.setText(getIntent().getStringExtra("url"));
        String category = getIntent().getStringExtra("category");
        String imagenum = getIntent().getStringExtra("imagenum");

        String categoryname = null;
        if(category.contains(" ")){
            categoryname = category.replaceAll("\\s+","");
        }
        else {
            categoryname = category;
        }
        int id = getResources()
                .getIdentifier("com1032.cw2.sk00763.improov:drawable/" + categoryname.toLowerCase() + imagenum, null, null);
        image.setImageResource(id);

        if(!getIntent().getStringExtra("p1").matches("")){
            p1.setText(getIntent().getStringExtra("p1"));
            p1.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p2").matches("")){
            p2.setText(getIntent().getStringExtra("p2"));
            p2.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p3").matches("")){
            p3.setText(getIntent().getStringExtra("p3"));
            p3.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p4").matches("")){
            p4.setText(getIntent().getStringExtra("p4"));
            p4.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p5").matches("")){
            p5.setText(getIntent().getStringExtra("p5"));
            p5.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p6").matches("")){
            p6.setText(getIntent().getStringExtra("p6"));
            p6.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p7").matches("")){
            p7.setText(getIntent().getStringExtra("p7"));
            p7.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p8").matches("")){
            p8.setText(getIntent().getStringExtra("p8"));
            p8.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p9").matches("")){
            p9.setText(getIntent().getStringExtra("p9"));
            p9.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p10").matches("")){
            p10.setText(getIntent().getStringExtra("p10"));
            p10.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p11").matches("")){
            p11.setText(getIntent().getStringExtra("p11"));
            p11.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p12").matches("")){
            p12.setText(getIntent().getStringExtra("p12"));
            p12.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p12").matches("")){
            p12.setText(getIntent().getStringExtra("p12"));
            p12.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p13").matches("")){
            p13.setText(getIntent().getStringExtra("p13"));
            p13.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p14").matches("")){
            p14.setText(getIntent().getStringExtra("p14"));
            p14.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p15").matches("")){
            p15.setText(getIntent().getStringExtra("p15"));
            p15.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p16").matches("")){
            p16.setText(getIntent().getStringExtra("p16"));
            p16.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p17").matches("")){
            p17.setText(getIntent().getStringExtra("p17"));
            p17.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p18").matches("")){
            p18.setText(getIntent().getStringExtra("p18"));
            p18.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p19").matches("")){
            p19.setText(getIntent().getStringExtra("p19"));
            p19.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p20").matches("")){
            p20.setText(getIntent().getStringExtra("p20"));
            p20.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p21").matches("")){
            p21.setText(getIntent().getStringExtra("p21"));
            p21.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p22").matches("")){
            p22.setText(getIntent().getStringExtra("p22"));
            p22.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p23").matches("")){
            p23.setText(getIntent().getStringExtra("p23"));
            p23.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p24").matches("")){
            p24.setText(getIntent().getStringExtra("p24"));
            p24.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p25").matches("")){
            p25.setText(getIntent().getStringExtra("p25"));
            p25.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p26").matches("")){
            p26.setText(getIntent().getStringExtra("p26"));
            p26.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p27").matches("")){
            p27.setText(getIntent().getStringExtra("p27"));
            p27.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p28").matches("")){
            p28.setText(getIntent().getStringExtra("p28"));
            p28.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p29").matches("")){
            p29.setText(getIntent().getStringExtra("p29"));
            p29.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p30").matches("")){
            p30.setText(getIntent().getStringExtra("p30"));
            p30.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p31").matches("")){
            p31.setText(getIntent().getStringExtra("p31"));
            p31.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p32").matches("")){
            p32.setText(getIntent().getStringExtra("p32"));
            p32.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p33").matches("")){
            p33.setText(getIntent().getStringExtra("p33"));
            p33.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p34").matches("")){
            p34.setText(getIntent().getStringExtra("p34"));
            p34.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p35").matches("")){
            p35.setText(getIntent().getStringExtra("p35"));
            p35.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p36").matches("")){
            p36.setText(getIntent().getStringExtra("p36"));
            p36.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p37").matches("")){
            p37.setText(getIntent().getStringExtra("p37"));
            p37.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p38").matches("")){
            p38.setText(getIntent().getStringExtra("p38"));
            p38.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p39").matches("")){
            p39.setText(getIntent().getStringExtra("p39"));
            p39.setVisibility(View.VISIBLE);
        }
        if(!getIntent().getStringExtra("p40").matches("")){
            p40.setText(getIntent().getStringExtra("p40"));
            p40.setVisibility(View.VISIBLE);
        }

    }

    public void saveArticle(){
        if(save.isChecked()){
            m_ref.child("user").child(m_user.getUid()).child("saved articles").child(getIntent().getStringExtra("id")).child("article").setValue(getIntent().getStringExtra("id"));
        }
        else {
            m_ref.child("user").child(m_user.getUid()).child("saved articles").child(getIntent().getStringExtra("id")).removeValue();
        }
    }

    public void checkIfSaved(){
        m_ref.child("user").child(m_user.getUid()).child("saved articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(getIntent().getStringExtra("id"))){
                    save.setChecked(true);
                    Log.d("savedoeo", "nai");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
