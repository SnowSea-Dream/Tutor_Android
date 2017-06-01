package com.snowsea.school;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.snowsea.school.model.Course;
import com.snowsea.school.model.Level;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by SnowSea on 4/1/2017.
 */

public class LevelAdapter extends BaseAdapter {
    private Course course;
    private List<Level> lstLevel;
    Context context;

    public LevelAdapter(Context context, Course course) {
        super();
        this.context = context;
        this.course = course;
        lstLevel = this.course.getLevels();
    }

    public int getCount() {
        return lstLevel.size();
    }

    @Override
    public Level getItem(int position) {
        return lstLevel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        Level level = getItem(position);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_level, parent, false);
        }
        FancyButton _name = (FancyButton) v.findViewById(R.id.btn_level);
        _name.setText(level.getName());

        _name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity activity = (DashBoardActivity) context;
                activity.loadSubjectFragment(course.getNumber(), level.getNumber());
            }
        });

        return v;
    }

}
