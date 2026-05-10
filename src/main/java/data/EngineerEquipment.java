package data;

public class EngineerEquipment extends Equipment {
    private String discipline;
    private String safetyStandard;

    public EngineerEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath, String discipline, String safetyStandard) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
        this.discipline = discipline;
        this.safetyStandard = safetyStandard;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getSafetyStandard() {
        return safetyStandard;
    }

    public void setSafetyStandard(String safetyStandard) {
        this.safetyStandard = safetyStandard;
    }
}
