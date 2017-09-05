package com.snowsea.accountingtutors;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    public ContactUsFragment() {
        // Required empty public constructor

    }

    public void initView(View v) {
        DashBoardActivity activity = (DashBoardActivity) getActivity();
        activity.setStage(DashBoardActivity.Stage.CONTACTUS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contactus, container, false);

        initView(v);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
