package data;

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

    public int getEquipmentID() {
        return equipmentID;
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

    public int getAvailableQty() {
        return availableQty;
    }

    public String getImagePath() {
        return imagePath;
    }
}
