package com1032.cw2.sk00763.improov;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Stelios on 06/12/2019.
 */

public class Navigation extends Fragment {

    ImageView m_home = null;
    ImageView m_team = null;
    ImageView m_message = null;
    ImageView m_award = null;
    ImageView m_alert = null;
    TextView m_hometext = null;
    TextView m_exploretext = null;
    TextView m_awardtext = null;
    TextView m_messagetext = null;
    TextView m_alerttext = null;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        m_home = (ImageView)view.findViewById(R.id.home);
        m_team = (ImageView)view.findViewById(R.id.team);
        m_message = (ImageView)view.findViewById(R.id.messages);
        m_award = (ImageView)view.findViewById(R.id.todo);
        m_alert = (ImageView)view.findViewById(R.id.nottifciation);
        m_hometext = (TextView) view.findViewById(R.id.hometext);
        m_exploretext = (TextView) view.findViewById(R.id.exploretext);
        m_awardtext = (TextView) view.findViewById(R.id.challengetext);
        m_messagetext = (TextView) view.findViewById(R.id.messagingtext);
        m_alerttext = (TextView) view.findViewById(R.id.nottificationtext);


        m_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_hometext.setTextColor(Color.parseColor("#000000"));
                m_exploretext.setTextColor(Color.parseColor("#A1A1A1"));
                m_awardtext.setTextColor(Color.parseColor("#A1A1A1"));
                m_messagetext.setTextColor(Color.parseColor("#A1A1A1"));
                m_alerttext.setTextColor(Color.parseColor("#A1A1A1"));
                getFragmentManager().beginTransaction().replace(R.id.display, new ArticlePage()).commit();
            }
        });
        m_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_hometext.setTextColor(Color.parseColor("#A1A1A1"));
                m_exploretext.setTextColor(Color.parseColor("#000000"));
                m_awardtext.setTextColor(Color.parseColor("#A1A1A1"));
                m_messagetext.setTextColor(Color.parseColor("#A1A1A1"));
                m_alerttext.setTextColor(Color.parseColor("#A1A1A1"));
                getFragmentManager().beginTransaction().replace(R.id.display, new DiscussionAndCoach()).commit();
            }
        });
        m_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_hometext.setTextColor(Color.parseColor("#A1A1A1"));
                m_exploretext.setTextColor(Color.parseColor("#A1A1A1"));
                m_awardtext.setTextColor(Color.parseColor("#A1A1A1"));
                m_messagetext.setTextColor(Color.parseColor("#000000"));
                m_alerttext.setTextColor(Color.parseColor("#A1A1A1"));
                getFragmentManager().beginTransaction().replace(R.id.display, new Messages()).commit();
            }
        });
        m_award.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_hometext.setTextColor(Color.parseColor("#A1A1A1"));
                m_exploretext.setTextColor(Color.parseColor("#A1A1A1"));
                m_awardtext.setTextColor(Color.parseColor("#000000"));
                m_messagetext.setTextColor(Color.parseColor("#A1A1A1"));
                m_alerttext.setTextColor(Color.parseColor("#A1A1A1"));
                getFragmentManager().beginTransaction().replace(R.id.display, new Sessions()).commit();
            }
        });
        m_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_hometext.setTextColor(Color.parseColor("#A1A1A1"));
                m_exploretext.setTextColor(Color.parseColor("#A1A1A1"));
                m_awardtext.setTextColor(Color.parseColor("#A1A1A1"));
                m_messagetext.setTextColor(Color.parseColor("#A1A1A1"));
                m_alerttext.setTextColor(Color.parseColor("#000000"));
                getFragmentManager().beginTransaction().replace(R.id.display, new Notifications()).commit();
            }
        });
        return view;
    }
}
