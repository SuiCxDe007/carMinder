package com.suicxde.carminder.Receivers;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.suicxde.carminder.Models.Data;
import com.suicxde.carminder.Database.RemindersDbAdapter;
import com.suicxde.carminder.Activities.EditCarActivityt;
import com.suicxde.carminder.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.suicxde.carminder.Database.RemindersDbAdapter.FILTER_REMIND;
import static com.suicxde.carminder.Database.RemindersDbAdapter.KEY_ROWID;
import static com.suicxde.carminder.Database.RemindersDbAdapter.O_REMIND;
import static com.suicxde.carminder.Database.RemindersDbAdapter.S_REMIND;
import static com.suicxde.carminder.Database.RemindersDbAdapter.VEHICLE_NAME;
import static com.suicxde.carminder.Database.RemindersDbAdapter.A_REMIND;
public class OnAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = ComponentInfo.class.getCanonicalName();


	@Override	
	public void onReceive(Context context, Intent intent) {

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        ArrayList<Integer> notifiedVID = new ArrayList<>();
		ArrayList<String> results = new ArrayList<>();
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		RemindersDbAdapter dbAdapter = new RemindersDbAdapter(context);
		Cursor cursor = dbAdapter.getDates();
		ArrayList<Data> data = new ArrayList<>();
		if (cursor.moveToPosition(0)) {
			do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ROWID));
				String VName = cursor.getString(cursor.getColumnIndex(VEHICLE_NAME));
				String sRemind = cursor.getString(cursor.getColumnIndex(S_REMIND));
				String oRemind = cursor.getString(cursor.getColumnIndex(O_REMIND));
				String fRemind = cursor.getString(cursor.getColumnIndex(FILTER_REMIND));
                String aRemind = cursor.getString(cursor.getColumnIndex(A_REMIND));
                data.add(new Data(id, VName, sRemind, oRemind, fRemind, aRemind));
			} while (cursor.moveToNext());
		}

		for (Data datex : data) {

			String vname = datex.getVehicleName();

			String sdate = datex.getSerRem();
			String odate = datex.getOilRem();
			String fdate = datex.getFilRem();
            String adate = datex.getAirRem();

			SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
			Date sdateobj = null;
			Date odateobj = null;
			Date fdateobj = null;
            Date adateobj = null;

			try {
				sdateobj = curFormater.parse(sdate);
				odateobj = curFormater.parse(odate);
				fdateobj = curFormater.parse(fdate);
                adateobj = curFormater.parse(adate);

				//TODO How to get the application current language and setlocale for notification too ::>>ask sir !!!???

				if( new Date().after(sdateobj)){

                    results.add("Your Vehicle" + " " + vname + " is Required to Be Serviced! Please Update carMinder Once Vehicle is Serrviced!");
                    notifiedVID.add(datex.getId());
				}

				if( new Date().after(odateobj)){

                    results.add("Your Vehicle" + " " + vname + "'s Engine Oil is Required to be Changed.  Please Update carMinder Once Oil is Changed!");
                    notifiedVID.add(datex.getId());
				}
				if( new Date().after(fdateobj)){

                    results.add("Your Vehicle" + " " + vname + "'s Engine Oil Filter is Required to be Changed.  Please Update carMinder Once Filter is Changed!");
                    notifiedVID.add(datex.getId());
				}
                if (new Date().after(adateobj)) {

                    results.add("Your Vehicle" + " " + vname + "'s Air Filter is Required to be Changed.  Please Update carMinder Once Filter is Changed!");
                    notifiedVID.add(datex.getId());
                }

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		int i = 0;
		for (String st : results) {

            int currentVID = notifiedVID.get(results.indexOf(st));
            Intent intentx = new Intent(context, EditCarActivityt.class);
            intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentx.putExtra(KEY_ROWID, (long) currentVID);

            PendingIntent contentIntent = PendingIntent.getActivity(context, i + 3, intentx, PendingIntent.FLAG_UPDATE_CURRENT);

			Notification notification = new NotificationCompat.Builder(context, "CH1")
					.setSmallIcon(R.drawable.rem)
                    .setContentTitle("carMinder")
                    .setContentText("Your Vehicle Needs Attention!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(st)).setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER).setAutoCancel(true).setContentIntent(contentIntent)
					.build();
			notificationManager.notify(i, notification);
			i += 1;

        }
    }
}
