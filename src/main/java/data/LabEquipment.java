package data;

public class LabEquipment extends Equipment {
    private String hazardLevel;

    public LabEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath, String hazardLevel) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
        this.hazardLevel = hazardLevel;
    }

    public String getHazardLevel() {
        return hazardLevel;
    }

    public void setHazardLevel(String hazardLevel) {
        this.hazardLevel = hazardLevel;
    }
}
