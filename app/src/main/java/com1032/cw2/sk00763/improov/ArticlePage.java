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
    private List<String> articleIds = null;

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
        articleIds = new ArrayList<String>();
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
                                    articleIds.add(ds.getKey());
                                }
                                else if(searchtype == 1) {
                                    articles.add(article);
                                    articleIds.add(ds.getKey());
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
        Intent i = new Intent(getActivity(), ViewArticle.class);
        i.putExtra("title", articles.get(position).getTitle());
        i.putExtra("category", articles.get(position).getCategory());
        i.putExtra("imagenum", articles.get(position).getImagenum());
        i.putExtra("url", articles.get(position).getUrl());
        i.putExtra("id", articleIds.get(position));
        i.putExtra("p1", articles.get(position).getParagraph1());
        i.putExtra("p2", articles.get(position).getParagraph2());
        i.putExtra("p3", articles.get(position).getParagraph3());
        i.putExtra("p4", articles.get(position).getParagraph4());
        i.putExtra("p5", articles.get(position).getParagraph5());
        i.putExtra("p6", articles.get(position).getParagraph6());
        i.putExtra("p7", articles.get(position).getParagraph7());
        i.putExtra("p8", articles.get(position).getParagraph8());
        i.putExtra("p9", articles.get(position).getParagraph9());
        i.putExtra("p10", articles.get(position).getParagraph10());
        i.putExtra("p11", articles.get(position).getParagraph11());
        i.putExtra("p12", articles.get(position).getParagraph12());
        i.putExtra("p13", articles.get(position).getParagraph13());
        i.putExtra("p14", articles.get(position).getParagraph14());
        i.putExtra("p15", articles.get(position).getParagraph15());
        i.putExtra("p16", articles.get(position).getParagraph16());
        i.putExtra("p17", articles.get(position).getParagraph17());
        i.putExtra("p18", articles.get(position).getParagraph18());
        i.putExtra("p19", articles.get(position).getParagraph19());
        i.putExtra("p20", articles.get(position).getParagraph20());
        i.putExtra("p21", articles.get(position).getParagraph21());
        i.putExtra("p22", articles.get(position).getParagraph22());
        i.putExtra("p23", articles.get(position).getParagraph23());
        i.putExtra("p24", articles.get(position).getParagraph24());
        i.putExtra("p25", articles.get(position).getParagraph25());
        i.putExtra("p26", articles.get(position).getParagraph26());
        i.putExtra("p27", articles.get(position).getParagraph27());
        i.putExtra("p28", articles.get(position).getParagraph28());
        i.putExtra("p29", articles.get(position).getParagraph29());
        i.putExtra("p30", articles.get(position).getParagraph30());
        i.putExtra("p31", articles.get(position).getParagraph31());
        i.putExtra("p32", articles.get(position).getParagraph32());
        i.putExtra("p33", articles.get(position).getParagraph33());
        i.putExtra("p34", articles.get(position).getParagraph34());
        i.putExtra("p35", articles.get(position).getParagraph35());
        i.putExtra("p36", articles.get(position).getParagraph36());
        i.putExtra("p37", articles.get(position).getParagraph37());
        i.putExtra("p38", articles.get(position).getParagraph38());
        i.putExtra("p39", articles.get(position).getParagraph39());
        i.putExtra("p40", articles.get(position).getParagraph40());
        startActivity(i);
    }
}
