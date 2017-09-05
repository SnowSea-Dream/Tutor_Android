package com.snowsea.accountingtutors;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    FancyButton _btnCourses;
    FancyButton _btnContactUs;


    public MainFragment() {
        // Required empty public constructor
    }

    private void initViews(View v) {
        _btnCourses = (FancyButton) v.findViewById(R.id.btn_courses);
        _btnCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity dashBoard = (DashBoardActivity) getActivity();
                dashBoard.loadCoursesFragment();
            }
        });

        _btnContactUs = (FancyButton) v.findViewById(R.id.btn_contactus);
        _btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity dashBoard = (DashBoardActivity) getActivity();
                dashBoard.loadContactUsFragment();
            }
        });

        DashBoardActivity activity = (DashBoardActivity) getActivity();
        activity.setStage(DashBoardActivity.Stage.MAIN);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        initViews(v);
        return v;
    }
}
