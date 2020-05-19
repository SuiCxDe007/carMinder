package com.suicxde.carminder.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class RemindersDbAdapter {


    private static final String DATABASE_NAME = "Vehicles.db";
    private static final String DATABASE_TABLE = "Vehicle";
    private static final int DATABASE_VERSION = 1;
    public static final String VEHICLE_NAME = "VehicleName";
    public static final String SERVICE_DATE = "ServiceDate";
    public static final String S_REMIND = "SRemindAgain";
    public static final String OIL_DATE = "EngineOil";
    public static final String O_REMIND = "ERemindAgain";
    public static final String FILTER_DATE = "OilFilter";
    public static final String FILTER_REMIND = "ORemindAgain";
    public static final String AIR_DATE = "AirFilter";
    public static final String A_REMIND = "ARemindAgain";
    public static final String KEY_ROWID = "_id";
    private static final String TAG = "SUICXDE";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public long addReminder(String vehicle, String[] dates) {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(VEHICLE_NAME, vehicle);
        cv.put(SERVICE_DATE, dates[0]);
        cv.put(S_REMIND, dates[4]);
        cv.put(OIL_DATE, dates[1]);
        cv.put(O_REMIND, dates[5]);
        cv.put(FILTER_DATE, dates[2]);
        cv.put(FILTER_REMIND, dates[6]);
        cv.put(AIR_DATE, dates[7]);
        cv.put(A_REMIND, dates[8]);
        long result = mDb.insert(DATABASE_TABLE, null, cv);
        mDb.close();
        return result;
    }

    public Cursor getVehicleNames() {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
        return mDb.query(DATABASE_TABLE, new String[]{VEHICLE_NAME}, null, null, null, null, null);
    }


    public Cursor getDates(){
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, VEHICLE_NAME, S_REMIND, O_REMIND, FILTER_REMIND, A_REMIND},
                null, null, null, null, null);
    }


    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + VEHICLE_NAME + " text not null, "
                    + SERVICE_DATE + " text not null, "
                    + S_REMIND + " text not null, "
                    + OIL_DATE + " text not null, "
                    + O_REMIND + " text not null, "
                    + FILTER_DATE + " text not null, "
                    + FILTER_REMIND + " text not null, "
                    + AIR_DATE + " text not null, "
                    + A_REMIND + " text not null);";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }


    public RemindersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }



    public boolean deleteReminder(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllReminders() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, VEHICLE_NAME,
                SERVICE_DATE, S_REMIND, OIL_DATE, O_REMIND, FILTER_DATE, FILTER_REMIND, AIR_DATE, A_REMIND}, null, null, null, null, null);
    }

    public Cursor fetchReminder(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, VEHICLE_NAME,
                                SERVICE_DATE, S_REMIND, OIL_DATE, O_REMIND, FILTER_DATE, FILTER_REMIND, AIR_DATE, A_REMIND}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public boolean updateReminder(long rowId, String Vehiclename, String ServiceDate, String SRemind, String oildate, String Oilremind, String Filterdate, String Filterremind, String Airdate, String airremind) {
        ContentValues args = new ContentValues();
        args.put(VEHICLE_NAME, Vehiclename);
        args.put(SERVICE_DATE, ServiceDate);
        args.put(S_REMIND, SRemind);
        args.put(OIL_DATE, oildate);
        args.put(O_REMIND, Oilremind);
        args.put(FILTER_DATE, Filterdate);
        args.put(FILTER_REMIND, Filterremind);
        args.put(AIR_DATE, Airdate);
        args.put(A_REMIND, airremind);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


}