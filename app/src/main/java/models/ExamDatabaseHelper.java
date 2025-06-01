package models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ExamDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Exam";

    // Table name: Quizzes.
    private static final String TABLE_QUIZZES= "Quizzes";
    private static final String COLUMN_QUESTION_ID = "Question_id";
    private static final String COLUMN_EXAM_ID_FOREIGN = "Exam_id";
    private static final String COLUMN_QUESTION = "Question";
    private static final String COLUMN_OPTION_1= "Option_1";
    private static final String COLUMN_OPTION_2 = "Option_2";
    private static final String COLUMN_OPTION_3 = "Option_3";
    private static final String COLUMN_OPTION_4 = "Option_4";
    private static final String COLUMN_ANS = "Ans";

    // Table name: Exam
    private static final String TABLE_EXAM= "Exam";
    private static final String COLUMN_EXAM_ID = "Exam_id";
    private static final String COLUMN_EXAM_TITLE = "Exam_title";
    private static final String COLUMN_EXAM_TOTAL_QUESTIONS = "Exam_total_quetions";


    public ExamDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script_table_quizzes = "CREATE TABLE " + TABLE_QUIZZES + "("
                + COLUMN_QUESTION_ID +" INTEGER PRIMARY KEY,"
                + COLUMN_QUESTION + " TEXT,"
                + COLUMN_EXAM_ID_FOREIGN +" INTEGER,"
                + COLUMN_OPTION_1 + " TEXT,"
                + COLUMN_OPTION_2 + " TEXT,"
                + COLUMN_OPTION_3 + " TEXT,"
                + COLUMN_OPTION_4 + " TEXT,"
                + COLUMN_ANS + " INTEGER" +")";

        String script_table_exam = "CREATE TABLE " + TABLE_EXAM + "("
                + COLUMN_EXAM_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_EXAM_TITLE + " TEXT,"
                + COLUMN_EXAM_TOTAL_QUESTIONS +" INTEGER" + ")";

        db.execSQL(script_table_exam);
        db.execSQL(script_table_quizzes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("Drop TABLE IF EXISTS "+ TABLE_QUIZZES);
        db.execSQL("Drop TABLE IF EXISTS "+ TABLE_EXAM);
        onCreate(db);
    }

    public void addExam(String title, int totalQuestions){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXAM_TITLE, title);
        values.put(COLUMN_EXAM_TOTAL_QUESTIONS, totalQuestions);

        db.insert(TABLE_EXAM, null, values);
        db.close();

    }
    public void addQuestion(Question question, int examId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question.getQuestion());
        values.put(COLUMN_EXAM_ID_FOREIGN, examId);
        values.put(COLUMN_OPTION_1, question.getOption1());
        values.put(COLUMN_OPTION_2, question.getOption2());
        values.put(COLUMN_OPTION_3, question.getOption3());
        values.put(COLUMN_OPTION_4, question.getOption4());
        values.put(COLUMN_ANS, question.getCorrectAnswer());

        // Inserting Row
        db.insert(TABLE_QUIZZES, null, values);
        // Closing database connection
        db.close();
    }

    public int getIdExamOnTitle(String examTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Exam_id from Exam where Exam_title = ?",new String[]{examTitle});
        cursor.moveToFirst();
        int examId = cursor.getInt(0);
        cursor.close();
        return examId;
    }

    public ArrayList<Question> getQuestionFromExamTitle(String examTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        int examId = getIdExamOnTitle(examTitle);
        Cursor cursor = db.rawQuery("Select * from Quizzes where Exam_id = ?",
                new String[]{String.valueOf(examId)});
        ArrayList<Question> dataList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Extract data from the current row
                int question_id = cursor.getInt(0);
                String question = cursor.getString(1);
                String option1 = cursor.getString(3);
                String option2 = cursor.getString(4);
                String option3 = cursor.getString(5);
                String option4 = cursor.getString(6);
                int ans = Integer.parseInt(cursor.getString(7));

                // Create a new object and add it to the list
                Question item = new Question(question_id,question,option1,option2,option3,option4,ans);
                dataList.add(item);
            } while (cursor.moveToNext());
        }
        return dataList;
    }
}
