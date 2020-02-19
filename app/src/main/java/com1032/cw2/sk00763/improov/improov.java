package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 11/10/2019.
 */

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class improov extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}