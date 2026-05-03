package utilities.sqlRelated;

import data.Equipment;
import data.GenEquipment;

import java.util.ArrayList;
import java.util.List;

//once the database is ready, replace this with an actual SQL command for retrieving the equipments in the db
public class EquipmentService {
    public static List<Equipment> getAllEquipment() {
        List<Equipment> list = new ArrayList<>();

        //a "mock" database
        for(int i = 0; i < 33; i++) {
            list.add(new GenEquipment(
                    i+1,
                    "Equipment " + (i+1),
                    "M20" + (i+1),
                    "xyz",
                    "In good condition mhmm",
                    10,
                    10,
                    "/images/placeholder-img.png"));
        }

        return list;
    }

//    public static List<Equipment> getEquipmentByName(String name) {
//        List<Equipment> masterlist = getAllEquipment();
//        List<Equipment> list = new ArrayList<>();
//
//        for(Equipment e : masterlist) {
//            if(e.getEquipmentName().toLowerCase().contains(name.toLowerCase())) list.add(e);
//        }
//
//        return list;
//    }
}
