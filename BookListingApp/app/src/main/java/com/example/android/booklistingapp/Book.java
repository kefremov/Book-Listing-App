package com.example.android.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;


public class Book implements Parcelable {

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private final String title;
    private final String author;
    private final String imageUrl;
    private final String language;
    private String urlBook;

    public Book(String bookTitle, String authorName, String urlImageCover, String languageCode, String buyLink) {
        this.title = bookTitle;
        this.author = authorName;
        this.imageUrl = urlImageCover;
        this.language = languageCode;
        this.urlBook = buyLink;
    }

    protected Book(Parcel in) {
        this.title = in.readString();
        this.author = in.readString();
        this.imageUrl = in.readString();
        this.language = in.readString();
        this.urlBook = in.readString();
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getLanguage() {
        return language;
    }
    public String getUrlBook() {
        return urlBook;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.imageUrl);
        dest.writeString(this.language);
        dest.writeString(this.urlBook);
    }
}
