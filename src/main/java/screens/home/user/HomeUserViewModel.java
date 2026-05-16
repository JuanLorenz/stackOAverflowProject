package screens.home.user;

import data.Transaction;
import data.User;
import data.equipment.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.database.EquipmentDAO;
import utilities.database.TransactionDAO;
import utilities.database.UserDAO;
import utilities.manager.SerializeManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeUserViewModel {

    private final ObservableList<Transaction> borrowedItems;
    private Map<String, Integer> historyCounts;

    private final TransactionDAO transactionDAO;
    private final EquipmentDAO equipmentDAO;
    private final UserDAO userDAO;

    public HomeUserViewModel() {
        this.borrowedItems = FXCollections.observableArrayList();
        this.historyCounts = new HashMap<>();
        this.transactionDAO = new TransactionDAO();
        this.equipmentDAO = new EquipmentDAO();
        this.userDAO = new UserDAO();
    }

    public ObservableList<Transaction> getBorrowedItems() {
        return borrowedItems;
    }

    public Map<String, Integer> getHistoryCounts() {
        return historyCounts;
    }

    public void loadData() {
        User currentUser = SerializeManager.deserializeUser();

        if (currentUser != null) {
            historyCounts = transactionDAO.getHistoryCountByCategory(currentUser.getId());
            List<Transaction> activeTransactions = transactionDAO.findActiveByUserId(currentUser.getId());

            // Overdue Logic
            boolean hasOverdueItems = false;
            LocalDate today = LocalDate.now();

            for (Transaction t : activeTransactions) {
                // Adjust this rule to fit your exact due date policy
                LocalDate dueDate = t.getDateBorrowed().plusDays(3);
                if (today.isAfter(dueDate)) {
                    hasOverdueItems = true;
                    break;
                }
            }

            if (hasOverdueItems && !currentUser.isBlocked()) {
                System.out.println("Overdue items found! Blocking user.");
                userDAO.updateUserBlockStatus(currentUser.getId(), true);
                currentUser.setUserAccessStatus(true);
                SerializeManager.serializeUser(currentUser);
            }

            borrowedItems.setAll(activeTransactions);
        }
    }

    public void returnEquipment(Transaction transaction) {
        Equipment equipment = transaction.getEquipment();
        boolean transactionUpdated = transactionDAO.updateReturn(transaction.getTransactionID(), LocalDate.now());

        if (transactionUpdated) {
            int newAvailableQty = equipment.getAvailableQty() + 1;
            equipmentDAO.updateQuantity(equipment.getEquipmentID(), equipment.getTotalQty(), newAvailableQty);
            borrowedItems.remove(transaction); // Removes from UI
        }
    }
}