package com.suicxde.carminder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.suicxde.carminder.Database.RemindersDbAdapter;
import com.suicxde.carminder.R;
import java.util.ArrayList;

public class ViewVehiclesActivity extends AppCompatActivity {
    private RemindersDbAdapter dbAdapter;
    ArrayList <String> listItems;
    ListView userlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_view_vehicles);


        dbAdapter = new RemindersDbAdapter(getApplicationContext());
        dbAdapter.getVehicleNames();
        userlist = findViewById(R.id.vehicles);
        listItems = new ArrayList<>();
        fillData();

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent x = new Intent(ViewVehiclesActivity.this, EditCarActivityt.class);
            x.putExtra(RemindersDbAdapter.KEY_ROWID, l);
            startActivity(x);
        }
    });


    }

    @Override
    public void onBackPressed() {
        super.onResume();
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        fillData();

    }

    private void fillData() {
        Cursor remindersCursor = dbAdapter.fetchAllReminders();
        startManagingCursor(remindersCursor);

        String[] from = new String[]{RemindersDbAdapter.VEHICLE_NAME};

        int[] to = new int[]{R.id.text1};

        SimpleCursorAdapter reminders =
                new SimpleCursorAdapter(this, R.layout.reminder_row, remindersCursor, from, to);
        userlist.setAdapter(reminders);
        int lcount = userlist.getAdapter().getCount();

        if (lcount == 0) {

            new AlertDialog.Builder(this).setTitle(R.string.warning).setCancelable(false)
                    .setMessage(R.string.novehicles).setIcon(R.drawable.warn)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();

                                }
                            })
                    .create().show();

        }
    

    }


}
