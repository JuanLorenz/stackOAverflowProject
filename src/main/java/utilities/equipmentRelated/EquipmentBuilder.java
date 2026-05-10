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

    private String hazard;
    private String sport, size;
    private String discipline, safetyStd;
    private String specs, warranty;

    public static EquipmentBuilder start(String type) {
        EquipmentBuilder builder = new EquipmentBuilder();
        builder.type = type.toUpperCase();
        return builder;
    }

    public EquipmentBuilder setInfo(int id, String name, String model, String serial, String condition, String imagePath) {
        this.id = id;
        this.name = name;
        this.model = model;
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

    public EquipmentBuilder labDetails(String hazard) {
        this.hazard = hazard;
        return this;
    }

    public EquipmentBuilder sportDetails(String sport, String size) {
        this.sport = sport;
        this.size = size;
        return this;
    }

    public EquipmentBuilder engineerDetails(String discipline, String safetyStd) {
        this.discipline = discipline;
        this.safetyStd = safetyStd;
        return this;
    }

    public EquipmentBuilder techDetails(String specs, String warranty) {
        this.specs = specs;
        this.warranty = warranty;
        return this;
    }

    public Equipment build() {
        switch (this.type) {
            case "LAB":
                return new LabEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath, hazard);
            case "SPORT":
                return new SportEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath, sport, size);
            case "ENGINEER":
                return new EngineerEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath, discipline, safetyStd);
            case "TECH":
                return new TechEquipment(id, name, model, serial, condition, totalQty, availableQty, imagePath, specs, LocalDate.parse(warranty, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            default:
                throw new IllegalArgumentException("Unknown equipment type: " + type);
        }
    }
}
