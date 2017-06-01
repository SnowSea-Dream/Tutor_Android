package com.snowsea.school.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snowsea.school.R;
import com.snowsea.school.model.Answer;
import com.snowsea.school.model.Ask;
import com.snowsea.school.model.CheckoutStatus;
import com.snowsea.school.model.Course;
import com.snowsea.school.model.Lecturer;
import com.snowsea.school.model.PurchaseInfo;
import com.snowsea.school.model.Question;
import com.snowsea.school.model.Response;
import com.snowsea.school.model.Student;
import com.snowsea.school.model.Subject;
import com.snowsea.school.network.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SnowSea on 4/2/2017.
 */

public class DataManager {

    public interface CourseDelegate {
        public void handleResponse(ArrayList<Course> response);
        public void handleError(Throwable error);
    };

    public interface SubjectDelegate {
        public void handleResponse(ArrayList<Subject> response);
        public void handleError(Throwable error);
    };

    public interface StudentDelegate {
        public void handleResponse(Student response);
        public void handleError(Throwable error);
    };

    public interface LecturerDelegate {
        public void handleResponse(Lecturer response);
        public void handleError(Throwable error);
    };

    public interface QuestionDelegate {
        public void handleResponse(ArrayList<Question> response);
        public void handleError(Throwable error);
    };

    public interface ResponseDelegate {
        public void handleResponse(Response response);
        public void handleError(Throwable error);
    };

    public interface PurchaseDelegate {
        public void handleResponse(PurchaseInfo response);
        public void handleError(Throwable error);
    };

    public interface CheckoutStatusDelegate {
        public void handleResponse(CheckoutStatus response);
        public void handleError(Throwable error);
    };

    private static DataManager manager = null;

    private SharedPreferences mSharedPreferences;

    private Context context;
    private CompositeSubscription mSubscriptions;

    public static DataManager getManager(Context context) {

        if (manager == null) {
            manager = new DataManager(context);
        }

        manager.context = context;
        return manager;
    }

    protected DataManager(Context context) {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mSubscriptions = new CompositeSubscription();

        this.context = context;
    }

    public void handleError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);

                new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                        .setTitle(R.string.error)
                        .setMessage(response.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                            }
                        }).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                    .setTitle(R.string.error)
                    //.setMessage(R.string.msg_dialog_network_error)
                    .setMessage(error.getMessage())
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                        }
                    }).show();
        }
    }

    public void getCourses(CourseDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getCourses()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void getSubject(int courseNumber, int levelNumber, SubjectDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getSubjects(courseNumber, levelNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void getStudent(StudentDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getStudent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void getLecturer(LecturerDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getLecturer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void getQuestions(int courseNumber, int levelNumber, int subjectNumber, QuestionDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getQuestions(courseNumber, levelNumber, subjectNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void askQuestions(int courseNumber, int levelNumber, int subjectNumber, String title, String content, ResponseDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        Question question = new Question();
        question.setCourseNumber(courseNumber);
        question.setLevelNumber(levelNumber);
        question.setSubjectNumber(subjectNumber);
        Ask ask = new Ask();
        ask.setTitle(title);
        ask.setContent(content);
        question.setQuestion(ask);

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).askQuestion(question)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void answerQuestions(String question_id, String content, ResponseDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        Question question = new Question();
        question.setId(question_id);

        Answer answer = new Answer();
        answer.setContent(content);
        question.setAnswer(answer);

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).answerQuestion(question)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void getCheckoutID(Float price, PurchaseDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getCheckoutID(price)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void getCheckoutStatus(String id, CheckoutStatusDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).getCheckoutStatus(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }

    public void purchaseSubject(Subject subject, ResponseDelegate delegate) {

        String token = mSharedPreferences.getString(Constants.TOKEN, "");
        String email = mSharedPreferences.getString(Constants.EMAIL, "");

        mSubscriptions.add(NetworkUtil.getRetrofit(email, token).purchaseSubject(subject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(delegate::handleResponse, delegate::handleError));
    }
}
