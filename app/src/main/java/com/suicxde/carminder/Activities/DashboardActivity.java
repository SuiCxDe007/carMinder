package com.suicxde.carminder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suicxde.carminder.R;
import com.suicxde.carminder.Receivers.OnAlarmReceiver;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView addcarcard, listcard, logoutcard, profilecard;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDb = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_dashboard);

        //cardViews
        addcarcard = findViewById(R.id.soscard_res);
        listcard = findViewById(R.id.mapscard_res);
        logoutcard = findViewById(R.id.attatchcardid);
        profilecard = findViewById(R.id.profilecard_res);

        addcarcard.setOnClickListener(this);
        listcard.setOnClickListener(this);
        logoutcard.setOnClickListener(this);
        profilecard.setOnClickListener(this);
        //create Method to Call Daily @ 8AM
        createAlarmForEveryday();


    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    //card OnClicks
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.soscard_res) {
            Intent addcar = new Intent(this, AddcarActivity.class);
            startActivity(addcar);
        } else if (id == R.id.mapscard_res) {

            Intent viewcar = new Intent(this, ViewVehiclesActivity.class);
            startActivity(viewcar);
        } else if (id == R.id.profilecard_res) {
            Intent profilex = new Intent(this, ProfileActivity.class);
            startActivity(profilex);
            this.finish();
        } else if (id == R.id.attatchcardid) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);

        }

    }

    //Language Set
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    //Load Lang
    public void loadLocale(){

        SharedPreferences prefs =getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);

    }

    //Reminder Time  : 8 AM
    private void createAlarmForEveryday(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        int min = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(this, OnAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent1, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


}
//TODO PUSH TO GIT- SUICXDE007