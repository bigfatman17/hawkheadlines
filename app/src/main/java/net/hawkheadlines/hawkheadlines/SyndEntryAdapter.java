package net.hawkheadlines.hawkheadlines;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rometools.rome.feed.synd.SyndEntry;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by Armend on 3/11/2017.
 * Allows population of RSS entries into a ListView.
 */

public class SyndEntryAdapter extends ArrayAdapter<SyndEntry> {
    public SyndEntryAdapter(Context context, List<SyndEntry> entries) {
        super(context, 0, entries);
    }

    /* An overridden function that produces a View from the current SyndEntry. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Because Views are "recycled", we must inflate this null one with our layout.
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.syndentry, parent, false);

        // Get the current entry.
        SyndEntry currentEntry = getItem(position);
        // Set the title in the layout to the title of the current entry.
        ((TextView) convertView.findViewById(R.id.title)).setText(currentEntry.getTitle());

        // Parse the HTML within the entry's description.
        Document post = Jsoup.parse(currentEntry.getDescription().getValue());

        // Get the ImageView of the layout.
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        // Download the entry's thumbnail and load it into the ImageView.
        Picasso.with(getContext()).load(post.select("img").attr("src")).into(image);
        // Set the layout's description to the description found in the feed.
        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(post.text().replace(" Continue Reading››", "...") + "\n");
        description.setGravity(Gravity.CENTER);
        // Dynamically set the height of the TextView based on the amount of text.
        description.getLayoutParams().height = description.getLineCount() * description.getLineHeight();
        // Refresh the layout once we've finished.
        notifyDataSetChanged();

        return convertView;
    }
}
