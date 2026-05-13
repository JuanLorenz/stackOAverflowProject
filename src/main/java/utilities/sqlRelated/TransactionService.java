package utilities.sqlRelated;

import data.Equipment;
import data.Transaction;
import data.User;
import utilities.daoRelated.EquipmentDAO;
import utilities.daoRelated.TransactionDAO;

import java.time.LocalDate;

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
        // check if dateReturned is null
        if (t.getDateReturned() != null) {
            System.err.println("Item already returned!");
            return false;
        }

        // adds dateReturned
        t.setDateReturned(LocalDate.now());

        // update equipment's availableQty
        Equipment e = t.getEquipment();
        e.setAvailableQty(e.getAvailableQty() + 1);

        // update db
        boolean updateEquipment = equipmentDAO.updateQuantity(e.getEquipmentID(), e.getTotalQty(), e.getAvailableQty());
        boolean updateTransaction = transactionDAO.updateReturn(t.getTransactionID(), t.getDateReturned());

        // returns false when one of them failed to update
        return updateEquipment && updateTransaction;
    }
}
