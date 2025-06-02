package models;

public class UserQuizzResult{
    private int Exam_id;
    private String Exam_title;
    private int User_Total_Point;
    private int Exam_Total_Questions;

    public UserQuizzResult(int exam_id, String exam_title, int user_Total_Point, int exam_Total_Questions) {
        Exam_id = exam_id;
        Exam_title = exam_title;
        User_Total_Point = user_Total_Point;
        Exam_Total_Questions = exam_Total_Questions;
    }

    public UserQuizzResult(String exam_title, int exam_Total_Questions) {
        Exam_title = exam_title;
        Exam_Total_Questions = exam_Total_Questions;
    }

    public int getExam_id() {
        return Exam_id;
    }

    public String getExam_title() {
        return Exam_title;
    }

    public int getUser_Total_Point() {
        return User_Total_Point;
    }

    public int getExam_Total_Questions() {
        return Exam_Total_Questions;
    }

}
