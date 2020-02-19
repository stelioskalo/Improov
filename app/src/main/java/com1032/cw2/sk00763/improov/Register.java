package com1032.cw2.sk00763.improov;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class Register extends AppCompatActivity {

    //name field
    private EditText m_name = null;
    //email field
    private EditText m_email = null;
    //password field
    private EditText m_pass = null;
    //confirm password field
    private EditText m_cPass = null;
    //register button
    private EditText m_surname = null;
    private Button m_submit = null;
    //login button from register
    private Button m_login = null;
    // firebase auth
    private FirebaseAuth m_auth = null;
    //loading dialog
    private ProgressDialog m_dialog = null;
    //gdpr checkbox
    private CheckBox m_check = null;
    //regex to check email is only surrey email
    private static final String REGEX = "^[a-zA-Z0-9]*\\@[a-zA-Z0-9]*.com$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //inflate the widgets
        m_name = (EditText) findViewById(R.id.name_edit_register);
        m_email = (EditText) findViewById(R.id.email_edit_register);
        m_pass = (EditText) findViewById(R.id.password_edit_register);
        m_cPass = (EditText) findViewById(R.id.c_password_edit_register);
        m_submit = (Button) findViewById(R.id.btn_submit);
        m_login = (Button) findViewById(R.id.btn_login_from_reg);
        m_check = (CheckBox) findViewById(R.id.data_check);
        m_surname = (EditText) findViewById(R.id.surname_edit_register);

        //get firebase reference
        m_auth = FirebaseAuth.getInstance();

        //set onclick for sign up button
        m_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if fields are empty
                if(getName().isEmpty() || getSurname().isEmpty() || getEmail().isEmpty() || getPass().isEmpty() || getCPass().isEmpty()){
                    //if so, display message
                    Toasty.warning(Register.this, "Some field(s) are empty!", Toast.LENGTH_SHORT).show();
                } else if (!m_check.isChecked()) {
                    Toasty.warning(Register.this, "You need to accept the terms to create an account!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //if fields are present, try to register
                    register();
                }
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //set on click for login from register button
        m_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to login
                finish();
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
    }

    /**
     * Method to register a new account with firebase
     *
     * @return: true if registered, false otherwise
     */
    private void register(){
        this.m_dialog = new ProgressDialog(Register.this);
        this.m_dialog.setMessage("Registering Account");
        this.m_dialog.show();

        // check to see if password meets the firebase requirements of at least 6 characters long
        if(getPass().length() >= 6) {
            // check if the passwords match
            if (matchPass()) {
                //check if the email is valid
                if(validateEmail()) {

                    // if so, register an account
                    m_auth.createUserWithEmailAndPassword(getEmail(), getPass()).
                            addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // check to see if task is successful
                                    Log.d("IME DAME", "kkiaa");
                                    if (task.isSuccessful()) {
                                        //dismiss dialog
                                        m_dialog.dismiss();

                                        //add details to firebase
                                        addToFirebase();
                                        //sign out user
                                        m_auth.signOut();
                                        //display message of success
                                        Toasty.success(Register.this, "Account Registered!", Toast.LENGTH_SHORT).show();
                                        //redirect to login page
                                        Intent i = new Intent(Register.this, Login.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        //if task fails, stop dialog and display error message
                                        m_dialog.dismiss();
                                        Toasty.error(Register.this, "Task failed!" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    m_dialog.dismiss();
                    Toasty.warning(Register.this, "Email is not valid!", Toast.LENGTH_SHORT).show();
                }
            } else {
                //display error if passwords do not match
                m_dialog.dismiss();
                Toasty.warning(Register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        }else{
            //display error if password is less than 6 characters long
            m_dialog.dismiss();
            Toasty.warning(Register.this, "Password should be at least 6 characters long!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEmail() {
        return this.getEmail().matches(REGEX);
    }

    /**
     * Method to add user details to firebase
     */
    private void addToFirebase(){
        // get db reference
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // get current user who registered
        final FirebaseUser fUser = m_auth.getCurrentUser();
        // add the email of user
        reference.child("user").child(fUser.getUid()).child("email").setValue(fUser.getEmail());

        // when the email is added, add the other relevant info
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add the name, age, id, default events_joined and default events_hosted to the user
                String firstLetter = String.valueOf(getName().charAt(0)).toUpperCase();
                String firstname = firstLetter + getName().substring(1).toLowerCase();
                String firstLetter2 = String.valueOf(getSurname().charAt(0)).toUpperCase();
                String lastname = firstLetter2 + getSurname().substring(1).toLowerCase();
                Log.d("FIRSTNAME", firstname);
                reference.child("user").child(fUser.getUid()).child("name").setValue(firstname);
                reference.child("user").child(fUser.getUid()).child("id").setValue(fUser.getUid());
                reference.child("user").child(fUser.getUid()).child("surname").setValue(lastname);
                reference.child("user").child(fUser.getUid()).child("firsttime").setValue("yes");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //display error if any occur
                Toasty.error(Register.this, "Error" +  databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Getter method for name
     * @return: name
     */
    private String getName(){ return this.m_name.getText().toString(); }

    private String getSurname(){
        return this.m_surname.getText().toString();
    }


    /**
     * Getter method for email
     * @return: email
     */
    private String getEmail(){ return this.m_email.getText().toString(); }

    /**
     * Getter method for password
     * @return: password
     */
    private String getPass(){ return this.m_pass.getText().toString(); }

    /**
     * Getter method for confirm password
     * @return: confirm password
     */
    private String getCPass(){ return this.m_cPass.getText().toString(); }

    /**
     * Method to check if password fields match
     * @return: true if match, false otherwise
     */
    private boolean matchPass(){
        return this.getPass().equals(this.getCPass());
    }

    /**
     * Method to check if email matches REGEX i.e. only surrey email
     * @return: true if match, false otherwise
     */


}
