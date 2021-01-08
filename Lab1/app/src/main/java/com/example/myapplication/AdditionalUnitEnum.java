package com.example.myapplication;

public enum AdditionalUnitEnum {
    BYR(MainUnitEnum.CURRENCIES, 1.0),
    USD(MainUnitEnum.CURRENCIES, 0.384),
    EUR(MainUnitEnum.CURRENCIES, 0.3294),

    METERS(MainUnitEnum.DISTANCES, 1.0),
    SM(MainUnitEnum.DISTANCES, 100.0),
    KM(MainUnitEnum.DISTANCES, 0.001),

    CELSIUS(MainUnitEnum.TEMPERATURES, 1.0, 0),
    KELVIN(MainUnitEnum.TEMPERATURES, 0.0, 273),
    FAHRENHEIT(MainUnitEnum.TEMPERATURES, 0.55555, 32);

    private MainUnitEnum unit;
    private double coefficient;
    private double addCoefficient;
    AdditionalUnitEnum(MainUnitEnum unit, double coefficient){
        this.unit = unit;
        this.coefficient = coefficient;
    }

    AdditionalUnitEnum(MainUnitEnum unit, double coefficient, double addCoefficient){
        this.unit = unit;
        this.coefficient = coefficient;
        this.addCoefficient = addCoefficient;
    }

    public MainUnitEnum getUnit(){
        return unit;
    }

    public double getCoefficient(){
        return coefficient;
    }

    public double getAddCoefficient(){
        return addCoefficient;
    }
}
