package com.snowsea.school;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.snowsea.school.model.Course;
import com.snowsea.school.utils.DataManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment implements DataManager.CourseDelegate {

    ListView _lstCourses;
    SimpleCursorAdapter mAdapter;

    public CourseFragment() {
        // Required empty public constructor

    }

    private void initViews(View v) {
        _lstCourses = (ListView) v.findViewById(R.id.lst_courses);
        _lstCourses.setDivider(null);

        DashBoardActivity activity = (DashBoardActivity)getActivity();
        activity.showProgressBar(activity.getResources().getString(R.string.loading));

        DataManager.getManager(getActivity()).getCourses(this);

        activity.setStage(DashBoardActivity.Stage.COURSES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course, container, false);
        initViews(v);

        return v;
    }

    public void showCourses(List<Course> courses) {
        CourseAdapter adapter = new CourseAdapter(getActivity(), courses);
        _lstCourses.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void handleResponse(ArrayList<Course> response) {
        DashBoardActivity activity = (DashBoardActivity)getActivity();

        activity.hideProgressBar();
        showCourses(response);
    }

    @Override
    public void handleError(Throwable error) {
        DashBoardActivity activity = (DashBoardActivity)getActivity();
        activity.hideProgressBar();

        DataManager.getManager(getActivity()).handleError(error);
    }
}
