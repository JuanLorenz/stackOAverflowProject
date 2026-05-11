package data;

public class AgriEquipment extends Equipment {
    public AgriEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
    }

    @Override
    public String getCategory() {
        return "Agriculture";
    }
}