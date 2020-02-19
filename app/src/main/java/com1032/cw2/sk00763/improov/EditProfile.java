package com1032.cw2.sk00763.improov;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
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


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EditProfile extends Activity {

    private TextView back = null;
    private TextView save = null;
    private ImageView picture = null;
    private TextView name = null;
    private TextView surname = null;
    private EditText aboutme = null;
    private EditText qualifications = null;
    private TextView changetopics = null;
    private TextView changepicture = null;
    private ImageView topicpic1 = null;
    private ImageView topicpic2 = null;
    private ImageView topicpic3 = null;
    private ImageView topicpic4 = null;
    private ImageView topicpic5 = null;
    private TextView topic1 = null;
    private TextView topic2 = null;
    private TextView topic3 = null;
    private TextView topic4 = null;
    private TextView topic5 = null;
    private Bitmap m_Map = null;
    private Uri m_FileUri = null;
    private FirebaseAuth m_auth = null;
    private FirebaseUser m_user = null;
    private DatabaseReference m_ref = null;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back = findViewById(R.id.backfromeditprofile);
        save = findViewById(R.id.savefromeditprofile);
        picture = findViewById(R.id.editprofilepic);
        name = findViewById(R.id.editprofname);
        surname = findViewById(R.id.editprofsurname);
        aboutme = findViewById(R.id.editaboutme);
        qualifications = findViewById(R.id.editqualifications);
        changetopics = findViewById(R.id.changetopics);
        changepicture = findViewById(R.id.changephoto);
        topicpic1 = (ImageView) findViewById(R.id.edittopicpic1);
        topicpic2 = (ImageView) findViewById(R.id.edittopicpic2);
        topicpic3 = (ImageView) findViewById(R.id.edittopicpic3);
        topicpic4 = (ImageView) findViewById(R.id.edittopicpic4);
        topicpic5 = (ImageView) findViewById(R.id.edittopicpic5);
        topic1 = (TextView) findViewById(R.id.edittopic1);
        topic2 = (TextView) findViewById(R.id.edittopic2);
        topic3 = (TextView) findViewById(R.id.edittopic3);
        topic4 = (TextView) findViewById(R.id.edittopic4);
        topic5 = (TextView) findViewById(R.id.edittopic5);
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_ref = FirebaseDatabase.getInstance().getReference();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        checkPermissionsCamera();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
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

        changetopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProfile.this, InterestSelection.class);
                i.putExtra("from", "edit");
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata();
            }
        });

        loadData();

    }

    public void loadData() {
        m_ref.child("user").child(m_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                surname.setText(user.getSurname());
                topic1.setText(user.getTopic1());
                topic2.setText(user.getTopic2());
                topic3.setText(user.getTopic3());
                topic4.setText(user.getTopic4());
                topic5.setText(user.getTopic5());
                String profilepic = user.getImage();
                String topicpicture1 = user.getTopicpic1();
                String topicpicture2 = user.getTopicpic2();
                String topicpicture3 = user.getTopicpic3();
                String topicpicture4 = user.getTopicpic4();
                String topicpicture5 = user.getTopicpic5();
                try {
                    topicpic1.setImageBitmap(decodeFromFirebaseBase64(topicpicture1));
                    topicpic2.setImageBitmap(decodeFromFirebaseBase64(topicpicture2));
                    topicpic3.setImageBitmap(decodeFromFirebaseBase64(topicpicture3));
                    topicpic4.setImageBitmap(decodeFromFirebaseBase64(topicpicture4));
                    topicpic5.setImageBitmap(decodeFromFirebaseBase64(topicpicture5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(dataSnapshot.hasChild("bio")){
                    aboutme.setText(user.getBio());
                }
                if(dataSnapshot.hasChild("qualifications")){
                    qualifications.setText(user.getQualifications());
                }
                if(dataSnapshot.hasChild("image")){
                    try {
                        picture.setImageBitmap(decodeFromFirebaseBase64(profilepic));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void savedata(){
        m_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                m_ref.child("user").child(m_user.getUid()).child("topic1").setValue(topic1.getText().toString());
                m_ref.child("user").child(m_user.getUid()).child("topic2").setValue(topic2.getText().toString());
                m_ref.child("user").child(m_user.getUid()).child("topic3").setValue(topic3.getText().toString());
                m_ref.child("user").child(m_user.getUid()).child("topic4").setValue(topic4.getText().toString());
                m_ref.child("user").child(m_user.getUid()).child("topic5").setValue(topic5.getText().toString());

                if(aboutme != null && aboutme.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,400}$")) {
                    if(qualifications != null && qualifications.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,400}$")) {
                        m_ref.child("user").child(m_user.getUid()).child("bio").setValue(aboutme.getText().toString());
                        m_ref.child("user").child(m_user.getUid()).child("qualifications").setValue(qualifications.getText().toString());
                    }
                    else {
                        Toast.makeText(EditProfile.this, "Please remove extra whitespaces. Max 400 words", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else {
                    Toast.makeText(EditProfile.this, "Please remove extra whitespaces. Max 400 words", Toast.LENGTH_LONG)
                            .show();
                }

                if(picture != null){
                    ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();
                    if(m_Map != null) {
                        m_Map.compress(Bitmap.CompressFormat.JPEG, 100, imgConverted);
                        String image = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);
                        m_ref.child("user").child(m_user.getUid()).child("image").setValue(image);
                    }
                }
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }


    public void checkPermissionsCamera() {
        PackageManager packageManager = EditProfile.this.getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Checking the permissions for using the camera.
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {
                Toast.makeText(EditProfile.this, "This device does not have a camera.", Toast.LENGTH_LONG)
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
            picture.setImageBitmap(m_Map);
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            // If its from Gallery we get the picture as Uri.
            m_FileUri = data.getData();
            try {
                //Converting it as a BitMap.
                m_Map = MediaStore.Images.Media.getBitmap(getContentResolver(), m_FileUri);

                // Setting the picture.
                picture.setImageBitmap(m_Map);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
