package com1032.cw2.sk00763.improov;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

/**
 * Created by Stelios on 07/03/2020.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.SearchViewHolder>  {

    //context object
    private Context m_context = null;
    //list of events
    private List<Article> m_articles = null;
    //custom click listener for events
    private ArticleListAdapter.ArticleActionListener m_articleListener = null;

    /**
     * Constructor for adapter
     * @param context: context of app
     * @param articles: list of discussions
     * @param eventActionListener: custom  listener for our discussions list
     */
    public ArticleListAdapter(Context context, List<Article> articles, ArticleListAdapter.ArticleActionListener eventActionListener) {
        super();
        this.m_context = context;
        this.m_articles = articles;
        this.m_articleListener = eventActionListener;
    }

    @Override
    public ArticleListAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate item layout for list items
        View view = LayoutInflater.from(m_context).inflate(R.layout.article_layout, parent, false);
        return new ArticleListAdapter.SearchViewHolder(view, m_articleListener);
    }

    @Override
    public void onBindViewHolder(final ArticleListAdapter.SearchViewHolder holder, int position) {
        //set the event details
        holder.m_title.setText(m_articles.get(position).getTitle());
        holder.m_paragraph.setText(m_articles.get(position).getParagraph1() + ".....");
        String imagenum = m_articles.get(position).getImagenum();
        int id = m_context
                .getResources()
                .getIdentifier("com1032.cw2.sk00763.improov:drawable/" + m_articles.get(position).getCategory().toLowerCase() + imagenum, null, null);
        holder.m_image.setImageResource(id);
    }

    /**
     * Method to return number of discussions
     * @return: number of discussions
     */
    @Override
    public int getItemCount() {
        return m_articles.size();
    }


    //search view holder class
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //event name
        private TextView m_title = null;
        //event category
        private TextView m_paragraph = null;
        //on event click listener
        private ImageView m_image = null;

        private ArticleListAdapter.ArticleActionListener m_listener = null;

        private SearchViewHolder(View itemView, ArticleListAdapter.ArticleActionListener articleActionListener) {
            super(itemView);

            //inflate widgets
            m_title = (TextView) itemView.findViewById(R.id.cardarticlename);
            m_paragraph = (TextView) itemView.findViewById(R.id.cardarticleparagraph);
            m_image = (ImageView) itemView.findViewById(R.id.articleimage);

            m_listener = articleActionListener;

            //set onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //call our on click listener
            m_listener.onArticleClick(getAdapterPosition());
        }
    }

    /**
     * Interface for all event list actions
     */
    public interface ArticleActionListener{
        /** on event click */
        void onArticleClick(int position);
    }
}
