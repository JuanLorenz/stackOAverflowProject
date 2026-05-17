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
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
                currentUser.setBlockedOn(LocalDate.now());
                SerializeManager.serializeUser(currentUser);
            }else if(currentUser.isBlocked()){
                LocalDate dateUserBlocked = currentUser.getBlockedDate();

                if(dateUserBlocked != null){
                    long difference = ChronoUnit.DAYS.between(dateUserBlocked, today);

                    if(difference >= 7 && !hasOverdueItems){
                        System.out.println("User is no longer blocked!");
                        userDAO.updateUserBlockStatus(currentUser.getId(), false);
                        currentUser.setUserAccessStatus(false);
                        currentUser.setBlockedOn(null);
                        SerializeManager.serializeUser(currentUser);
                    }else if(difference >= 7 && hasOverdueItems){
                        System.out.println("Return remaining overdue items");
                    }
                }


            }

            borrowedItems.setAll(activeTransactions);
        }
    }

    public void returnEquipment(Transaction transaction, User currentUser) {
        Equipment equipment = transaction.getEquipment();
        boolean transactionUpdated = transactionDAO.updateReturn(transaction.getTransactionID(), LocalDate.now());

        if (transactionUpdated) {
            int newAvailableQty = equipment.getAvailableQty() + 1;
            equipment.setAvailableQty(newAvailableQty);
            equipmentDAO.updateQuantity(equipment.getEquipmentID(), equipment.getTotalQty(), newAvailableQty);
            borrowedItems.remove(transaction); // Removes from UI
        }
    }
}