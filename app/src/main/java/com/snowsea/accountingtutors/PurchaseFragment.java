package com.snowsea.accountingtutors;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.snowsea.accountingtutors.model.CheckoutStatus;
import com.snowsea.accountingtutors.model.PurchaseInfo;
import com.snowsea.accountingtutors.model.Response;
import com.snowsea.accountingtutors.model.Student;
import com.snowsea.accountingtutors.model.Subject;
import com.snowsea.accountingtutors.utils.Constants;
import com.snowsea.accountingtutors.utils.DataManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseFragment extends Fragment implements DataManager.CheckoutStatusDelegate {

    TextView _txtSubject;
    TextView _txtPrice;
    FancyButton _btnPurchase;
    Subject subject;
    public String checkoutID;

    public PurchaseFragment() {
        // Required empty public constructor

    }

    public void setInfo(Subject subject) {
        this.subject = subject;
    }

    public void initView(View v) {
        DashBoardActivity activity = (DashBoardActivity) getActivity();
        activity.setStage(DashBoardActivity.Stage.PURCHASE);

        _txtSubject = (TextView) v.findViewById(R.id.txt_subject);
        _txtPrice = (TextView) v.findViewById(R.id.txt_money);
        _btnPurchase = (FancyButton) v.findViewById(R.id.btn_purchase);

        _txtSubject.setText(subject.getName());
        _txtPrice.setText(activity.getResources().getString(R.string.currency) + " " + subject.getPrice());
        _btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showProgressBar("Please wait...");
                DataManager.getManager(activity).getCheckoutID(subject.getPrice(), new DataManager.PurchaseDelegate() {
                    @Override
                    public void handleResponse(PurchaseInfo response) {
                        activity.hideProgressBar();
                        checkoutID = response.getId();
                        Set<String> paymentBrands = new LinkedHashSet<String>();

                        paymentBrands.add("VISA");
                        paymentBrands.add("MASTER");

                        CheckoutSettings checkoutSettings = new CheckoutSettings(response.getId(), paymentBrands);
                        checkoutSettings.setWebViewEnabledFor3DSecure(true);
                        Intent intent = new Intent(activity, CheckoutActivity.class);
                        intent.putExtra(CheckoutActivity.CHECKOUT_SETTINGS, checkoutSettings);

                        startActivityForResult(intent, CheckoutActivity.CHECKOUT_ACTIVITY);
                    }

                    @Override
                    public void handleError(Throwable error) {
                        activity.hideProgressBar();
                        DataManager.getManager(activity).handleError(error);
                    }
                });


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        DashBoardActivity activity = (DashBoardActivity) getActivity();

        DataManager.getManager(getActivity()).getStudent(new DataManager.StudentDelegate() {
            @Override
            public void handleResponse(Student response) {
                activity.student = response;
                ArrayList<Student.PaidSubject> paidSubjects = activity.student.getPaidSubjects();
                for(Student.PaidSubject paidSubject: paidSubjects)
                {
                    if (paidSubject.getCourseNumber() == subject.getCourseNumber() && paidSubject.getLevelNumber() == subject.getLevelNumber() && subject.getNumber() == paidSubject.getSubjectNumber())
                        subject.setPaid(true);
                }

                if (subject.isPaid())
                    PurchaseFragment.this.getFragmentManager().popBackStack();
            }

            @Override
            public void handleError(Throwable error) {
                DataManager.getManager(activity).handleError(error);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);

        initView(v);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DashBoardActivity activity = (DashBoardActivity) getActivity();
        switch (resultCode) {
            case CheckoutActivity.RESULT_OK:
                activity.showProgressBar("Please wait...");
                // DataManager.getManager(getActivity()).getCheckoutStatus(checkoutID, this);
            /* transaction successful */
                break;
            case CheckoutActivity.RESULT_CANCELED:
            /* shopper canceled the checkout process */
                break;
            case CheckoutActivity.RESULT_ERROR:
            /* error occurred */
                PaymentError error = data.getExtras().getParcelable(CheckoutActivity.CHECKOUT_RESULT_ERROR);
                Log.e("SnowSea____", error.getErrorMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void handleResponse(CheckoutStatus response) {
        Log.e("CHECKOUT_STATUS", response.getCode());
        Log.e("CHECKOUT_STATUS_DESC", response.getDescription());
        DashBoardActivity activity = (DashBoardActivity) getActivity();
        activity.hideProgressBar();
        // Toast.makeText(activity, response.getCode(), Toast.LENGTH_LONG).show();
        Pattern p1 = Pattern.compile("(000.000.|000.100.1|000.[36])(.*)");
        Pattern p2 = Pattern.compile("(000.400.0|000.400.100)(.*)");
        if (p1.matcher(response.getCode()).matches() || p2.matcher(response.getCode()).matches()) {
            DataManager.getManager(getActivity()).purchaseSubject(subject, new DataManager.ResponseDelegate() {
                @Override
                public void handleResponse(Response response) {
                    activity.hideProgressBar();
                    new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dark_Dialog)
                            .setTitle("Buy Subject")
                            .setMessage(response.getMessage())
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    subject.setPaid(true);
                                    activity.student.addPaidSubject(subject);
                                    PurchaseFragment.this.getFragmentManager().popBackStack();
                                }
                            }).show();
                }

                @Override
                public void handleError(Throwable error) {
                    DashBoardActivity activity = (DashBoardActivity) getActivity();
                    activity.hideProgressBar();
                    DataManager.getManager(activity).handleError(error);
                }
            });
        }
        else {
            new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dark_Dialog)
                    .setTitle("Error...")
                    .setMessage(response.getDescription())
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                        }
                    }).show();
        }
    }

    @Override
    public void handleError(Throwable error) {
        DashBoardActivity activity = (DashBoardActivity) getActivity();
        activity.hideProgressBar();
        DataManager.getManager(activity).handleError(error);
    }
}
