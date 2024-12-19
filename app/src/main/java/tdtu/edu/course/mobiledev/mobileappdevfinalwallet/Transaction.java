package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String account;
    private String category;
    private String amount;
    private String note;
    private String date;
    private boolean isIncome;

    // Constructor
    public Transaction() {
        // Default constructor required for calls to DataSnapshot.getValue(Transaction.class)
    }

    // Constructor with parameters
    public Transaction(String account, String category, String amount, String note, String date, boolean isIncome) {
        this.account = account;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.isIncome = isIncome;
    }

    // Getters and Setters
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }
}

