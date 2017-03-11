package net.hawkheadlines.hawkheadlines;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends AppCompatActivity {

    private final String RSS_FEED_URL = "http://hawkheadlines.net/news/feed/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Download and parse the RSS feed.
        new GetFeedTask().execute();
    }

    /* Called once the feed has been downloaded and parsed. */
    private void onFeedReceived(SyndFeed feed) {
        Toast.makeText(this, feed.getTitle(), Toast.LENGTH_SHORT).show();
    }

    /** Asynchronous task that downloads the RSS feed.
     *  Android highly suggests that we do not run internet connections on the main UI thread.
     */
    private class GetFeedTask extends AsyncTask<Void, Void, SyndFeed>
    {
        protected SyndFeed doInBackground(Void... par) {
            SyndFeed feed = null;
            try { feed = new SyndFeedInput().build(new XmlReader(new URL(RSS_FEED_URL))); }
            catch(FeedException | IOException e) { Log.e("ERROR", "CANNOT READ RSS FEED", e); }
            return feed;
        }

        protected void onPostExecute(SyndFeed res) { onFeedReceived(res); }
    }
}