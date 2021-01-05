package com.example.myapplication;

public class Converter {
    static public String convert(String data, AdditionalUnitEnum input, AdditionalUnitEnum output) {
        if (input.getUnit() == output.getUnit()) {
            if (!data.equals("")) {
                    return String.valueOf((output.getCoefficient() / input.getCoefficient()) * Double.parseDouble(data));
                }
        }
        return "";
    }
}
