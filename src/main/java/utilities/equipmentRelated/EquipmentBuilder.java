package utilities.equipmentRelated;


import data.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * A combined builder/factory design pattern for Equipment.
 */
public class EquipmentBuilder {
    private String type;
    private int id;
    private String name, model, serial, condition, imagePath;
    private int totalQty, availableQty;

    public static EquipmentBuilder start(String type) {
        EquipmentBuilder builder = new EquipmentBuilder();
        builder.type = type.toUpperCase();
        return builder;
    }

    public EquipmentBuilder setInfo(int id, String name, String model) {
        this.id = id;
        this.name = name;
        this.model = model;
        return this;
    }

    public EquipmentBuilder setDetails(String serial, String condition, String imagePath) {
        this.serial = serial;
        this.condition = condition;
        this.imagePath = imagePath;
        return this;
    }

    public EquipmentBuilder setInventory(int total, int available) {
        this.totalQty = total;
        this.availableQty = available;
        return this;
    }

    public Equipment build() {
        switch (this.type) {
            case "ENGINEER":
                return new EngineerEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "CHEMISTRY":
                return new ChemistryEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "PE":
                return new PEEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "MULTIMEDIA":
                return new MultimediaEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "MEDICAL SCIENCE":
                return new MedSciEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "IT":
                return new ITEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "ARCHITECTURE":
                return new ArchiEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            case "AGRICULTURE":
                return new AgriEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath);
            default:
                throw new IllegalArgumentException("Invalid equipment type.");
        }
    }
}
