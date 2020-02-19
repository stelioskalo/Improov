package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ArticlePage extends Fragment {
    private FirebaseUser m_user = null;
    private FirebaseAuth m_auth = null;
    private View m_View = null;
    private EditText search = null;
    private ImageView searchpic = null;
    private ImageView profile = null;

    public ArticlePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_auth = FirebaseAuth.getInstance();
        m_user = m_auth.getCurrentUser();
        m_View = inflater.inflate(R.layout.fragment_article_page, container, false);
        search = (EditText) m_View.findViewById(R.id.searcharticle);
        searchpic = (ImageView) m_View.findViewById(R.id.searchpicarticle);
        profile = (ImageView) m_View.findViewById(R.id.viewprofile);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("user").child(m_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getFirsttime().matches("yes")) {
                    Intent i = new Intent(getActivity(), InterestSelection.class);
                    startActivity(i);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(getActivity(), ViewProfile.class);
              startActivity(i);
            }
        });

        return m_View;
    }

}
