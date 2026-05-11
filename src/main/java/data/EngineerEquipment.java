package data;

public class EngineerEquipment extends Equipment {
    public EngineerEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
    }

    @Override
    public String getCategory() {
        return "Engineer";
    }
}