package com.example.myapplication;

public enum AdditionalUnitEnum {
    BYR(MainUnitEnum.CURRENCIES, 1.0),
    USD(MainUnitEnum.CURRENCIES, 0.384),
    EUR(MainUnitEnum.CURRENCIES, 0.3294),

    METERS(MainUnitEnum.DISTANCES, 1.0),
    SM(MainUnitEnum.DISTANCES, 100.0),
    KM(MainUnitEnum.DISTANCES, 0.001),

    KG(MainUnitEnum.WEIGHTS, 1.0),
    TONNA(MainUnitEnum.WEIGHTS, 0.001),
    GRAMM(MainUnitEnum.WEIGHTS, 1000.0);

    private MainUnitEnum unit;
    private double coefficient;
    AdditionalUnitEnum(MainUnitEnum unit, double coefficient){
        this.unit = unit;
        this.coefficient = coefficient;
    }

    public MainUnitEnum getUnit(){
        return unit;
    }

    public double getCoefficient(){
        return coefficient;
    }
}
