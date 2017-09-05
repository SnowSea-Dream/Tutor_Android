package com.snowsea.accountingtutors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.snowsea.accountingtutors.model.Chapter;
import com.snowsea.accountingtutors.model.Subject;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SnowSea on 4/1/2017.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> implements Filterable {
    Subject subject;
    private ArrayList<Chapter> lstChapter = null;
    private ArrayList<Chapter> lstFiltered = null;
    private Context context;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toUpperCase();

                if (query.isEmpty()) {
                    lstFiltered = lstChapter;
                }
                else {
                    lstFiltered = new ArrayList<Chapter>();

                    for (Chapter chapter : lstChapter) {
                        if (chapter.getName().toUpperCase().contains(query)) {
                            lstFiltered.add(chapter);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = lstFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lstFiltered = (ArrayList<Chapter>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public FancyButton text;

        public ViewHolder(View itemView) {
            super(itemView);

            text = (FancyButton) itemView.findViewById(R.id.btn_level);
        }
    }

    public ChapterAdapter(Context context, Subject subject) {
        super();
        this.context = context;
        this.subject = subject;
        this.lstChapter = subject.getChapters();
        this.lstFiltered = subject.getChapters();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_chapter, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Chapter chapter = lstFiltered.get(position);
        holder.text.setText(chapter.getName());

        if (!subject.isPaid() && position >= 2) {
            holder.text.setTextColor(context.getResources().getColor(R.color.iron));
        }
        else {
            holder.text.setTextColor(context.getResources().getColor(R.color.primary));
        }


        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity activity = (DashBoardActivity) context;
                if (subject.isPaid()) {
                    activity.loadChapterTypeFragment(position);
                }
                else {
                    if (position >= 2)
                        activity.loadPurchaseFragment();
                    else
                        activity.loadChapterTypeFragment(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstFiltered.size();
    }
}
