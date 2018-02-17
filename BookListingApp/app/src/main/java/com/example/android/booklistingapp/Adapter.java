package com.example.android.booklistingapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Adapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = Adapter.class.getSimpleName();

    public Adapter(Activity context, ArrayList<Book> Books) {
        super(context, 0, Books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);
        Log.i(LOG_TAG, "Item position: " + position);

        TextView titleBookTextView = (TextView) listItemView.findViewById(R.id.book_title);
        TextView authorBookTextView = (TextView) listItemView.findViewById(R.id.author);
        ImageView coverImageView = (ImageView) listItemView.findViewById(R.id.cover_image);
        TextView languageCode = (TextView) listItemView.findViewById(R.id.country_code);

        assert currentBook != null;
        titleBookTextView.setText(currentBook.getTitle());
        authorBookTextView.setText(currentBook.getAuthor());
        Picasso.with(getContext()).load(currentBook.getImageUrl()).into(coverImageView);
        languageCode.setText(currentBook.getLanguage());

        Log.i(LOG_TAG, "ListView has been returned.");
        return listItemView;

    }

}
