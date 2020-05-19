package com.suicxde.carminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.suicxde.carminder.Database.RemindersDbAdapter;
import com.suicxde.carminder.Models.DialogModelReports;
import com.suicxde.carminder.R;
import net.igenius.customcheckbox.CustomCheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class EditCarActivityt extends AppCompatActivity implements View.OnClickListener, DialogModelReports.dlglistner2 {

    EditText vname, serviced, oilchange, oilfilter, nextservice, nextoil, nextoilfilter, nextairfilter, airfilter;
    Button emailsend, servicedatex, oildatex, oilfilterdatex, senddata, nextservicedatex, nextoildatex, nextoilfilterdatex, delbutton, nextairfilterdatex, airfilterdatex;
    CustomCheckBox servicecheck, oilcheck, filtercheck, aircheck;
    private RemindersDbAdapter mDbHelper;
    private Long mRowId;
    private String[] dates = new String[9];
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    private int mYear, mMonth, mDay;
    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car_activityt);


        //EDIT TEXTS
        vname = findViewById(R.id.cname);
        emailsend = findViewById(R.id.emailbut);
        serviced = findViewById(R.id.servicetext);
        oilchange = findViewById(R.id.eoildatetext);
        oilfilter = findViewById(R.id.oilfiltertext);
        airfilter = findViewById(R.id.airfiltertext);
        nextairfilter = findViewById(R.id.nextairfilter);
        nextservice = findViewById(R.id.nextservicetext);
        nextoil = findViewById(R.id.nextoilchange);
        nextoilfilter = findViewById(R.id.nextoilfilter);

        //DATE SELECTORS
        servicedatex = findViewById(R.id.servicesdate);
        oildatex = findViewById(R.id.eoildate);
        oilfilterdatex = findViewById(R.id.oilfilterdate);
        airfilterdatex = findViewById(R.id.airfilterdate);
        nextservicedatex = findViewById(R.id.nextservicebutton);
        nextoildatex = findViewById(R.id.nextoilchangebutton);
        nextoilfilterdatex = findViewById(R.id.nextoilfilterbutton);
        nextairfilterdatex = findViewById(R.id.nextairfilterbutton);

        senddata = findViewById(R.id.submitbut);
        delbutton = findViewById(R.id.delbutton);

        servicecheck = findViewById(R.id.servicecheckbox);
        oilcheck = findViewById(R.id.oilcheckbox);
        filtercheck = findViewById(R.id.oilfiltercheckbox);
        aircheck = findViewById(R.id.airfiltercheckbox);

        mDbHelper = new RemindersDbAdapter(this);

        senddata.setOnClickListener(this);
        emailsend.setOnClickListener(this);
        servicedatex.setOnClickListener(this);
        oildatex.setOnClickListener(this);
        oilfilterdatex.setOnClickListener(this);
        nextservicedatex.setOnClickListener(this);
        nextoildatex.setOnClickListener(this);
        nextoilfilterdatex.setOnClickListener(this);
        airfilterdatex.setOnClickListener(this);
        nextairfilterdatex.setOnClickListener(this);


        Date todaydate = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(todaydate);


        mRowId = savedInstanceState != null ? savedInstanceState.getLong(RemindersDbAdapter.KEY_ROWID)
                : null;

        //IF ROWID =! GET FIELDS WHERE LISSTVIEW ROWID == DBRID
        populateFields();

        delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delx();
            }
        });

        senddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    insert();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivityt.this);
                    builder.setTitle(R.string.warning);
                    builder.setNegativeButton(R.string.ok,null);
                    builder.setMessage(R.string.fillfields);
                    builder.setIcon(R.drawable.warn);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        servicecheck.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {

                if(isChecked){

                    serviced.setText(formattedDate);
              nextservice.setText("");
              servicecheck.setEnabled(false);
              nextservice.setError("Enter Next Service Date");
                }
            }
        });


        oilcheck.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if(isChecked){
                    oilchange.setText(formattedDate);
                        nextoil.setText("");
                    oilcheck.setEnabled(false);
                    nextoil.setError("Enter Next Oil Check Date");
                }
            }

        });

        filtercheck.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                if(isChecked) {

                    oilfilter.setText(formattedDate);
                    nextoilfilter.setText("");
                    filtercheck.setEnabled(false);
                    nextoilfilter.setError("Enter Next Oil Filter Check Date");
                }
            }
        });

        aircheck.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {

                if (isChecked) {
                    airfilter.setText(formattedDate);
                    nextairfilter.setText("");
                    aircheck.setEnabled(false);
                    nextairfilter.setError("Enter Next Service Date");
                }
            }
        });


        emailsend.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean dialogShown = settings.getBoolean("dialogShown", false);

                if (!dialogShown) {

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("dialogShown", true);
                    editor.commit();


                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditCarActivityt.this);
                    LayoutInflater factory = LayoutInflater.from(EditCarActivityt.this);
                    final View view = factory.inflate(R.layout.sample, null);
                    builder.setView(view);
                    builder.setCancelable(false).setIcon(R.drawable.emailico).setTitle(R.string.sendreportt).setMessage(R.string.sendreportdescc).setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openemailchangedlg();
                        }

                    });
                    final androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();

                }
                if (dialogShown) {

                    openemailchangedlg();

                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(RemindersDbAdapter.KEY_ROWID, mRowId);
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
        populateFields();
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(RemindersDbAdapter.KEY_ROWID)
                    : null;

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        //NEXT SERVUCE
        String nexts = nextservice.getText().toString();
        if (TextUtils.isEmpty(nexts)) {
            nextservice.setError(getText(R.string.required));
            valid = false;
        }

        else {
            nextservice.setError(null);
        }
        //NEXT  OIL
        String nexto = nextoil.getText().toString();

        if (TextUtils.isEmpty(nexto)) {
            nextoil.setError(getText(R.string.required));
            valid = false;
        }

        else {
            nextoil.setError(null);
        }

        //NEXT  OIL fikter
        String nextf = nextoilfilter.getText().toString();

        if (TextUtils.isEmpty(nextf)) {
            nextoilfilter.setError(getText(R.string.required));
            valid = false;
        }

        else {
            nextoilfilter.setError(null);
        }


        //NEXT air filter
        String nexta = nextairfilter.getText().toString();

        if (TextUtils.isEmpty(nexta)) {
            nextairfilter.setError(getText(R.string.required));
            valid = false;
        } else {
            nextairfilter.setError(null);
        }

        return valid;
    }


    private void populateFields() {


        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchReminder(mRowId);
            startManagingCursor(reminder);
            vname.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.VEHICLE_NAME)));
            serviced.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.SERVICE_DATE)));
            oilchange.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.OIL_DATE)));
            oilfilter.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.FILTER_DATE)));
            airfilter.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.AIR_DATE)));

            nextservice.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.S_REMIND)));
            nextoil.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.O_REMIND)));
            nextoilfilter.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.FILTER_REMIND)));
            nextairfilter.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.A_REMIND)));

        }

    }

    @Override
    public void onClick(View v) {

        if (v == servicedatex) {

            // Get Current Date
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

            // Get Current Date
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

            // Get Current Date
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

            // Get Current Date
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

            // Get Current Date
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

            // Get Current Date
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
        boolean rowInserted = mDbHelper.updateReminder(mRowId, vname.getText().toString(), serviced.getText().toString(), nextservice.getText().toString(), oilchange.getText().toString(), nextoil.getText().toString(), oilfilter.getText().toString(), nextoilfilter.getText().toString(), airfilter.getText().toString(), nextairfilter.getText().toString());

        if(rowInserted==true){

            AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivityt.this);
            builder.setTitle(R.string.savedsuccess);
            builder.setNegativeButton(R.string.ok,null);
            builder.setMessage(R.string.savedsuccessdesc);
            builder.setIcon(R.drawable.tick);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivityt.this);
            builder.setTitle(R.string.savefailed);
            builder.setNegativeButton(R.string.ok,null);
            builder.setMessage(R.string.savefaileddesc);
            builder.setIcon(R.drawable.error);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
    private void delx(){


        new AlertDialog.Builder(this).setTitle(R.string.delvehi)
                .setMessage(R.string.delvehicledesc).setIcon(R.drawable.warn)
                .setPositiveButton(R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                boolean rowdeleted =   mDbHelper.deleteReminder(mRowId);

                                if(rowdeleted == true){

                                    vname.setText("");
                                    serviced.setText("");
                                    nextservice.setText("");
                                    oilfilter.setText("");
                                    oilchange.setText("");
                                    nextoil.setText("");
                                    nextoilfilter.setText("");
                                    nextairfilter.setText("");
                                    airfilter.setText("");
                                    dialog.dismiss();
                                    new AlertDialog.Builder(EditCarActivityt.this).setTitle(R.string.success).setCancelable(false)
                                            .setMessage(R.string.succesdesc).setIcon(R.drawable.tick)
                                            .setPositiveButton(R.string.ok,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            onBackPressed();
                                                            EditCarActivityt.this.finish();
                                                            dialog.dismiss();
                                                        }
                                                    }).create()
                                            .show();
                                }
                                else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivityt.this);
                                    builder.setTitle(R.string.warning);
                                    builder.setNegativeButton(R.string.ok,null);
                                    builder.setMessage(R.string.savefaileddesc);
                                    builder.setIcon(R.drawable.tick);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create()
                .show();


    }


    private void openemailchangedlg() {
        DialogModelReports xxx = new DialogModelReports();
        xxx.show(getSupportFragmentManager(), "xxx");
    }


    @Override
    public void xas(String email1) {

        final android.app.AlertDialog alertDialogx = new
                SpotsDialog.Builder().setContext(EditCarActivityt.this).build();
        alertDialogx.setMessage(getString(R.string.wait));
        alertDialogx.show();

        GMailSender.withAccount("carMinderTeam@gmail.com", "deathcomestousall")
                .withTitle("Vehicle Maintainance Report")
                .withBody("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                        "    <title>Demystifying Email Design</title>\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                        "</head>\n" +
                        "\n" +
                        "<body style=\"margin: 0; padding: 0;\">\n" +
                        "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                        "        <tr>\n" +
                        "            <td style=\"padding: 10px 0 30px 0;\">\n" +
                        "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border: 1px solid #cccccc; border-collapse: collapse;\">\n" +
                        "                    <tr>\n" +
                        "                        <td align=\"center\" bgcolor=\"#70bbd9\" style=\"padding: 40px 0 30px 0; color: #153643; font-size: 28px; font-weight: bold; font-family: Arial, sans-serif;\">\n" +
                        "                            <img src=\"https://i.imgur.com/CEg4cjD.png\" alt=\"Creating Email Magic\" width=\"250\" height=\"230\" style=\"display: block;\" />\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <tr>\n" +
                        "                        <td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                        "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                <tr>\n" +
                        "\n" +
                        "                                    <td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px;\">\n" +
                        "                                        <b>carMinder Maintainance Details of " + vname.getText().toString() + "</b>\n" +
                        "                                    </td>\n" +
                        "                                </tr>\n" +
                        "                                <tr>\n" +
                        "                                    <td style=\"padding: 20px 0 30px 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\n" +
                        "                                    </td>\n" +
                        "                                </tr>\n" +
                        "                                <tr>\n" +
                        "                                    <td>\n" +
                        "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                            <tr>\n" +
                        "                                                <td width=\"6540\" valign=\"top\">\n" +
                        "                                                    <table border=\"0\" cellpadding=\"5\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                                        <tr>\n" +
                        "                                                            <td>\n" +
                        "                                                                <img src=\"https://i.imgur.com/Mfr57qg.png\" alt=\"\" width=\"130\" height=\"120\" />\n" +
                        "                                                            </td>\n" +
                        "                                                            <td width=\"370\" valign=\"center\">\n" +
                        "                                                                <b>Last Service Date :</b> <i>" + serviced.getText().toString() + "</i><br>\n" +
                        "                                                                <hr style=\"width: 210px;margin-left: 0px;\">\n" +
                        "                                                                <b>Next Service Date :</b> <i>" + nextservice.getText().toString() + "</i>\n" +
                        "\n" +
                        "                                                            </td>\n" +
                        "\n" +
                        "                                                        </tr>\n" +
                        "\n" +
                        "                                                        <tr>\n" +
                        "                                                            <td>\n" +
                        "                                                                <img src=\"https://i.imgur.com/G5lNhVq.png\" alt=\"\" width=\"130\" height=\"120\" />\n" +
                        "                                                            </td>\n" +
                        "                                                            <td width=\"360\" valign=\"center\">\n" +
                        "                                                                <b>Last Engine Oil Refill Date :</b> <i>" + oilchange.getText().toString() + "</i><br>\n" +
                        "                                                                <hr style=\"width: 275px;margin-left: 0px;\">\n" +
                        "                                                                <b>Next Engine Oil Refill Date :</b> <i>" + nextoil.getText().toString() + "</i>\n" +
                        "\n" +
                        "                                                            </td>\n" +
                        "\n" +
                        "                                                        </tr>\n" +
                        "                                                        <tr>\n" +
                        "                                                            <td>\n" +
                        "                                                                <img src=\"https://i.imgur.com/PR2b5x5.png\" alt=\"\" width=\"130\" height=\"120\" />\n" +
                        "                                                            </td>\n" +
                        "                                                            <td width=\"360\" valign=\"center\">\n" +
                        "                                                                <b>Last Engine Oil Filter Replacement Date :</b> <i>" + oilfilter.getText().toString() + "</i><br>\n" +
                        "                                                                <hr style=\"width: 370px;margin-left: 0px;\">\n" +
                        "                                                                <b>Next Engine Oil Filter Replacement Date :</b> <i>" + nextoil.getText().toString() + "</i>\n" +
                        "\n" +
                        "                                                            </td>\n" +
                        "\n" +
                        "                                                        </tr>\n" +
                        "                                                        <tr>\n" +
                        "                                                            <td>\n" +
                        "                                                                <img src=\"https://i.imgur.com/1wVBH1q.png\" alt=\"\" width=\"130\" height=\"120\" />\n" +
                        "                                                            </td>\n" +
                        "                                                            <td width=\"360\" valign=\"center\">\n" +
                        "                                                                <b>Last Air Filter Replacement Date :</b> <i>" + airfilter.getText().toString() + "</i><br>\n" +
                        "                                                                <hr style=\"width: 315px;margin-left: 0px;\">\n" +
                        "                                                                <b>Next Air Filter Replacement Date :</b> <i>" + nextairfilter.getText().toString() + "</i>\n" +
                        "\n" +
                        "                                                            </td>\n" +
                        "\n" +
                        "                                                        </tr>\n" +
                        "                                                    </table>\n" +
                        "                                                </td>\n" +
                        "\n" +
                        "\n" +
                        "                                            </tr>\n" +
                        "                                        </table>\n" +
                        "                                    </td>\n" +
                        "                                </tr>\n" +
                        "                            </table>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <tr>\n" +
                        "                        <td bgcolor=\"#ee4c50\" style=\"padding: 30px 30px 30px 30px;\">\n" +
                        "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                <tr>\n" +
                        "                                    <td style=\"color: #ffffff; font-family: Arial, sans-serif; font-size: 14px;\" width=\"75%\">\n" +
                        "                                        &copy; carMinder, 2020<br/>\n" +
                        "                                        <a href=\"#\" style=\"color: #ffffff;\">\n" +
                        "\n" +
                        "                                        </a> This Email was sent to you By carMinder.\n" +
                        "                                    </td>\n" +
                        "\n" +
                        "                                </tr>\n" +
                        "                            </table>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                </table>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>")
                .withSender(getString(R.string.app_name))
                .toEmailAddress(email1)
                .withListenner(new GmailListener() {
                    @Override
                    public void sendSuccess() {
                        alertDialogx.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivityt.this);
                        builder.setTitle(R.string.success);
                        builder.setNegativeButton(R.string.ok, null);
                        builder.setMessage(R.string.reportsentdesc);
                        builder.setIcon(R.drawable.emailsent);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                    @Override
                    public void sendFail(String err) {

                        alertDialogx.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditCarActivityt.this);
                        builder.setTitle(R.string.error);
                        builder.setNegativeButton(R.string.ok, null);
                        builder.setMessage(R.string.errordesc);
                        builder.setIcon(R.drawable.error);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                })
                .send();


    }


}
