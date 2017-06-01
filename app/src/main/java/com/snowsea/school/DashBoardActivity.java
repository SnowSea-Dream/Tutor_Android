package com.snowsea.school;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;
import com.snowsea.school.model.Course;
import com.snowsea.school.model.Student;
import com.snowsea.school.model.Subject;
import com.snowsea.school.utils.Constants;
import com.snowsea.school.utils.DataManager;
import com.snowsea.school.utils.GlobalData;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog progressDialog;

    private MainFragment fragMain;
    private CourseFragment fragCourse;
    private SubjectFragment fragSubject;
    private ChapterFragment fragChapter;
    private ChapterTypeFragment fragChapterType;
    private PurchaseFragment fragPurchase;
    private VideoFragment fragVideo;
    private NoteFragment fragNote;
    private Toolbar toolbar;

    private TextView _txtName;
    private TextView _txtEmail;

    public Course course;
    public Subject subject;
    public Student student;

    int courseNumber = 0;
    int levelNumber = 0;
    int subjectNumber = 0;
    int chapterNumber = 0;

    MenuItem itemAsk;
    MenuItem itemQuestions;

    public enum Stage{
        MAIN, COURSES, LEVELS, SUBJECT, CHAPTER, CHAPTER_TYPE, PURCHASE, CONTACTUS, VIDEO, NOTE;

    }

    private Stage stage = Stage.MAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView v = (NavigationView) drawer.findViewById(R.id.nav_view);
        Menu menu = v.getMenu();

        itemAsk = menu.findItem(R.id.nav_ask);
        itemQuestions = menu.findItem(R.id.nav_questions);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = v.getHeaderView(0);

        _txtName = (TextView) header.findViewById(R.id.txt_name);
        _txtName.setText(GlobalData.user.getFirstName() + " " + GlobalData.user.getLastName());
        _txtEmail = (TextView) header.findViewById(R.id.txt_email);
        _txtEmail.setText(GlobalData.user.getEmail());

        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();

        loadStudent();
        loadMainFragment();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getScheme().equals("school.snowsea.com")) {
            String checkoutId = intent.getData().getQueryParameter("id");

        /* request payment status */
        }

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void showProgressBar(String message) {
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    public void setStage(Stage newStage) {
        this.stage = newStage;

        String title = null;
        itemAsk.setVisible(false);
        itemQuestions.setVisible(false);
        switch (stage) {
            case MAIN:
                title = getResources().getString(R.string.title_actionbar_dashboard);
                break;
            case COURSES:
                title = getResources().getString(R.string.title_actionbar_courses);
                break;
            case LEVELS:
                title = course.getName();
                break;
            case SUBJECT:
                title = course.getLevels().get(levelNumber).getName();
                break;
            case CONTACTUS:
                title = getResources().getString(R.string.title_actionbar_contactus);
                break;
            case CHAPTER:
                title = subject.getName();
                if (subject.isPaid()) {
                    itemAsk.setVisible(true);
                    itemQuestions.setVisible(true);
                }

                break;
            case CHAPTER_TYPE:
                title = subject.getChapters().get(chapterNumber).getName();
                if (subject.isPaid()) {
                    itemAsk.setVisible(true);
                    itemQuestions.setVisible(true);
                }
                break;
            case PURCHASE:
                title = getResources().getString(R.string.title_actionbar_purchase);
                break;
            case VIDEO:
                title = getResources().getString(R.string.title_actionbar_video);
                if (subject.isPaid()) {
                    itemAsk.setVisible(true);
                    itemQuestions.setVisible(true);
                }
                break;
            case NOTE:
                title = "Note";
                if (subject.isPaid()) {
                    itemAsk.setVisible(true);
                    itemQuestions.setVisible(true);
                }
                break;

            default:
                title = getResources().getString(R.string.title_actionbar_dashboard);
        }
        setActionBarTitle(title);
    }

    public Stage getStage() {
        return stage;
    }

    public void setActionBarTitle(int resID) {
        toolbar.setTitle(resID);
    }
    public void setActionBarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void loadStudent() {
        DataManager.getManager(this).getStudent(new DataManager.StudentDelegate() {
            @Override
            public void handleResponse(Student response) {
                student = response;
            }

            @Override
            public void handleError(Throwable error) {
                DataManager.getManager(DashBoardActivity.this).handleError(error);
            }
        });
    }

    public void setStudentInfo(ArrayList<Subject> response) {
        ArrayList<Student.PaidSubject> paidSubjects = student.getPaidSubjects();
        int rCourseNumber = course.getNumber();
        int rLevelNumber = course.getLevels().get(levelNumber).getNumber();
        for(Student.PaidSubject paidSubject: paidSubjects)
        {
            if (paidSubject.getCourseNumber() == rCourseNumber && paidSubject.getLevelNumber() == rLevelNumber)
                response.get(paidSubject.getSubjectNumber()).setPaid(true);
        }
    }

    public void loadMainFragment()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragMain = new MainFragment();

        fragmentTransaction.add(R.id.fragment_container, fragMain);
        fragmentTransaction.commit();

        setStage(Stage.MAIN);
    }

    public void loadCoursesFragment()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        fragCourse = new CourseFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragCourse);
        fragmentTransaction.addToBackStack("Courses");
        fragmentTransaction.commit();

        setStage(Stage.COURSES);
    }

    public void loadContactUsFragment()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        ContactUsFragment fragContactUs = new ContactUsFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragContactUs);
        fragmentTransaction.addToBackStack("ContactUs");
        fragmentTransaction.commit();

        setStage(Stage.CONTACTUS);
    }

    public void loadLevelFragment(Course course)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        this.course = course;
        LevelFragment frag = new LevelFragment();
        frag.setCourse(course);

        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack("Level");
        fragmentTransaction.commit();

        setStage(Stage.LEVELS);
    }

    public void loadSubjectFragment(int courseNumber, int levelNumber)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        fragSubject = new SubjectFragment();
        this.courseNumber = courseNumber;
        this.levelNumber = levelNumber;
        fragSubject.setInfo(courseNumber, levelNumber);

        fragmentTransaction.replace(R.id.fragment_container, fragSubject);
        fragmentTransaction.addToBackStack("Subject");
        fragmentTransaction.commit();

        setStage(Stage.SUBJECT);
    }

    public void loadChapterFragment(Subject subject)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        this.subject = subject;
        this.subjectNumber = subject.getNumber();
        fragChapter = new ChapterFragment();
        fragChapter.setInfo(subject);


        fragmentTransaction.replace(R.id.fragment_container, fragChapter);
        fragmentTransaction.addToBackStack("Chapter");
        fragmentTransaction.commit();

        setStage(Stage.CHAPTER);
    }

    public void loadChapterTypeFragment(int chapterNumber)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        this.chapterNumber = chapterNumber;
        fragChapterType = new ChapterTypeFragment();
        fragChapterType.setInfo(subject.getChapters().get(chapterNumber));

        fragmentTransaction.replace(R.id.fragment_container, fragChapterType);
        fragmentTransaction.addToBackStack("ChapterType");
        fragmentTransaction.commit();
    }

    public void loadPurchaseFragment()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        fragPurchase = new PurchaseFragment();
        fragPurchase.setInfo(subject);

        fragmentTransaction.replace(R.id.fragment_container, fragPurchase);
        fragmentTransaction.addToBackStack("Purchase");
        fragmentTransaction.commit();
    }

    public void loadVideoFragment()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        fragVideo = new VideoFragment();
        String email = mSharedPreferences.getString(Constants.EMAIL, "");
        String token = mSharedPreferences.getString(Constants.TOKEN, "");;
        fragVideo.setInfo(subject.getChapters().get(chapterNumber), email, token);

        fragmentTransaction.replace(R.id.fragment_container, fragVideo);
        fragmentTransaction.addToBackStack("Video");
        fragmentTransaction.commit();

        setStage(Stage.VIDEO);
    }

    public void loadNoteFragment()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        fragNote = new NoteFragment();
        String email = mSharedPreferences.getString(Constants.EMAIL, "");
        String token = mSharedPreferences.getString(Constants.TOKEN, "");;
        fragNote.setInfo(subject.getChapters().get(chapterNumber), email, token);

        fragmentTransaction.replace(R.id.fragment_container, fragNote);
        fragmentTransaction.addToBackStack("Note");
        fragmentTransaction.commit();

/*        Chapter chapter = subject.getChapters().get(chapterNumber);
        String pdf = Constants.NOTE_FOLDER_URL + chapter.getNoteUrl();
        Uri uri = Uri.parse(pdf);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                if (stage == Stage.VIDEO && fragVideo.bFullScreen == true) {
                    fragVideo.onNormalSize();
                }
                else {
                    getFragmentManager().popBackStack();
                }
            } else {
                logout();
            }
        }
    }

    public void logout() {
        new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
                .setTitle("Log out")
                .setMessage("Do you want to log out really?")
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        DashBoardActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (getStage()) {
                    case COURSES:

                        break;
                    case LEVELS:

                        break;
                    case SUBJECT:
                        fragSubject.setFilter(newText);
                        break;
                    case CHAPTER:
                        fragChapter.setFilter(newText);
                    case CONTACTUS:

                        break;
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getIt1emId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
*/
        if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_ask) {
            Intent intent = new Intent(this, AskActivity.class);
            intent.putExtra("course_number", courseNumber);
            intent.putExtra("level_number", levelNumber);
            intent.putExtra("subject_number", subjectNumber);
            startActivity(intent);
        } else if (id == R.id.nav_questions) {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("UserID", student.getUserId());
            intent.putExtra("course_number", courseNumber);
            intent.putExtra("level_number", levelNumber);
            intent.putExtra("subject_number", subjectNumber);
            intent.putExtra("Answerable", false);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // do nothing, just override
    }

    private IProviderBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IProviderBinder) service;
        /* we have a connection to the service */
            try {
                binder.initializeProvider(Connect.ProviderMode.LIVE);
            } catch (PaymentException ee) {
	    /* error occurred */
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, ConnectService.class);

        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unbindService(serviceConnection);
        stopService(new Intent(this, ConnectService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case CheckoutActivity.RESULT_OK:
            /* transaction successful */
                break;
            case CheckoutActivity.RESULT_CANCELED:
            /* shopper canceled the checkout process */
                break;
            case CheckoutActivity.RESULT_ERROR:
            /* error occurred */

                PaymentError error = data.getExtras().getParcelable(CheckoutActivity.CHECKOUT_RESULT_ERROR);
        }
    }
}
