package com.example.myapplication;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;

public class ActionFragment extends Fragment {

    MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.action_fragment, container, false);
        final EditText editIn = layout.findViewById(R.id.inField);
        EditText editOut = layout.findViewById(R.id.outField);
        final Spinner spinnerInput = layout.findViewById(R.id.inSpinner);
        final Spinner spinnerOutput = layout.findViewById(R.id.outSpinner);

        spinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainViewModel.setInputData(spinnerInput.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainViewModel.setOutputData(spinnerOutput.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainViewModel.getInputData().observe(requireActivity(), value -> editIn.setText(value));
        mainViewModel.getOutputData().observe(requireActivity(), value -> editOut.setText(value));
        layout.findViewById(R.id.btnConvert).setOnClickListener(item -> mainViewModel.convert(editIn.getText().toString()));
        return layout;
    }
}