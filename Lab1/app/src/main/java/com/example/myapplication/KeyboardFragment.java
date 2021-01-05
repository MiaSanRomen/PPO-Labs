package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class KeyboardFragment extends Fragment implements View.OnClickListener {

    private MainViewModel mainViewModel;

    public static KeyboardFragment newInstance() {
        return new KeyboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.keyboard_fragment, container, false);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        Button button = (Button) view.findViewById(R.id.btn1);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn2);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn3);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn4);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn5);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn6);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn7);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn8);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn9);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btn0);
        button.setOnClickListener(this);
        button = (Button) view.findViewById(R.id.btnDel);
        button.setOnClickListener(item -> mainViewModel.getInputData().setValue(""));
        button = (Button) view.findViewById(R.id.btnDot);
        button.setOnClickListener(realNumber);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button)view;
        if(!btn.getText().equals("")){
            mainViewModel.getInputData().setValue(mainViewModel.getInputData().getValue() + btn.getText());
        }
    }

    private View.OnClickListener realNumber = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            String input = mainViewModel.getInputData().getValue();
            if(!input.contains(".") && !input.equals("")){
                mainViewModel.getInputData().setValue(mainViewModel.getInputData().getValue() + ".");
            }
        }
    };
}
