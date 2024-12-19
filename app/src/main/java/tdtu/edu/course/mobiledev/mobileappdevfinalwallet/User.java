package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

public class User {
    private String name;
    private String phone;
    private String password;
    private double balance;

    public User() {
    }

    public User(String name, String phone, String password, double balance) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
