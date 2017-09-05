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
import com.snowsea.accountingtutors.model.Lecturer;
import com.snowsea.accountingtutors.model.Question;
import com.snowsea.accountingtutors.model.Subject;
import com.snowsea.accountingtutors.utils.GlobalData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SnowSea on 4/1/2017.
 */

public class AssignedSubjectAdapter extends BaseAdapter implements Filterable {
    private List<Subject> lstSubject = null;
    private List<Subject> lstFiltered = null;
    Lecturer lecturer;
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

                lstFiltered = new ArrayList<Subject>();
                if (bAllUser == true) {
                    for (Subject subject : lstSubject) {
//                        if (bAllAnswer || subject.getIsAnswered() == bAnswered) {
//                            if (bText == false || subject.getQuestion().getTitle().toUpperCase().contains(text) == true)
                            lstFiltered.add(subject);
//                        }
                    }
                }
                else {
                    for (Subject subject : lstSubject) {
//                        if (userId.equals(question.getQuestion().getUserId()) && (bAllAnswer || question.getIsAnswered() == bAnswered)) {
//                            if (bText == false || question.getQuestion().getTitle().contains(text))
                                lstFiltered.add(subject);
//                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = lstFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lstFiltered = (List<Subject>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public AssignedSubjectAdapter(Context context, Lecturer lecturer) {
        super();
        this.context = context;
        this.lecturer = lecturer;
        this.lstSubject = lecturer.getAssignedSubjects();
        this.lstFiltered = this.lstSubject;
    }

    public int getCount()
    {
        if (lstFiltered == null)
            return 0;
        return lstFiltered.size();
    }

    @Override
    public Subject getItem(int position) {
        return lstFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        Subject subject = getItem(position);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_assigned_subject, parent, false);
        }

        TextView _txtSubject = (TextView) v.findViewById(R.id.txt_subject);
        TextView _txtLevel = (TextView) v.findViewById(R.id.txt_level);
        TextView _txtCourse = (TextView) v.findViewById(R.id.txt_course);

        Course course = GlobalData.lstCourse.get(subject.getCourseNumber());

        _txtSubject.setText(subject.getName());
        _txtCourse.setText(course.getName());
        _txtLevel.setText(course.getLevels().get(subject.getLevelNumber()).getName());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("UserID", lecturer.getUserId());
                intent.putExtra("course_number", subject.getCourseNumber());
                intent.putExtra("level_number", subject.getLevelNumber());
                intent.putExtra("subject_number", subject.getNumber());
                intent.putExtra("Answerable", true);
                context.startActivity(intent);
            }
        });
        return v;
    }

}
