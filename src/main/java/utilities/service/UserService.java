package utilities.service;

import data.Transaction;
import data.User;
import utilities.database.BlockedUserDAO;
import utilities.database.UserDAO;
import java.time.LocalDate;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final BlockedUserDAO blockedUserDAO = new BlockedUserDAO();
    private static final int PENALTY_DAYS = 7;

    public boolean syncUserStatus(User user, List<Transaction> activeTransactions) {
        LocalDate today = LocalDate.now();
        boolean hasOverdue = activeTransactions.stream().anyMatch(this::isOverdue);

        // CASE 1: User is active but has overdue items -> BLOCK THEM
        if (!user.isBlocked() && hasOverdue) {
            blockUser(user);
            return true;
        }

        // CASE 2: User is blocked -> CHECK IF THEY CAN BE UNBLOCKED
        if (user.isBlocked()) {
            // Fetch blocking metadata from the blockedusers table
            User blockedData = blockedUserDAO.findByID(user.getId());

            if (blockedData != null && blockedData.getUnblockedDate() != null) {
                LocalDate unblockDate = blockedData.getUnblockedDate();

                // Condition: Today is on/after unblock date AND all overdue items are returned
                if (!today.isBefore(unblockDate) && !hasOverdue) {
                    unblockUser(user);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOverdue(Transaction t) {
        LocalDate today = LocalDate.now();
        LocalDate due = t.getDueDate();
        boolean isLate = today.isAfter(due);

        // For Debug
//        System.out.println("Checking Item: " + t.getEquipment().getEquipmentName());
//        System.out.println("Today's Date: " + today);
//        System.out.println("Due Date in Java: " + due);
//        System.out.println("Is it overdue? : " + late);
//        System.out.println("-------------------------");

        return isLate;
    }

    private void blockUser(User user) {
        LocalDate today = LocalDate.now();
        LocalDate blockedUntil = today.plusDays(PENALTY_DAYS);

        // 1. Update DB flag
        userDAO.updateUserBlockStatus(user.getId(), true);

        // 2. Set dates in object
        user.setUserAccessStatus(true);
        user.setBlockedOn(today);
        user.setBlockedUntil(blockedUntil); // Set it here!

        // 3. Save to blockedusers table (this will now use the date we just set)
        blockedUserDAO.save(user);
    }

    private void unblockUser(User user) {
        // 1. Update main users table flag
        userDAO.updateUserBlockStatus(user.getId(), false);

        // 2. Clear dates in object
        user.setUserAccessStatus(false);
        user.setBlockedOn(null);

        // 3. Remove from blockedusers table
        blockedUserDAO.delete(user.getId());
    }
}
