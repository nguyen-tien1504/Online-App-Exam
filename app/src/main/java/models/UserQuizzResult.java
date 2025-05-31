package models;

public class UserQuizzResult{
    private int Exam_id;
    private String Exam_title;
    private String User_Total_Point;
    private String Exam_Total_Questions;

    public UserQuizzResult(int exam_id, String exam_title, String user_Total_Point, String exam_Total_Questions) {
        Exam_id = exam_id;
        Exam_title = exam_title;
        User_Total_Point = user_Total_Point;
        Exam_Total_Questions = exam_Total_Questions;
    }

    public int getExam_id() {
        return Exam_id;
    }

    public String getExam_title() {
        return Exam_title;
    }

    public String getUser_Total_Point() {
        return User_Total_Point;
    }

    public String getExam_Total_Questions() {
        return Exam_Total_Questions;
    }

}
