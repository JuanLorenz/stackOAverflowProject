package data;

import java.util.Date;

public class Transaction {
    private int transactionID;
    private User user;
    private Equipment equipment;
    private Date dateBorrowed;
    private Date dateDue;
    private Date dateReturned;

    public Transaction(int transactionID, User user, Equipment equipment, Date dateBorrowed, Date dateDue) {
        this.transactionID = transactionID;
        this.user = user;
        this.equipment = equipment;
        this.dateBorrowed = dateBorrowed;
        this.dateDue = dateDue;
        dateReturned = null;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Date getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(Date dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public Date getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(Date dateReturned) {
        this.dateReturned = dateReturned;
    }
}
