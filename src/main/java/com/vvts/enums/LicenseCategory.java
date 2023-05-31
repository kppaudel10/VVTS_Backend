package com.vvts.enums;

import java.util.Arrays;

/**
 * @auther kul.paudel
 * @created at 2023-05-31
 */
public enum LicenseCategory {

    A("A(Motorcycle, Scooter, Moped)", 0),
    B("B(Car, Jeep, Delivery Van)", 1),
    C("C(Tempo, Auto rickshaw)", 2),
    D("D(Power tiller)", 3),
    E("E(Tractor)", 4),
    F("F(Minibus, Mini-truck)", 5),
    G("G(Truck, Bus, Lorry)", 6),
    H("H(Road Roller, Dozer)", 7),
    J("J(Other)", 8);

    private String value;
    private int key;

    LicenseCategory(String value, int key) {
        this.value = value;
        this.key = key;
    }

    public static LicenseCategory getLicenseCategoryByKey(Integer key) {
        for (LicenseCategory licenseCategory : Arrays.asList(LicenseCategory.values())) {
            if (licenseCategory.ordinal() == key) {
                return licenseCategory;
            }
        }
        return null;
    }

    public static String getLicenseCategoryValueByKey(Integer key) {
        LicenseCategory licenseCategory = getLicenseCategoryByKey(key);
        if (licenseCategory != null) {
            return licenseCategory.getValue();
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
