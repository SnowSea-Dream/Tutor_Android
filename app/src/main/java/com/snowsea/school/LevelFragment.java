package com.snowsea.school;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.snowsea.school.model.Course;
import com.snowsea.school.model.Level;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LevelFragment extends Fragment {

    Course course;
    List<Level> levels;

    ListView _lstSubjects;
    public LevelFragment() {
        // Required empty public constructor
    }

    public void setCourse(Course course) {
        this.course = course;
        this.levels = course.getLevels();
    }

    private void initViews(View v) {
        _lstSubjects = (ListView) v.findViewById(R.id.lst_courses);
        _lstSubjects.setDivider(null);

        DashBoardActivity activity = (DashBoardActivity) getActivity();
        activity.setStage(DashBoardActivity.Stage.LEVELS);
        showLevels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_level, container, false);
        initViews(v);

        return v;
    }

    public void showLevels() {
        LevelAdapter adapter = new LevelAdapter(getActivity(), course);
        _lstSubjects.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
