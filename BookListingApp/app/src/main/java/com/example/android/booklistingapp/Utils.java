package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public final class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    static List<Book> fetchBookData(String requestUrl) {

        final int SLEEP_TIME_MILLIS = 2000;

        try {
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "HTTP request: OK");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> listBooks = extractBookFromJson(jsonResponse);

        return listBooks;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        final int READ_TIMEOUT = 10000;
        final int CONNECT_TIMEOUT = 15000;
        final int CORRECT_RESPONSE_CODE = 200;

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == CORRECT_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractBookFromJson(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            Log.println(Log.INFO, LOG_TAG, bookJSON);
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");
            Log.println(Log.INFO, LOG_TAG, String.valueOf(booksArray));

            for (int i = 0; i < booksArray.length(); i++) {

                JSONObject currentBook = booksArray.getJSONObject(i);
                Log.println(Log.INFO, LOG_TAG, String.valueOf(currentBook));
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                String language = volumeInfo.getString("language");
                String link = (String) volumeInfo.get("previewLink");

                String author;
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    Log.println(Log.INFO, LOG_TAG, String.valueOf(authors));
                    if (!volumeInfo.isNull("authors")) {
                        author = (String) authors.get(0);
                    } else {
                        author = "*** unknown author ***";
                    }
                } else {
                    author = "*** missing info of authors ***";
                }

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                Log.println(Log.INFO, LOG_TAG, String.valueOf(imageLinks));
                String coverImageUrl = imageLinks.getString("smallThumbnail");
                StringBuilder stringBuilder = new StringBuilder();
                Pattern p = Pattern.compile("id=(.*?)&");
                Matcher m = p.matcher(coverImageUrl);
                if (m.matches()) {
                    String id = m.group(1);
                    coverImageUrl = String.valueOf(stringBuilder.append("https://books.google.com/books/content/images/frontcover/").append(id).append("?fife=w300"));
                } else {
                    Log.i(LOG_TAG, "Issue with cover");
                }

                Book bookItem = new Book(title, author, coverImageUrl, language, link);
                books.add(bookItem);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        return books;
    }
}
