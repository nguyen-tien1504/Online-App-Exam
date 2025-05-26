package models;

public class User {
    private int Id;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;

    public User(String email) {
        Email = email;
    }

    public User(String firstName, String lastName, String email, String password) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
    }

    public User(String email, String password) {
        Email = email;
        Password = password;
    }

    public User(String firstName, String lastName, String email) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
    }

    public User(int id, String email) {
        Id = id;
        Email = email;
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
