package utilities.service;

import data.equipment.Equipment;
import data.Transaction;
import data.User;
import utilities.database.EquipmentDAO;
import utilities.database.TransactionDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TransactionService {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean processBorrow(Equipment e, User u) {
        // check if quantity is not 0
        if (e.getAvailableQty() <= 0) {
            System.err.println("Item out of stock!");
            return false;
        }

        // create the new object
        Transaction t = new Transaction(
                0,              // temporary id
                u,
                e,
                LocalDate.now(),
                null
        );

        // update availableQty
        e.setAvailableQty(e.getAvailableQty() - 1);

        // update db
        boolean updateEquipment = equipmentDAO.updateQuantity(e.getEquipmentID(), e.getTotalQty(), e.getAvailableQty());
        boolean addTransaction = transactionDAO.save(t);

        // returns false when one of them failed to update/save
        return updateEquipment && addTransaction;
    }

    public boolean processReturn(Transaction t) {
        if (t.getDateReturned() != null) {
            System.err.println("Item already returned!");
            return false;
        }

        t.setDateReturned(LocalDate.now());

        Equipment e = t.getEquipment();
        e.setAvailableQty(e.getAvailableQty() + 1);

        // Let's capture the exact results!
        System.out.println("--- PROCESSING RETURN ---");
        System.out.println("Attempting to update Equipment ID: " + e.getEquipmentID());

        boolean updateEquipment = equipmentDAO.updateQuantity(e.getEquipmentID(), e.getTotalQty(), e.getAvailableQty());
        System.out.println("Did Equipment update in DB? : " + updateEquipment);

        boolean updateTransaction = transactionDAO.updateReturn(t.getTransactionID(), t.getDateReturned());
        System.out.println("Did Transaction update in DB? : " + updateTransaction);
        System.out.println("-------------------------");

        return updateEquipment && updateTransaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }

    public List<Transaction> getUserHistory(int userId) {
        return transactionDAO.findAllByUserId(userId);
    }

    public List<Transaction> getUserActiveTransaction(int userId) {
        return transactionDAO.findActiveByUserId(userId);
    }


    public Map<String, Integer> getHistoryCountByCategory(int userId) {
        return transactionDAO.getHistoryCountByCategory(userId);
    }
}