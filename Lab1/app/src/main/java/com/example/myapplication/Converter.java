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
    static public String convertTemperature(String data, AdditionalUnitEnum input, AdditionalUnitEnum output) {
        if (input.getUnit() == output.getUnit()) {
            if (!data.equals("")) {
                if(input == AdditionalUnitEnum.CELSIUS){
                    return String.valueOf(fromCelsius(Double.parseDouble(data), output));
                }
                else if (output == AdditionalUnitEnum.CELSIUS){
                    return String.valueOf(toCelsius(Double.parseDouble(data), input));
                }
                return String.valueOf(fromCelsius(toCelsius(Double.parseDouble(data), input), output));
            }
        }
        return "";
    }

    static public double toCelsius(double data, AdditionalUnitEnum unit)
    {
        return (data - unit.getAddCoefficient()) / unit.getCoefficient();
    }

    static public double fromCelsius(double data, AdditionalUnitEnum unit)
    {
        return data * unit.getCoefficient() + unit.getAddCoefficient();
    }
}
