package data;

import data.equipment.Equipment;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private transient String password;
    private String userType;
    private boolean isBlocked;
    private String profilePhotoPath;

    public User(int id, String name, String email, String password, String userType,  boolean isBlocked, String profilePhotoPath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.isBlocked = isBlocked;
        this.profilePhotoPath = profilePhotoPath;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
    public boolean isBlocked() { return isBlocked; }
    public String getProfilePhotoPath() { return profilePhotoPath; }

    public void setUserAccessStatus(boolean isBlocked) { this.isBlocked = isBlocked; }
}