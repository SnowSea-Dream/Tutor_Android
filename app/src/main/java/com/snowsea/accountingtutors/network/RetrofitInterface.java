package com.snowsea.accountingtutors.network;

/**
 * Created by SnowSea on 3/29/2017.
 */

import com.snowsea.accountingtutors.model.CheckoutStatus;
import com.snowsea.accountingtutors.model.Course;
import com.snowsea.accountingtutors.model.Lecturer;
import com.snowsea.accountingtutors.model.Login;
import com.snowsea.accountingtutors.model.PurchaseInfo;
import com.snowsea.accountingtutors.model.Question;
import com.snowsea.accountingtutors.model.Response;
import com.snowsea.accountingtutors.model.Student;
import com.snowsea.accountingtutors.model.Subject;
import com.snowsea.accountingtutors.model.User;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {

    @POST("users/register")
    Observable<Response> register(@Body User user);

    @POST("users/login")
    Observable<Login> login();

    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    @GET("courses/")
    Observable<ArrayList<Course>> getCourses();

    @GET("courses/{course_number}/{level_number}/subjects")
    Observable<ArrayList<Subject>> getSubjects(@Path("course_number") int courseNumber, @Path("level_number") int levelNumber);

    @GET("students")
    Observable<Student> getStudent();

    @GET("students/checkout/id/{price}")
    Observable<PurchaseInfo> getCheckoutID(@Path("price") Float price);

    @GET("students/checkout/status/{id}")
    Observable<CheckoutStatus> getCheckoutStatus(@Path("id") String id);

    @POST("students/checkout/success")
    Observable<Response> purchaseSubject(@Body Subject subject);

    @GET("lecturers")
    Observable<Lecturer> getLecturer();

    @GET("questions/{course_number}/{level_number}/{subject_number}")
    Observable<ArrayList<Question>> getQuestions(@Path("course_number") int courseNumber, @Path("level_number") int levelNumber, @Path("subject_number") int subjectNumber);

    @GET("questions/{id}")
    Observable<Question> getQuestion(@Path("id") int id);

    @POST("questions/ask")
    Observable<Response> askQuestion(@Body Question question);

    @POST("questions/answer")
    Observable<Response> answerQuestion(@Body Question question);
}