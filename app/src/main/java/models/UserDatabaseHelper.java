package models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Online Exam App";

    // Table name: User.
    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "User_id";
    private static final String COLUMN_USER_FIRSTNAME = "User_Firstname";
    private static final String COLUMN_USER_LASTNAME = "User_Lastname";
    private static final String COLUMN_USER_EMAIL = "User_Email";
    private static final String COLUMN_USER_PASSWORD = "User_Password";
    private static final String COLUMN_USER_TOTAL_QUESTIONS = "User_Total_Questions";
    private static final String COLUMN_USER_TOTAL_POINTS = "User_Total_Points";


    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID +" INTEGER PRIMARY KEY,"
                + COLUMN_USER_FIRSTNAME + " TEXT,"
                + COLUMN_USER_LASTNAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_TOTAL_QUESTIONS + " INTEGER DEFAULT 0,"
                + COLUMN_USER_TOTAL_POINTS + " INTEGER DEFAULT 0" +")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("Drop TABLE IF EXISTS "+ TABLE_USER);
        onCreate(db);
    }

    public void createDefaultNotesIfNeed() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addUser(User user){
        // Creating Base64 encoder and decoder instances
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedPassword = encoder.encodeToString(user.getPassword().getBytes());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_FIRSTNAME, user.getFirstName());
        values.put(COLUMN_USER_LASTNAME, user.getLastName());
        values.put(COLUMN_USER_EMAIL,user.getEmail());
        values.put(COLUMN_USER_PASSWORD,encodedPassword);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        // Closing database connection
        db.close();
    }

    public User getUserByPasswordAndEmail(User user){
        //Base64.Decoder decoder = Base64.getDecoder();
        String encodedPassword = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && user.getPassword()!=null) {
            encodedPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        }
        SQLiteDatabase db = this.getReadableDatabase();
        User findUser = null;
        Cursor cursor;
        if(user.getPassword()==null){
            cursor = db.rawQuery("Select User_Email from User where User_Email = ?",
                    new String[]{user.getEmail()});
            if(cursor.moveToFirst()) {
                findUser = new User(cursor.getString(0));
            }
        }else {
            cursor = db.query(TABLE_USER, new String[]{COLUMN_USER_FIRSTNAME,COLUMN_USER_LASTNAME,COLUMN_USER_ID,COLUMN_USER_EMAIL},
                    COLUMN_USER_EMAIL+"=? and "+COLUMN_USER_PASSWORD+"=?",
                    new String[]{String.valueOf(user.getEmail()),encodedPassword},null,null,null);
            if( cursor.moveToFirst()) {
                findUser = new User(cursor.getString(0),cursor.getString(1),cursor.getInt(2), cursor.getString(3));
            }
        }
        if(cursor.getCount() == 0) return null;
        cursor.close();
        return findUser;
    }

    public User getTotalQuestionsAndPointsFromEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select User_Total_Questions, User_Total_Points from User where User_Email=?",
                new String[]{email});
        cursor.moveToFirst();
        User userFind = new User(cursor.getInt(0),cursor.getInt(1));
        cursor.close();
        return userFind;
    }
}
