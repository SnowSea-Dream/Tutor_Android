package com.snowsea.accountingtutors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.snowsea.accountingtutors.model.Course;
import com.snowsea.accountingtutors.model.Question;
import com.snowsea.accountingtutors.model.Subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SnowSea on 4/1/2017.
 */

public class QuestionAdapter extends BaseAdapter implements Filterable {
    private List<Question> lstQuestion = null;
    private List<Question> lstFiltered = null;
    private Boolean bAnswerable = false;
    Context context;

    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String[] query = constraint.toString().split("-");

                String filterUser = query[0].toUpperCase();
                String filterAnswer = query[1].toUpperCase();
                Boolean bAnswered = filterAnswer.equals("ANSWERED");
                Boolean bAllUser = filterUser.equals("ALL");
                Boolean bAllAnswer = filterAnswer.equals("ALL");

                String userId = query[2];

                String text = "";
                if (query.length > 3)
                    text = query[3].toUpperCase();

                Boolean bText = !text.equals("");

                lstFiltered = new ArrayList<Question>();
                if (bAllUser == true) {
                    for (Question question : lstQuestion) {
                        if (bAllAnswer || question.getIsAnswered() == bAnswered) {
                            if (bText == false || question.getQuestion().getTitle().toUpperCase().contains(text) == true)
                            lstFiltered.add(question);
                        }
                    }
                }
                else {
                    for (Question question : lstQuestion) {
                        if (userId.equals(question.getQuestion().getUserId()) && (bAllAnswer || question.getIsAnswered() == bAnswered)) {
                            if (bText == false || question.getQuestion().getTitle().contains(text) == true)
                                lstFiltered.add(question);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = lstFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lstFiltered = (List<Question>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public QuestionAdapter(Context context, List<Question> list, Boolean bAnswerable) {
        super();
        this.context = context;
        this.lstQuestion = list;
        this.lstFiltered = list;
        this.bAnswerable = bAnswerable;
    }

    public int getCount()
    {
        if (lstFiltered == null)
            return 0;
        return lstFiltered.size();
    }

    @Override
    public Question getItem(int position) {
        return lstFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        Question question = getItem(position);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        }

        TextView title = (TextView) v.findViewById(R.id.txt_title);
        TextView querist = (TextView) v.findViewById(R.id.txt_querist);
        TextView date = (TextView) v.findViewById(R.id.txt_date);
        title.setText(question.getQuestion().getTitle());
        querist.setText(question.getQuestion().getUserId());
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyy");
        date.setText(df.format(question.getQuestion().getCreatedAt()));
        TextView badge = (TextView) v.findViewById(R.id.txt_badge);

        if (question.getIsAnswered() == true) {
            badge.setVisibility(View.VISIBLE);
        }
        else {
            badge.setVisibility(View.GONE);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionDetail.class);
                intent.putExtra("Id", question.getId());
                intent.putExtra("Title", question.getQuestion().getTitle());
                intent.putExtra("Question", question.getQuestion().getContent());
                intent.putExtra("Answerable", bAnswerable);
                if (question.getAnswer() != null)
                    intent.putExtra("Answer", question.getAnswer().getContent());
                context.startActivity(intent);

            }
        });
        return v;
    }

}
