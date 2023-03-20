package com.example.championship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

@SuppressLint("ViewHolder")
public class AdapterQuotes extends BaseAdapter {

    private Context ThisContext;
    private List<Quotes> QuotesList;

    public AdapterQuotes(Context context, List<Quotes> quotesList) {

        ThisContext = context;
        QuotesList = quotesList;
    }

    @Override
    public int getCount() {
        return QuotesList.size();
    }

    @Override
    public Object getItem(int i) {
        return QuotesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return QuotesList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(ThisContext, R.layout.item_quotes, null);

        TextView TVHeader = v.findViewById(R.id.TVHeader);
        TextView TVDescription = v.findViewById(R.id.TVDescription);
        ImageView image = v.findViewById(R.id.imageQuotes);

        Quotes quote = QuotesList.get(i);
        TVHeader.setText(quote.getTitle());
        TVDescription.setText(quote.getDescription());
        new GetSetBitmap(image).execute(quote.getImage());

        return v;
    }
}