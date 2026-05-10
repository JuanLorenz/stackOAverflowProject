package data;

public class SportEquipment extends Equipment {
    private String sport;
    private String size;

    public SportEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath, String sport, String size) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
        this.sport = sport;
        this.size = size;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
