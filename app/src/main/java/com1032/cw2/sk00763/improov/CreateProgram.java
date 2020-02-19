package com1032.cw2.sk00763.improov;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class CreateProgram extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView skipOrCancel = null;
    private TextView next = null;
    private ImageView picture = null;
    private EditText name = null;
    private EditText desc = null;
    private Spinner spinner = null;
    private EditText hourrate = null;
    private EditText monthlyrate = null;
    private String topic = null;
    private Bitmap m_Map = null;
    private Bitmap m_final_map = null;
    private Uri m_FileUri = null;
    private String programId = null;
    private FirebaseAuth m_auth = null;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String programName = null;
    private String programDesc = null;
    private String hourRate = null;
    private String monthRate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        skipOrCancel = (TextView) findViewById(R.id.skiporcancel);
        next = (TextView) findViewById(R.id.createprogram);
        picture = (ImageView) findViewById(R.id.progampic);
        name = (EditText) findViewById(R.id.programname);
        desc = (EditText) findViewById(R.id.programdesc);
        spinner = (Spinner) findViewById(R.id.topicspinner);
        hourrate = (EditText) findViewById(R.id.programhourrate);
        monthlyrate = (EditText) findViewById(R.id.programmonthrate);
        m_auth = FirebaseAuth.getInstance();
        checkPermissionsCamera();
        setSpinner();

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateProgram.this);
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

        skipOrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                //get the current logged in user
                final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                ref.child("user").child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref.child("user").child(fUser.getUid()).child("firsttime").setValue("no");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(getIntent().getStringExtra("from").matches("profile")){
                    finish();
                }
                else {
                    Intent i = new Intent(CreateProgram.this, MenuContainer.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFirebase();
            }
        });
    }

    private void setSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.topics_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        //An item in spinner was selected
        topic = (String) adapterView.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Checking if the result came from Camera or Gallery
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // If its from Camera we retrieve the image and save it to firebase
            m_Map = (Bitmap) data.getExtras().get("data");
            if (m_Map.getWidth() >= m_Map.getHeight()){

                m_final_map = Bitmap.createBitmap(
                        m_Map,
                        m_Map.getWidth()/2 - m_Map.getHeight()/2,
                        0,
                        m_Map.getHeight(),
                        m_Map.getHeight()
                );

            }else{

                m_final_map = Bitmap.createBitmap(
                        m_Map,
                        0,
                        m_Map.getHeight()/2 - m_Map.getWidth()/2,
                        m_Map.getWidth(),
                        m_Map.getWidth()
                );
            }
            //BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
           // picture.setImageBitmap(Bitmap.createScaledBitmap(m_Map, 120, 120, false));

            // Setting the picture.
            picture.setImageBitmap(m_final_map);


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

    public void checkPermissionsCamera() {
        PackageManager packageManager = CreateProgram.this.getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Checking the permissions for using the camera.
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {
                Toast.makeText(CreateProgram.this, "This device does not have a camera.", Toast.LENGTH_LONG)
                        .show();
                return;
            }
        }
    }

    public void saveToFirebase(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //get the current logged in user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        final Bitmap img = ((BitmapDrawable) picture.getDrawable()).getBitmap();

        if(name.getText().toString().matches("^(.|\\s)*[a-zA-Z0-9]+(.|\\s){0,15}$")){
            if(desc.getText().toString().matches("^[^\\s]+(\\s+[^\\s]+){0,400}$")){
                if(spinner != null){
                    if(monthlyrate != null && hourrate != null){
                        if(picture != null){
                            programName = name.getText().toString();
                            programDesc = desc.getText().toString();
                            hourRate = hourrate.getText().toString();
                            monthRate = monthlyrate.getText().toString();
                            programId = RandomNumber.generateUID();

                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ByteArrayOutputStream imgConverted = new ByteArrayOutputStream();

                                    //save the image as a .jpg file
                                    img.compress(Bitmap.CompressFormat.JPEG, 100, imgConverted);

                                    String imageEncoded = Base64.encodeToString(imgConverted.toByteArray(), Base64.DEFAULT);

                                    ref.child("program").child(programId).child("name").setValue(programName);
                                    ref.child("program").child(programId).child("description").setValue(programDesc);
                                    ref.child("program").child(programId).child("hourRate").setValue(hourRate);
                                    ref.child("program").child(programId).child("monthRate").setValue(monthRate);
                                    ref.child("program").child(programId).child("topic").setValue(topic);
                                    ref.child("program").child(programId).child("coach").setValue(fUser.getUid());
                                    ref.child("program").child(programId).child("programImage").setValue(imageEncoded);
                                    ref.child("user").child(fUser.getUid()).child("firsttime").setValue("no");
                                    ref.child("program").child(programId).child("programId").setValue(programId);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            if(getIntent().getStringExtra("from").matches("profile")){
                                finish();
                            }
                            else {
                                finish();
                                Intent i = new Intent(CreateProgram.this, MenuContainer.class);
                                startActivity(i);
                            }

                        }
                        else {
                            Toast.makeText(CreateProgram.this, "Please enter program picture.", Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                    else {
                        Toast.makeText(CreateProgram.this, "Please enter Hourly and Monthly rate.", Toast.LENGTH_LONG)
                                .show();
                    }

                }
                else {
                    Toast.makeText(CreateProgram.this, "Please select program topic.", Toast.LENGTH_LONG)
                            .show();
                }

            }
            else {
                Toast.makeText(CreateProgram.this, "Please enter description. No extra spaces allowed. Max 400 words", Toast.LENGTH_LONG)
                        .show();
            }

        }
        else {
            Toast.makeText(CreateProgram.this, "Please enter a valid name. Max 15 words", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
