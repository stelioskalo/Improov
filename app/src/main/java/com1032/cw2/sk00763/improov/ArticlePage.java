package com1032.cw2.sk00763.improov;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;


public class ArticlePage extends Fragment implements ArticleListAdapter.ArticleActionListener {
    private FirebaseUser m_user = null;
    private FirebaseAuth m_auth = null;
    private DatabaseReference m_ref = null;
    private View m_View = null;
    private EditText search = null;
    private ImageView searchpic = null;
    private ImageView profile = null;
    private RecyclerView articleList = null;
    private List<Article> articles = null;
    private ArticleListAdapter m_adapter = null;

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
        articleList = (RecyclerView) m_View.findViewById(R.id.articlelist);
        articles = new ArrayList<Article>();
        m_ref = FirebaseDatabase.getInstance().getReference();
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

        articleList.setHasFixedSize(true);
        articleList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
        articleList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(getActivity(), ViewProfile.class);
              startActivity(i);
            }
        });

        populateArticles("", 1);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    articleList.removeAllViews();
                    populateArticles(editable.toString(), 2);
                } else {
                    articleList.removeAllViews();
                    populateArticles("", 1);
                }
            }
        });

        return m_View;
    }

    public void populateArticles(final String searchtext, final int searchtype){
        articles.clear();
        articleList.removeAllViews();
        final String lowerCaseSearch = searchtext.toLowerCase();
        m_ref.child("user").child(m_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articles.clear();
                articleList.removeAllViews();
                final User user = dataSnapshot.getValue(User.class);
                m_ref.child("article").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            final Article article = ds.getValue(Article.class);
                            if (article.getCategory().matches(user.getTopic1()) || article.getCategory().matches(user.getTopic2()) || article.getCategory().matches(user.getTopic3())
                                    || article.getCategory().matches(user.getTopic4()) || article.getCategory().matches(user.getTopic5())) {
                                if(searchtype == 2 && article.getTitle().toLowerCase().contains(lowerCaseSearch)) {
                                    articles.add(article);
                                }
                                else if(searchtype == 1) {
                                    articles.add(article);
                                }

                            }
                        }
                        m_adapter = new ArticleListAdapter(getActivity().getApplicationContext(), articles, ArticlePage.this);
                        articleList.setAdapter(m_adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onArticleClick(int position) {

    }
}
