package com1032.cw2.sk00763.improov;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateProfile extends AppCompatActivity {
    private FirebaseAuth m_auth = null;
    private FirebaseUser fuser = null;
    private DatabaseReference reference = null;
    ImageView profile = null;
    TextView name = null;
    TextView surname = null;
    EditText bio = null;
    TextView topic1 = null;
    TextView topic2 = null;
    TextView topic3 = null;
    TextView topic4 = null;
    TextView topic5 = null;
    TextView skip = null;
    TextView next = null;
    EditText qualifications = null;
    ImageView topicpic1 = null;
    ImageView topicpic2 = null;
    ImageView topicpic3 = null;
    ImageView topicpic4 = null;
    ImageView topicpic5 = null;
    private Uri m_FileUri = null;
    TextView addprofile = null;
    Bitmap m_Map = null;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        profile = findViewById(R.id.createprofilepic);
        name = findViewById(R.id.editprofname);
        surname = findViewById(R.id.editprofsurname);
        bio = findViewById(R.id.edit_bio);
        topic1 = findViewById(R.id.topic1);
        topic2 = findViewById(R.id.topic2);
        topic3 = findViewById(R.id.topic3);
        topic4 = findViewById(R.id.topic4);
        topic5 = findViewById(R.id.topic5);
        skip = findViewById(R.id.skipfromcreate);
        next = findViewById(R.id.nextfromcreate);
        qualifications = findViewById(R.id.edit_qualifications);
        topicpic1 = findViewById(R.id.topicpic1);
        topicpic2 = findViewById(R.id.topicpic2);
        topicpic3 = findViewById(R.id.topicpic3);
        topicpic4 = findViewById(R.id.topicpic4);
        topicpic5 = findViewById(R.id.topicpic5);
        addprofile = findViewById(R.id.addprofilepic);
        m_auth = FirebaseAuth.getInstance();
        fuser = m_auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        checkPermissionsCamera();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("user").getChildren()) {
                    if (ds.getKey().equals(fuser.getUid())){
                        User u = ds.getValue(User.class);
                        name.setText(u.getName());
                        surname.setText(u.getSurname());
                        topic1.setText(u.getTopic1());
                        topic2.setText(u.getTopic2());
                        topic3.setText(u.getTopic3());
                        topic4.setText(u.getTopic4());
                        topic5.setText(u.getTopic5());
                        String topicpicture1 = u.getTopicpic1();
                        String topicpicture2 = u.getTopicpic2();
                        String topicpicture3 = u.getTopicpic3();
                        String topicpicture4 = u.getTopicpic4();
                        String topicpicture5 = u.getTopicpic5();
                        try {
                        topicpic1.setImageBitmap(decodeFromFirebaseBase64(topicpicture1));
                        topicpic2.setImageBitmap(decodeFromFirebaseBase64(topicpicture2));
                        topicpic3.setImageBitmap(decodeFromFirebaseBase64(topicpicture3));
                        topicpic4.setImageBitmap(decodeFromFirebaseBase64(topicpicture4));
                        topicpic5.setImageBitmap(decodeFromFirebaseBase64(topicpicture5));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfile.this);
                builder.setTitle("Select an option:");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (items[i].equals("Camera")) {

                            // Calling the camera via intent.
                            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Starting Camera.
                            startActivityForResult(picIntent, 0);

                        } else if (items[i].equals("Gallery")) {
                            // Calling the gallery via intent.
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, 3);

                        } else if (items[i].equals("Cancel")) {
                            // Nothing happens.
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bio.getText().toString() != null && profile != null){
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(bio != null && bio.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,400}$")) {
                                if(qualifications != null && qualifications.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,400}$")) {
                                    reference.child("user").child(fuser.getUid()).child("bio").setValue(bio.getText().toString());
                                    reference.child("user").child(fuser.getUid()).child("qualifications").setValue(qualifications.getText().toString());
                                }
                                else {
                                    Toast.makeText(CreateProfile.this, "Please remove extra whitespaces. Max 400 words", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(CreateProfile.this, "Please remove extra whitespaces. Max 400 words", Toast.LENGTH_LONG)
                                        .show();
                            }

                            if(profile != null){
                                ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                                if(m_Map != null) {
                                    m_Map.compress(Bitmap.CompressFormat.JPEG, 100, imgConverted);
                                    String image = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                                    reference.child("user").child(fuser.getUid()).child("image").setValue(image);
                                }
                            }

                            Intent i = new Intent(CreateProfile.this, StudentOrCoach.class);
                            startActivity(i);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateProfile.this, StudentOrCoach.class);
                startActivity(i);
            }
        });

    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void checkPermissionsCamera() {
        PackageManager packageManager = CreateProfile.this.getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Checking the permissions for using the camera.
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {
                Toast.makeText(CreateProfile.this, "This device does not have a camera.", Toast.LENGTH_LONG)
                        .show();
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Checking if the result came from Camera or Gallery
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // If its from Camera we retrieve the image and save it to firebase
            m_Map = (Bitmap) data.getExtras().get("data");


            // Setting the picture.
            profile.setImageBitmap(m_Map);
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            // If its from Gallery we get the picture as Uri.
            m_FileUri = data.getData();
            try {
                //Converting it as a BitMap.
                m_Map = MediaStore.Images.Media.getBitmap(getContentResolver(), m_FileUri);

                // Setting the picture.
                profile.setImageBitmap(m_Map);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
