package data.equipment;

public abstract class Equipment {
    //can't decide if these should be final, iirc you can edit equipment as an admin right?
    private int equipmentID;
    private String equipmentName;
    private String modelNo;
    private String serialNo;
    private String condition;
    private int totalQty;
    private int availableQty;
    private String imagePath;

    public Equipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath) {
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.modelNo = modelNo;
        this.serialNo = serialNo;
        this.condition = condition;
        this.totalQty = totalQty;
        this.availableQty = availableQty;
        this.imagePath = imagePath;
    }

    public abstract String getCategory();

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int id) {
        this.equipmentID = id;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getModelNo() {
        return modelNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getCondition() {
        return condition;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public String getImagePath() {
        return imagePath;
    }
}
