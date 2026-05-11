package data;

public class PEEquipment extends Equipment {
    public PEEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
    }

    @Override
    public String getCategory() {
        return "Physical Education";
    }
}