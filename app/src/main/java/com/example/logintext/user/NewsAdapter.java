package com.example.logintext.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.logintext.R;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<NewsItem> news;
    int layout;

    public NewsAdapter(Context context,int layout,ArrayList<NewsItem> news)
    {
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.news = news;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder  holder = new ViewHolder();

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout,viewGroup,false);
            holder.news_title = (TextView)view.findViewById(R.id.news);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        NewsItem n = news.get(i);

        if(n!=null)
        {
            holder.news_title.setText(news.get(i).getTitle());
        }
        return view;
    }

    static class ViewHolder{
        TextView news_title;
    }
}