package com.suicxde.carminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.suicxde.carminder.Activities.EditCarActivityt;
import com.suicxde.carminder.Database.RemindersDbAdapter;

public class ReminderService extends WakeReminderIntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    void doReminderWork(Intent intent) {
        Log.d("ReminderService", "Doing work.");
        Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

        NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, EditCarActivityt.class);
        notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification note=new Notification(android.R.drawable.stat_sys_warning, getString(R.string.addvehidesc), System.currentTimeMillis());

        note.defaults |= Notification.DEFAULT_SOUND;
        note.flags |= Notification.FLAG_AUTO_CANCEL;

        int id = (int)((long)rowId);
        mgr.notify(id, note);


    }
}
