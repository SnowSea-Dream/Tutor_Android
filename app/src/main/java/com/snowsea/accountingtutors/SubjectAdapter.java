package com.snowsea.accountingtutors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.snowsea.accountingtutors.model.Student;
import com.snowsea.accountingtutors.model.Subject;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SnowSea on 4/1/2017.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> implements Filterable {
    private ArrayList<Subject> lstSubject = null;
    private ArrayList<Subject> lstFiltered = null;
    private Context context;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toUpperCase();

                if (query.isEmpty()) {
                    lstFiltered = lstSubject;
                }
                else {
                    lstFiltered = new ArrayList<Subject>();

                    for (Subject subject : lstSubject) {
                        if (subject.getName().toUpperCase().contains(query)) {
                            lstFiltered.add(subject);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = lstFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lstFiltered = (ArrayList<Subject>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public FancyButton text;
        public TextView badge;

        public ViewHolder(View itemView) {
            super(itemView);

            text = (FancyButton) itemView.findViewById(R.id.btn_level);
            badge = (TextView) itemView.findViewById(R.id.txt_badge);
        }
    }

    public SubjectAdapter(Context context, ArrayList<Subject> list) {
        super();
        this.context = context;
        this.lstSubject = list;
        this.lstFiltered = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_subject, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Subject subject = lstFiltered.get(position);
        holder.text.setText(subject.getName());
        if (subject.isPaid()) {
            holder.badge.setVisibility(View.GONE);
        }
        else {
            holder.badge.setVisibility(View.VISIBLE);
        }

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity activity = (DashBoardActivity)context;
                activity.loadChapterFragment(subject);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (lstFiltered != null)
            return lstFiltered.size();

        return 0;
    }
}
