package com.example.android.newstoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aaryan on 17-03-2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getName();

    static class ViewHolder {
        public TextView webTitleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;
    }

    public NewsAdapter(Context context, ArrayList<News> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_feed,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.webTitleTextView = (TextView) listItemView.findViewById(R.id.title);
            viewHolder.descriptionTextView = (TextView) listItemView.findViewById(R.id.description);
            viewHolder.imageView = (ImageView) listItemView.findViewById(R.id.Image_View);
            listItemView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) listItemView.getTag();
        News news = getItem(position);

        holder.webTitleTextView.setText(news.getTitle());
        holder.descriptionTextView.setText(news.getDescription());
        holder.imageView.setImageBitmap(news.getBitmap());

        return listItemView;
    }


}
