package net.hawkheadlines.hawkheadlines;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main extends AppCompatActivity {

    private final String RSS_FEED_URL = "http://hawkheadlines.net/news/feed/";

    /* Called when the app starts. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Retrieve the Toolbar from the layout and set it as the Action Bar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        // Why I can't do this in XML? I have no idea.
        getSupportActionBar().setTitle("");

        // Download and parse the RSS feed.
        new GetFeedTask().execute();
    }

    /* Once the feed has been downloaded and parsed, populate the app with the articles. */
    private void onFeedReceived(SyndFeed feed) {
        ListView v = (ListView) findViewById(R.id.articles);
        // Create an instance of the custom adapter and set the ListView's adapter to it.
        SyndEntryAdapter adapter = new SyndEntryAdapter(this, feed.getEntries());
        v.setAdapter(adapter);
    }

    /**
     * Asynchronous task that downloads the RSS feed.
     * Android highly suggests that we do not run internet connections on the main UI thread.
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