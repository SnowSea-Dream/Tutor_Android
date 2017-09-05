package com.snowsea.accountingtutors;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snowsea.accountingtutors.model.Subject;


/**
 *
 */
public class ChapterFragment extends Fragment{

    Subject subject;
    RecyclerView _lstChapter;
    ChapterAdapter adapter;

    int courseNumber;
    int levelNumber;

    public ChapterFragment() {
        // Required empty public constructor
    }

    public void setInfo(Subject subject) {
        this.subject = subject;
    }

    private void initViews(View v) {
        _lstChapter = (RecyclerView) v.findViewById(R.id.lst_subjects);

        showChapter(subject);
        DashBoardActivity activity = (DashBoardActivity)getActivity();
        activity.setStage(DashBoardActivity.Stage.CHAPTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subject, container, false);
        initViews(v);

        return v;
    }

    public void showChapter(Subject subject) {
        adapter = new ChapterAdapter(getActivity(), subject);

        _lstChapter.setHasFixedSize(true);
        _lstChapter.setAdapter(adapter);
        _lstChapter.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void setFilter(String newText) {
        adapter.getFilter().filter(newText);
    }
}
