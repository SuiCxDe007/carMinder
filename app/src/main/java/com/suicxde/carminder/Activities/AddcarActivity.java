package com.suicxde.carminder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.suicxde.carminder.Database.RemindersDbAdapter;
import com.suicxde.carminder.R;

import java.util.Calendar;

public class AddcarActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    private String[] dates = new String[9];
    private Long mRowId;
    private RemindersDbAdapter mDbHelper;
    EditText vname, serviced, oilchange, oilfilter, nextservice, nextoil, nextoilfilter, nextairfilter, airfilter;
    Button servicedatex, oildatex, oilfilterdatex, senddata, nextservicedatex, nextoildatex, nextoilfilterdatex, airfilterdatex, nextairfilterdatex;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_activty_x);

        //EditTexts
        vname = findViewById(R.id.cname);
        serviced = findViewById(R.id.servicetext);
        oilchange = findViewById(R.id.eoildatetext);
        oilfilter = findViewById(R.id.oilfiltertext);
        airfilter = findViewById(R.id.airfilter);
        nextservice = findViewById(R.id.nextservicetext);
        nextoil = findViewById(R.id.nextoilchange);
        nextoilfilter = findViewById(R.id.nextoilfilter);
        nextairfilter = findViewById(R.id.nextairfilter);

        //DataSelectors
        servicedatex = findViewById(R.id.servicesdate);
        oildatex = findViewById(R.id.eoildate);
        oilfilterdatex = findViewById(R.id.oilfilterdate);
        nextservicedatex = findViewById(R.id.nextservicebutton);
        nextoildatex = findViewById(R.id.nextoilchangebutton);
        nextoilfilterdatex = findViewById(R.id.nextoilfilterbutton);
        airfilterdatex = findViewById(R.id.airfilterdate);
        nextairfilterdatex = findViewById(R.id.nextairfilterdate);

        senddata = findViewById(R.id.submitbut);
        mDbHelper = new RemindersDbAdapter(this);

        senddata.setOnClickListener(this);
        servicedatex.setOnClickListener(this);
        oildatex.setOnClickListener(this);
        oilfilterdatex.setOnClickListener(this);
        nextservicedatex.setOnClickListener(this);
        nextoildatex.setOnClickListener(this);
        nextoilfilterdatex.setOnClickListener(this);
        nextairfilterdatex.setOnClickListener(this);
        airfilterdatex.setOnClickListener(this);

        senddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateForm()){
                    insert();
                }else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddcarActivity.this);
                    builder.setTitle(R.string.warning);
                    builder.setNegativeButton(R.string.ok,null);
                    builder.setMessage(R.string.fillfields);
                    builder.setIcon(R.drawable.warn);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(RemindersDbAdapter.KEY_ROWID)
                : null;
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(RemindersDbAdapter.KEY_ROWID)
                    : null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onResume();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper.open();
        setRowIdFromIntent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(RemindersDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    public void onClick(View v) {

        if (v == servicedatex) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String ddx = null;
                            String MMX = null;
                            if(dayOfMonth<10){
                               ddx = "0" + dayOfMonth;
                            }else {
                                ddx = String.valueOf(dayOfMonth);
                            }
                            if(monthOfYear<10){
                                monthOfYear = monthOfYear+1;
                                MMX = "0" + monthOfYear;
                            }else {
                                monthOfYear = monthOfYear+1;
                                MMX= String.valueOf(monthOfYear);
                            }

                            dates[0] = ddx + "-" + MMX + "-" + year;
                            serviced.setText(ddx + "-" + (monthOfYear ) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == oildatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String ddx = null;
                            String MMX = null;
                            if(dayOfMonth<10){

                                ddx = "0" + dayOfMonth;

                            }else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if(monthOfYear<10){
                                monthOfYear = monthOfYear+1;
                                MMX = "0" + monthOfYear;

                            }else {
                                monthOfYear = monthOfYear+1;
                                MMX= String.valueOf(monthOfYear);
                            }

                            dates[1] = ddx + "-" + MMX + "-" + year;
                            oilchange.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == oilfilterdatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String ddx = null;
                            String MMX = null;
                            if(dayOfMonth<10){

                                ddx = "0" + dayOfMonth;

                            }else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if(monthOfYear<10){
                                monthOfYear = monthOfYear+1;
                                MMX = "0" + monthOfYear;

                            }else {
                                monthOfYear = monthOfYear+1;
                                MMX= String.valueOf(monthOfYear);
                            }

                            dates[2] = ddx + "-" + MMX + "-" + year;
                            oilfilter.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == nextservicedatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String ddx = null;
                            String MMX = null;
                            if(dayOfMonth<10){

                                ddx = "0" + dayOfMonth;

                            }else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if(monthOfYear<10){
                                monthOfYear = monthOfYear+1;
                                MMX = "0" + monthOfYear;

                            }else {
                                monthOfYear = monthOfYear+1;
                                MMX= String.valueOf(monthOfYear);
                            }
                            dates[4] = ddx + "-" + MMX + "-" + year;
                            nextservice.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == nextoildatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String ddx = null;
                            String MMX = null;
                            if(dayOfMonth<10){

                                ddx = "0" + dayOfMonth;

                            }else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if(monthOfYear<10){
                                monthOfYear = monthOfYear+1;
                                MMX = "0" + monthOfYear;

                            }else {
                                monthOfYear = monthOfYear+1;
                                MMX= String.valueOf(monthOfYear);
                            }

                            dates[5] = ddx + "-" + MMX + "-" + year;
                            nextoil.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
     if (v == nextoilfilterdatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String ddx = null;
                            String MMX = null;
                            if(dayOfMonth<10){

                                ddx = "0" + dayOfMonth;

                            }else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if(monthOfYear<10){
                                monthOfYear = monthOfYear+1;
                                MMX = "0" + monthOfYear;

                            }else {
                                monthOfYear = monthOfYear+1;
                                MMX= String.valueOf(monthOfYear);
                            }


                            dates[6] = ddx + "-" + MMX + "-" + year;
                            nextoilfilter.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == nextairfilterdatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String ddx = null;
                            String MMX = null;
                            if (dayOfMonth < 10) {

                                ddx = "0" + dayOfMonth;

                            } else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if (monthOfYear < 10) {
                                monthOfYear = monthOfYear + 1;
                                MMX = "0" + monthOfYear;

                            } else {
                                monthOfYear = monthOfYear + 1;
                                MMX = String.valueOf(monthOfYear);
                            }


                            dates[8] = ddx + "-" + MMX + "-" + year;
                            nextairfilter.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == airfilterdatex) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String ddx = null;
                            String MMX = null;
                            if (dayOfMonth < 10) {

                                ddx = "0" + dayOfMonth;

                            } else {

                                ddx = String.valueOf(dayOfMonth);
                            }

                            if (monthOfYear < 10) {
                                monthOfYear = monthOfYear + 1;
                                MMX = "0" + monthOfYear;

                            } else {
                                monthOfYear = monthOfYear + 1;
                                MMX = String.valueOf(monthOfYear);
                            }


                            dates[7] = ddx + "-" + MMX + "-" + year;
                            airfilter.setText(ddx + "-" + (monthOfYear) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }


    private void insert() {

        RemindersDbAdapter dbAdapter = new RemindersDbAdapter(getApplicationContext());
        long rowInserted =     dbAdapter.addReminder(vname.getText().toString(),dates);
        if(rowInserted != -1){

            AlertDialog.Builder builder = new AlertDialog.Builder(AddcarActivity.this);
            builder.setTitle(R.string.savedsuccess);
            builder.setNegativeButton(R.string.ok,null);
            builder.setMessage(R.string.savedsuccessdesc);
            builder.setIcon(R.drawable.tick);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            vname.setText("");
            serviced.setText("");
            nextservice.setText("");
            oilfilter.setText("");
            oilchange.setText("");
            nextoil.setText("");
            nextoilfilter.setText("");
            airfilter.setText("");
            nextairfilter.setText("");

        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(AddcarActivity.this);
            builder.setTitle(R.string.savefailed);
            builder.setNegativeButton(R.string.ok,null);
            builder.setMessage(R.string.savefaileddesc);
            builder.setIcon(R.drawable.error);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private boolean validateForm() {
        boolean valid = true;

        //~~~~~NEXT SERVICE!!!
        String nexts = nextservice.getText().toString();
        if (TextUtils.isEmpty(nexts)) {
            nextservice.setError(getText(R.string.required));
            valid = false;
        }

        else {
            nextservice.setError(null);
        }
        //~~~~~NEXT OIL!!!
        String nexto = nextoil.getText().toString();

        if (TextUtils.isEmpty(nexto)) {
            nextoil.setError(getText(R.string.required));
            valid = false;
        }

        else {
            nextoil.setError(null);
        }

        //~~~~~NEXT OIL FILTER!!!
        String nextf = nextoilfilter.getText().toString();

        if (TextUtils.isEmpty(nextf)) {
            nextoilfilter.setError(getText(R.string.required));
            valid = false;
        }

        else {
            nextoilfilter.setError(null);
        }

        //~~~~~ SERVICE DATE!!!
        String ss = serviced.getText().toString();

        if (TextUtils.isEmpty(ss)) {
            serviced.setError(getText(R.string.required));
            valid = false;
        }

        else {
            serviced.setError(null);
        }

        //~~~~~OIL CHANGE!!!
        String oo = oilchange.getText().toString();

        if (TextUtils.isEmpty(oo)) {
            oilchange.setError(getText(R.string.required));
            valid = false;
        }

        else {
            oilchange.setError(null);
        }


        //~~~~~OIL FILTER!!!
        String ff = oilfilter.getText().toString();

        if (TextUtils.isEmpty(ff)) {
            oilfilter.setError(getText(R.string.required));
            valid = false;
        }

        else {
            oilfilter.setError(null);
        }

        //~~~~~VEHICLE NAME !!!
        String nn = vname.getText().toString();

        if (TextUtils.isEmpty(nn)) {
            vname.setError(getText(R.string.required));
            valid = false;
        }

        else {
            vname.setError(null);
        }
        //~~~~~AIR FILTER !!!
        String aa = airfilter.getText().toString();

        if (TextUtils.isEmpty(aa)) {
            airfilter.setError(getText(R.string.required));
            valid = false;
        } else {
            airfilter.setError(null);
        }

        //~~~~~NEXT AIR F!!!
        String na = nextairfilter.getText().toString();

        if (TextUtils.isEmpty(na)) {
            nextairfilter.setError(getText(R.string.required));
            valid = false;
        } else {
            nextairfilter.setError(null);
        }


        return valid;
    }


}
