package com1032.cw2.sk00763.improov;
/**
 * Last edited by Stelios at 05/03/2019
 */

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The MenuContainer activity simply holds the layout of the menu.
 */
public class MenuContainer extends AppCompatActivity {

    FragmentTransaction m_Transaction;
    TextView m_hometext = null;


    /**
     * onCreate will set the view to menu_container layout
     * and begin a transaction between fragments and give functionality
     * to change the view of the relative layout when something is pressed
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_menu_container);
        m_hometext = (TextView) findViewById(R.id.hometext);


        ArticlePage articleFragment = new ArticlePage();
        m_Transaction = getFragmentManager().beginTransaction();
        m_hometext.setTextColor(Color.parseColor("#000000"));
        m_Transaction.replace(R.id.display, articleFragment);
        m_Transaction.commit();

    }
}
