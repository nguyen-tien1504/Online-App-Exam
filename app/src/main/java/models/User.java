package models;

public class User {
    private int Id;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private int TotalQuestions;
    private int TotalPoints;
    public User(String email) {
        Email = email;
    }

    public User(String firstName, String lastName, String email, String password) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
    }

    public int getTotalQuestions() {
        return TotalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        TotalQuestions = totalQuestions;
    }

    public int getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        TotalPoints = totalPoints;
    }

    public User(int totalQuestions, int totalPoints) {
        TotalQuestions = totalQuestions;
        TotalPoints = totalPoints;
    }

    public User(String firstName, String lastName, int id, String email) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
    }

    public User(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }
    public String getPassword() {
        return Password;
    }
}
