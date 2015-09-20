package trackandhack.trackandhackprototype_2.Classes;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import trackandhack.trackandhackprototype_2.R;

/**
 * Created by andrewdorsett on 9/19/15.
 */
public class HistoryListAdapter extends BaseAdapter {
    Context ctx;
    Integer id;
    List<HistoryItem> historyItems;
    DateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);

    public HistoryListAdapter(Context context, int listViewId, List<HistoryItem> historyItems) {
        ctx = context;
        id = listViewId;
        this.historyItems = historyItems;
    }

    @Override
    public int getCount() {
        return historyItems.size();
    }

    @Override
    public Object getItem(int position) {
        return historyItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return historyItems.get(position).getUid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if( view == null) {
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(id, null);
        }

        HistoryItem historyItem = historyItems.get(position);
        TextView date = (TextView) view.findViewById(R.id.history_date);
        TextView amount = (TextView) view.findViewById(R.id.history_amount);
        ImageView notesIcon = (ImageView) view.findViewById(R.id.history_notes_icon);

        date.setText(format.format(historyItem.getDate()));

        amount.setText(String.format("%.2f", historyItem.getAmount()));

        int backgroundColor = historyItem.getAmount() < 0 ? Color.RED : Color.GREEN;
        amount.setTextColor(backgroundColor);

        notesIcon.getLayoutParams().height = date.getLineHeight();

        if (historyItem.getUid() == -1L) {
            notesIcon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_warning_white_48dp));
        }
        else if (historyItem.getNotes() == null || historyItem.getNotes().isEmpty()) {
            notesIcon.setVisibility(View.INVISIBLE);
        } else {
            notesIcon.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
