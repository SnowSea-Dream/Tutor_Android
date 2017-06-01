package com.snowsea.school;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.snowsea.school.model.Course;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SnowSea on 4/1/2017.
 */

public class CourseAdapter extends BaseAdapter {
    private List<Course> lstCourse;
    Context context;

    public CourseAdapter(Context context, List<Course> list) {
        super();
        this.context = context;
        this.lstCourse = list;
    }

    public int getCount() {
        return lstCourse.size();
    }

    @Override
    public Course getItem(int position) {
        return lstCourse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        Course course = getItem(position);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        }
        FancyButton _name = (FancyButton) v.findViewById(R.id.btn_course);
        TextView _badge = (TextView) v.findViewById(R.id.txt_badge);
        _name.setText(course.getName());
        if (course.getIsAvailable().toUpperCase().equals("TRUE")) {
            _badge.setText(context.getResources().getString(R.string.opened));
            _badge.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else {
            _badge.setText(context.getResources().getString(R.string.coming_soon));
            _badge.setBackgroundColor(context.getResources().getColor(R.color.primary));
        }

        _name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course.isAvailable()) {
                    DashBoardActivity activity = (DashBoardActivity) context;
                    activity.loadLevelFragment(course);
                }
            }
        });

        return v;
    }


}
