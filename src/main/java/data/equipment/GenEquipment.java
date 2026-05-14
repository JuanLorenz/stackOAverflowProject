package data.equipment;

//fallback/placeholder
public class GenEquipment extends Equipment {

    public GenEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
    }
    @Override
    public String getCategory() {
        return "General";
    }
}
