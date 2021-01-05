package com.example.myapplication;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<String> input;
    private MutableLiveData<String> output;
    private MutableLiveData<AdditionalUnitEnum> inSpinner;
    private MutableLiveData<AdditionalUnitEnum> outSpinner;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getInputData(){
        if(input == null){
            input = new MutableLiveData<String>();
            input.setValue("");
        }
        return input;
    }

    public LiveData<String> getOutputData(){
        if(output == null){
            output = new MutableLiveData<String>();
            output.setValue("");
        }
        return output;
    }

    public void setInputData(String str){
        if(input == null){
            input = new MutableLiveData<String>();
        }
        input.setValue(str);
    }

    public void setOutputData(String str){
        if(output == null){
            output = new MutableLiveData<String>();
        }
        output.setValue(str);
    }

    public void swapUnits()
    {
        AdditionalUnitEnum temporary = inSpinner.getValue();
        inSpinner.setValue(outSpinner.getValue());
        outSpinner.setValue(temporary);

        String temp = input.getValue();
        output.setValue(output.getValue());
        output.setValue(temp);

    }

    public MutableLiveData<AdditionalUnitEnum> getInputSpinner(){
        if(inSpinner == null){
            inSpinner = new MutableLiveData<AdditionalUnitEnum>();
            inSpinner.setValue(AdditionalUnitEnum.BYR);
        }
        return inSpinner;
    }

    public MutableLiveData<AdditionalUnitEnum> getOutputSpinner(){
        if(outSpinner == null){
            outSpinner = new MutableLiveData<AdditionalUnitEnum>();
            outSpinner.setValue(AdditionalUnitEnum.BYR);
        }
        return outSpinner;
    }

    public String convert(String data){
        String string = Converter.convert(data, getInputSpinner().getValue(), getOutputSpinner().getValue());
        setOutputData(string);
        return output.getValue();
    }

    public void copyInput(ClipboardManager clipboardManager){
        ClipData clip;
        clip = ClipData.newPlainText("text", getInputData().getValue());
        clipboardManager.setPrimaryClip(clip);
        Toast toast = Toast.makeText(getApplication(), clip.toString(), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void copyOutput(ClipboardManager clipboardManager){
        ClipData clip;
        clip = ClipData.newPlainText("text", getOutputData().getValue());
        clipboardManager.setPrimaryClip(clip);
        Toast toast = Toast.makeText(getApplication(), clip.toString(), Toast.LENGTH_SHORT);
        toast.show();
    }
}
