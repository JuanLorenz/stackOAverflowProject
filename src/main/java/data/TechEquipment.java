package data;

import java.time.LocalDate;

public class TechEquipment extends Equipment {
    private String specs;
    private LocalDate warrantyExpiry;

    public TechEquipment(int equipmentID, String equipmentName, String modelNo, String serialNo, String condition, int totalQty, int availableQty, String imagePath, String specs, LocalDate warrantyExpiry) {
        super(equipmentID, equipmentName, modelNo, serialNo, condition, totalQty, availableQty, imagePath);
        this.specs = specs;
        this.warrantyExpiry = warrantyExpiry;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public LocalDate getWarrantyExpiry() {
        return warrantyExpiry;
    }

    public void setWarrantyExpiry(LocalDate warrantyExpiry) {
        this.warrantyExpiry = warrantyExpiry;
    }
}
