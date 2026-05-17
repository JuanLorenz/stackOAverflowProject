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
import utilities.service.TransactionService;
import utilities.service.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeUserViewModel {

    private final ObservableList<Transaction> borrowedItems = FXCollections.observableArrayList();;
    private Map<String, Integer> historyCounts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();

    public ObservableList<Transaction> getBorrowedItems() {
        return borrowedItems;
    }

    public Map<String, Integer> getHistoryCounts() {
        return historyCounts;
    }

    public void loadData() {
        User currentUser = SerializeManager.deserializeUser();
        if (currentUser == null) return;

        // 1. Fetch current active items
        List<Transaction> activeTransactions = transactionService.getUserActiveTransaction(currentUser.getId());

        Map<String, Integer> rawCounts = transactionService.getHistoryCountByCategory(currentUser.getId());
        historyCounts.clear();
        if (rawCounts != null) {
            rawCounts.forEach((category, count) -> {
                if (category != null) {
                    historyCounts.put(category.trim(), count);
                }
            });
        }


        // 2. Delegate "Status Logic" to UserService
        // If the service changed the user (blocked/unblocked), we re-serialize
        if (userService.syncUserStatus(currentUser, activeTransactions)) {
            SerializeManager.serializeUser(currentUser);
        }

        // 3. Update UI
        borrowedItems.setAll(activeTransactions);
    }

    public void returnEquipment(Transaction transaction) {
        // 1. Use TransactionService to handle the return
        boolean success = transactionService.processReturn(transaction);

        if (success) {

            String returnedCategory = transaction.getEquipment().getCategory().trim();
            for (String mapKey : historyCounts.keySet()) {
                if (mapKey.equalsIgnoreCase(returnedCategory)) {
                    historyCounts.compute(mapKey, (k, currentCount) -> Math.max(0, currentCount - 1));
                    break;
                }
            }

            // 2. Immediately re-sync user status
            loadData();
        }
    }
}