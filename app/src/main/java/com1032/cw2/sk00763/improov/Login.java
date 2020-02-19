package com1032.cw2.sk00763.improov;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {

    //email field
    private EditText m_email = null;
    //password field
    private EditText m_pass = null;
    //login button
    private Button m_login = null;
    //sign up text
    private Button m_signup = null;
    // firebase auth
    private FirebaseAuth m_auth = null;
    // loading dialog
    private ProgressDialog m_dialog = null;
    // current user logged in
    private FirebaseUser m_user = null;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private String firsttime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        //inflate the widgets
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        m_email = (EditText) findViewById(R.id.email_edit_login);
        m_pass = (EditText) findViewById(R.id.password_edit_login);
        m_login = (Button) findViewById(R.id.btn_login);
        m_signup = (Button) findViewById(R.id.btn_register);
        // get instance of firebase auth
        m_auth = FirebaseAuth.getInstance();
        // get current logged in user
        m_user = m_auth.getCurrentUser();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        //Necessary permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
        }

        checkIflogedin();
    }

    private void checkIflogedin(){
        //check if user is not logged in already
        if (m_user == null) {
            // attempt to sign in when login button is clicked
            m_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check to see if fields are not empty
                    if(getEmail().isEmpty() || getPassword().isEmpty()){
                        //display toast if they are
                        Toasty.warning(Login.this, "Some field(s) are empty!", Toast.LENGTH_SHORT).show();
                    }else{
                        //check if user is already logged in
                        if(m_user != null){
                            //if so, redirect to home
                            Intent i = new Intent(Login.this, MenuContainer.class);
                            startActivity(i);
                            finish();

                        }else {
                            //if not, try to login
                            login();
                        }
                    }
                }
            });

            //on click action for sign up text
            m_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to the register activity
                    finish();
                    Intent i = new Intent(Login.this, Register.class);
                    startActivity(i);
                }
            });
        } // else log in the user straight away
        else {
            //if so, redirect to home
            Intent i = new Intent(Login.this,MenuContainer.class);
            startActivity(i);
        }
    }

    /**
     * Method to login a user
     */
    private void login(){
        //display dialog
        m_dialog = new ProgressDialog(Login.this);
        m_dialog.setMessage("Logging in");
        m_dialog.show();
        // log into the app with the credentials provided
        m_auth.signInWithEmailAndPassword(getEmail(), getPassword()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            //display error, if any
                            m_dialog.dismiss();
                            Toasty.warning(Login.this, "A problem occurred!", Toast.LENGTH_SHORT).show();
                        }else{
                            // if successful, go to home activity
                            m_dialog.dismiss();
                            Toasty.success(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Intent i = new Intent(Login.this, MenuContainer.class);
                            startActivity(i);
                            finish();

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = m_auth.getCurrentUser();
        if(currentUser != null) {
            updateUI();
        }
    }

    private void updateUI() {
        Toast.makeText(Login.this,"Logged in",Toast.LENGTH_LONG).show();
        Intent i = new Intent(Login.this,MenuContainer.class);
        startActivity(i);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        m_auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Facebook", "signInWithCredential:success");
                            FirebaseUser user = m_auth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });


    }

    /**
     * Method to get the email entered
     * @return: email
     */
    private String getEmail(){
        return m_email.getText().toString();
    }

    /**
     * Method to get the password entered
     * @return: password
     */
    private String getPassword(){
        return m_pass.getText().toString();
    }
}

