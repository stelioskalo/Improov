package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InterestSelection extends AppCompatActivity {
    private FirebaseAuth m_auth = null;
    ImageView accounting = null;
    ImageView advertising = null;
    ImageView audit = null;
    ImageView banking = null;
    ImageView law = null;
    ImageView ecommerce = null;
    ImageView entrepreneurship = null;
    ImageView ethics = null;
    ImageView finance = null;
    ImageView hr = null;
    ImageView insurance = null;
    ImageView investing = null;
    ImageView logistics = null;
    ImageView marketing = null;
    ImageView negotiation = null;
    ImageView realestate = null;
    ImageView records = null;
    ImageView sales = null;
    ImageView startup = null;
    ImageView tech = null;
    ImageView trading = null;
    ImageView writing = null;
    RelativeLayout footer = null;
    CardView caccounting = null;
    CardView cadvertising = null;
    CardView caudit = null;
    CardView cbanking = null;
    CardView claw = null;
    CardView cecommerce = null;
    CardView centrepreneurship = null;
    CardView cethics = null;
    CardView cfinance = null;
    CardView chr = null;
    CardView cinsurance = null;
    CardView cinvesting = null;
    CardView clogistics = null;
    CardView cmarketing = null;
    CardView cnegotiation = null;
    CardView crealestate = null;
    CardView crecords = null;
    CardView csales = null;
    CardView cstartup = null;
    CardView ctech = null;
    CardView ctrading = null;
    CardView cwriting = null;
    TextView noOfTopics = null;
    int currentnooftopics = 0;
    List<String> topics = null;
    List<String> topicspics = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selection);
        m_auth = FirebaseAuth.getInstance();
        noOfTopics = findViewById(R.id.nooftopics);
        footer = findViewById(R.id.footer_next);
        accounting = findViewById(R.id.accounting);
        advertising = findViewById(R.id.advertising);
        audit = findViewById(R.id.audit);
        banking = findViewById(R.id.banking);
        law = findViewById(R.id.law);
        ecommerce = findViewById(R.id.ecomm);
        entrepreneurship = findViewById(R.id.entrepreneurship);
        ethics = findViewById(R.id.ethics);
        finance = findViewById(R.id.finance);
        hr = findViewById(R.id.hr);
        insurance = findViewById(R.id.insurance);
        investing = findViewById(R.id.investing);
        logistics = findViewById(R.id.logistics);
        marketing = findViewById(R.id.marketing);
        negotiation = findViewById(R.id.negotiation);
        realestate = findViewById(R.id.realestate);
        records = findViewById(R.id.records);
        sales = findViewById(R.id.sales);
        startup = findViewById(R.id.startup);
        tech = findViewById(R.id.tech);
        trading = findViewById(R.id.trading);
        writing = findViewById(R.id.writing);
        caccounting = findViewById(R.id.card_acc);
        cadvertising = findViewById(R.id.card_adv);
        caudit = findViewById(R.id.card_aud);
        cbanking = findViewById(R.id.card_banking);
        claw = findViewById(R.id.card_cor);
        cecommerce = findViewById(R.id.card_ecom);
        centrepreneurship = findViewById(R.id.card_entr);
        cethics = findViewById(R.id.card_eth);
        cfinance = findViewById(R.id.card_fin);
        chr = findViewById(R.id.card_hr);
        cinsurance = findViewById(R.id.card_insurance);
        cinvesting = findViewById(R.id.card_inv);
        clogistics = findViewById(R.id.card_log);
        cmarketing = findViewById(R.id.card_mark);
        cnegotiation = findViewById(R.id.card_neg);
        crealestate = findViewById(R.id.card_real);
        crecords = findViewById(R.id.card_rec);
        csales = findViewById(R.id.card_sale);
        cstartup = findViewById(R.id.card_start);
        ctech = findViewById(R.id.card_tech);
        ctrading = findViewById(R.id.card_trad);
        cwriting = findViewById(R.id.card_writ);
        topics = new ArrayList<String>();
        topicspics = new ArrayList<String>();
        setOnClickListeners();

        final Profile profile = Profile.getCurrentProfile();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // get current user who registered
        if(Profile.getCurrentProfile() != null) {
            final String userId = Profile.getCurrentProfile().getId();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseUser user = m_auth.getCurrentUser();
                    Log.d("KKKIAA", user.toString());
                    // add the name, age, id, default events_joined and default events_hosted to the user
                    reference.child("user").child(userId).child("email").setValue(user.getEmail());
                    reference.child("user").child(userId).child("name").setValue(profile.getFirstName());
                    reference.child("user").child(userId).child("name").setValue(profile.getLastName());
                    reference.child("user").child(userId).child("id").setValue(userId);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        // when the email is added, add the other relevant info


    }


    public void setOnClickListeners() {

        accounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.accounting);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Accounting")) {
                    caccounting.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Accounting");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Accounting")) {
                    topics.remove("Accounting");
                    topicspics.remove(imageEncoded);
                    caccounting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }



            }
        });

        advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.advertising);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Advertising")) {
                    cadvertising.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Advertising");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Advertising")){
                    topics.remove("Advertising");
                    topicspics.remove(imageEncoded);
                    cadvertising.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });

        audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.audit);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Audit")) {
                    caudit.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Audit");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Audit")){
                    topics.remove("Audit");
                    topicspics.remove(imageEncoded);
                    caudit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        banking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.banking);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("banking")) {
                    cbanking.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("banking");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("banking")){
                    topics.remove("banking");
                    topicspics.remove(imageEncoded);
                    cbanking.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.corporatelaw);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Corporate law")) {
                    claw.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Corporate law");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Corporate law")){
                    topics.remove("Corporate law");
                    topicspics.remove(imageEncoded);
                    claw.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        ecommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ecommerce);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Ecommerce")) {
                    cecommerce.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Ecommerce");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Ecommerce")){
                    topics.remove("Ecommerce");
                    topicspics.remove(imageEncoded);
                    cecommerce.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        entrepreneurship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.entrepreneurship);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Entrepreneurship")) {
                    centrepreneurship.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Entrepreneurship");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Entrepreneurship")){
                    topics.remove("Entrepreneurship");
                    topicspics.remove(imageEncoded);
                    centrepreneurship.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        ethics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ethics);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Business ethics")) {
                    cethics.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Business ethics");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Business ethics")){
                    topics.remove("Business ethics");
                    topicspics.remove(imageEncoded);
                    cethics.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.finance_img_i1140);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Finance")) {
                    cfinance.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Finance");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Finance")){
                    topics.remove("Finance");
                    topicspics.remove(imageEncoded);
                    cfinance.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.hr);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Human resource")) {
                    chr.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Human resource");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Human resource")){
                    topics.remove("Human resource");
                    topicspics.remove(imageEncoded);
                    chr.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.insurance);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Insurance")) {
                    cinsurance.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Insurance");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Insurance")){
                    topics.remove("Insurance");
                    topicspics.remove(imageEncoded);
                    cinsurance.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        investing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.investing);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Investing")) {
                    cinvesting.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Investing");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Investing")){
                    topics.remove("Investing");
                    topicspics.remove(imageEncoded);
                    cinvesting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.logistics);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Logistics")) {
                    clogistics.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Logistics");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Logistics")){
                    topics.remove("Logistics");
                    topicspics.remove(imageEncoded);
                    clogistics.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        marketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.marketing);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Marketing")) {
                    cmarketing.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Marketing");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Marketing")){
                    topics.remove("Marketing");
                    topicspics.remove(imageEncoded);
                    cmarketing.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        negotiation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.negotiation);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Negotiation")) {
                    cnegotiation.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Negotiation");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Negotiation")){
                    topics.remove("Negotiation");
                    topicspics.remove(imageEncoded);
                    cnegotiation.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        realestate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.realestate);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Real Estate")) {
                    crealestate.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Real Estate");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Real Estate")){
                    topics.remove("Real Estate");
                    topicspics.remove(imageEncoded);
                    crealestate.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.records);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Record keeping")) {
                    crecords.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Record keeping");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Record keeping")){
                    topics.remove("Record keeping");
                    topicspics.remove(imageEncoded);
                    crecords.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.sales);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Sales")) {
                    csales.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Sales");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Sales")){
                    topics.remove("Sales");
                    topicspics.remove(imageEncoded);
                    csales.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        startup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.startup);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Start up")) {
                    cstartup.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Start up");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Start up")){
                    topics.remove("Start up");
                    topicspics.remove(imageEncoded);
                    cstartup.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.tech);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Technology")) {
                    ctech.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Technology");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Technology")){
                    topics.remove("Technology");
                    topicspics.remove(imageEncoded);
                    ctech.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        trading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.trading);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Trading")) {
                    ctrading.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Trading");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Trading")){
                    topics.remove("Trading");
                    topicspics.remove(imageEncoded);
                    ctrading.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });
        writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.writing);
                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100,imgConverted);
                //save the image as a .png file
                String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                if (currentnooftopics != 5 && !topics.contains("Business writing")) {
                    cwriting.setCardBackgroundColor(Color.parseColor("#3253FB"));
                    currentnooftopics += 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) + 1;
                    noOfTopics.setText(String.valueOf(temp));
                    topics.add("Business writing");
                    topicspics.add(imageEncoded);
                    if(currentnooftopics == 5){
                        footer.setBackground(getResources().getDrawable(R.drawable.next));
                    }
                }
                else if (topics.contains("Business writing")){
                    topics.remove("Business writing");
                    topicspics.remove(imageEncoded);
                    cwriting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    currentnooftopics -= 1;
                    int temp = Integer.parseInt(noOfTopics.getText().toString()) - 1;
                    noOfTopics.setText(String.valueOf(temp));
                    footer.setBackground(getResources().getDrawable(R.drawable.nextshape));
                }
            }
        });

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentnooftopics == 5){
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    // get current user who registered
                    final FirebaseUser fUser = m_auth.getCurrentUser();

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(int i = 1; i < 6; i++){
                                reference.child("user").child(fUser.getUid()).child("topic" + String.valueOf(i)).setValue(topics.get(i-1));
                                reference.child("user").child(fUser.getUid()).child("topicpic" + String.valueOf(i)).setValue(topicspics.get(i-1));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if(getIntent().getStringExtra("from") != null){
                        finish();
                    }
                    else {
                        Intent i = new Intent(InterestSelection.this,CreateProfile.class);
                        startActivity(i);
                        finish();
                    }

                }
            }
        });

    }

}
