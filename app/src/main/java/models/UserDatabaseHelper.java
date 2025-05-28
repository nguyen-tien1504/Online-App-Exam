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
    private static final String DATABASE_NAME = "User.db";

    // Table name: User.
    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "User_id";
    private static final String COLUMN_USER_FIRSTNAME = "User_Firstname";
    private static final String COLUMN_USER_LASTNAME = "User_Lastname";
    private static final String COLUMN_USER_EMAIL = "User_Email";
    private static final String COLUMN_USER_PASSWORD = "User_Password";


    //Table name: User_Result
    private static final String TABLE_USER_RESULT = "User_Result";
    private static final String COLUMN_USER_RESULT_ID = "User_Result_id";
    private static final String COLUMN_USER_ID_FOREIGN = "User_id";

    private static final String COLUMN_USER_EXAM_TITLE_FOREIGN = "User_Exam_Title_Foreign";
    private static final String COLUMN_USER_EXAM_POINT = "User_Total_Points";
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String userScript = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID +" INTEGER PRIMARY KEY,"
                + COLUMN_USER_FIRSTNAME + " TEXT,"
                + COLUMN_USER_LASTNAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" +")";

        String userResultScript = "CREATE TABLE " + TABLE_USER_RESULT + "("
                + COLUMN_USER_RESULT_ID +" INTEGER PRIMARY KEY,"
                + COLUMN_USER_ID_FOREIGN +" INTEGER,"
                + COLUMN_USER_EXAM_TITLE_FOREIGN + " TEXT,"
                + COLUMN_USER_EXAM_POINT + " INTEGER" +")";
        db.execSQL(userScript);
        db.execSQL(userResultScript);
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

    public int getTotalQuestionsAndPointsFromEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select SUM(User_Total_Points) from User_Result where User_id= (Select User_id from User where User_Email=?)",
                new String[]{email});
        if (!cursor.moveToFirst()) return 0;
        int userTotalPoints = cursor.getInt(0);
        cursor.close();
        return userTotalPoints;
    }

    public void addUserPointAndQuestionExam(String email, String examTitle,int point){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Insert into User_Result (User_id,User_Exam_Title_Foreign,User_Total_Points) values((Select User_id from User where User_Email=?), ?,?)",
                new String[]{email, examTitle, String.valueOf(point)});
        db.close();
    }
}
