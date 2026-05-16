package data;

import data.equipment.Equipment;

import java.time.LocalDate;

public class Transaction {
    private int transactionID;
    private User user;
    private Equipment equipment;
    private LocalDate dateBorrowed;
    private LocalDate dueDate;
    private LocalDate dateReturned;

    public Transaction(int transactionID, User user, Equipment equipment, LocalDate dateBorrowed, LocalDate dateReturned) {
        this.transactionID = transactionID;
        this.user = user;
        this.equipment = equipment;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
        //REMOVED INIT FOR dateReturned SINCE IT DOESN'T MAKE SENSE
        dueDate = dateBorrowed.plusDays(30);
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

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public LocalDate getDateReturned() {
        return dateReturned;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDateReturned(LocalDate dateReturned) {
        this.dateReturned = dateReturned;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(long days) {
        dueDate = dueDate.plusDays(days);
    }
}
