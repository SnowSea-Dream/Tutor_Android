package com.snowsea.school;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snowsea.school.model.Subject;
import com.snowsea.school.utils.DataManager;

import java.util.ArrayList;


/**
 *
 */
public class SubjectFragment extends Fragment implements DataManager.SubjectDelegate{
    
    RecyclerView _lstSubjects;
    SubjectAdapter adapter;

    int courseNumber;
    int levelNumber;

    public SubjectFragment() {
        // Required empty public constructor
    }

    public void setInfo(int courseNumber, int levelNumber) {
        this.courseNumber = courseNumber;
        this.levelNumber = levelNumber;
    }

    private void initViews(View v) {
        _lstSubjects = (RecyclerView) v.findViewById(R.id.lst_subjects);

        DashBoardActivity activity = (DashBoardActivity)getActivity();
        activity.showProgressBar(activity.getResources().getString(R.string.loading));

        DataManager.getManager(getActivity()).getSubject(courseNumber, levelNumber, this);
        activity.setStage(DashBoardActivity.Stage.SUBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subject, container, false);
        initViews(v);

        return v;
    }

    public void showSubjects(ArrayList<Subject> response) {
        adapter = new SubjectAdapter(getActivity(), response);

        _lstSubjects.setHasFixedSize(true);
        _lstSubjects.setAdapter(adapter);
        _lstSubjects.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void handleResponse(ArrayList<Subject> response) {
        DashBoardActivity activity = (DashBoardActivity)getActivity();

        activity.hideProgressBar();
        activity.setStudentInfo(response);
        showSubjects(response);
    }

    @Override
    public void handleError(Throwable error) {
        DashBoardActivity activity = (DashBoardActivity)getActivity();
        activity.hideProgressBar();

        DataManager.getManager(getActivity()).handleError(error);
    }

    public void setFilter(String newText) {
        adapter.getFilter().filter(newText);
    }
}
