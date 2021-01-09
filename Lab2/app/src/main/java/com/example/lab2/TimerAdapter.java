package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lab2.Database.ActionsDB;
import com.example.lab2.Database.DataBaseProvider;
import com.example.lab2.Database.TimerModel;

import java.util.List;


public class TimerAdapter extends ArrayAdapter<TimerModel> {
    private LayoutInflater inflater;
    private int layout;
    private List<TimerModel> timerModelList;
    private DataBaseProvider db;

    TimerAdapter(Context context, int resource, List<TimerModel> timerModels,
                 DataBaseProvider db) {
        super(context, resource, timerModels);
        this.timerModelList = timerModels;
        this.layout = resource;
        this.db = db;
        this.inflater = LayoutInflater.from(context);
        ActionsDB.setDb(db);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TimerModel timerModel = timerModelList.get(position);
        viewHolder.nameView.setText(timerModel.Name);
        viewHolder.idView.setText((Integer.toString(timerModel.Id)));
        viewHolder.layout.setBackgroundColor(timerModel.Color);

        viewHolder.startButton.setOnClickListener(i -> {
            Context context = getContext();
            Intent intent = new Intent(context, TimerPage.class);
            intent.putExtra("timerId", timerModel.Id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        viewHolder.removeButton.setOnClickListener(i -> {
            ActionsDB.actionDelete(timerModel);
            timerModelList.remove(timerModel);
            notifyDataSetChanged();
        });

        viewHolder.editButton.setOnClickListener(i -> {
            Context context = getContext();
            Intent intent = new Intent(context, TimerCreate.class);
            intent.putExtra("timerId", new int[]{timerModel.Id, 1});
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        return convertView;
    }

    private class ViewHolder {
        Button removeButton, editButton, startButton;
        TextView nameView, idView;
        LinearLayout layout;
        ViewHolder(View view){
            startButton = view.findViewById(R.id.btnStart);
            editButton = view.findViewById(R.id.btnEdit);
            removeButton = view.findViewById(R.id.btnRemove);
            nameView = view.findViewById(R.id.txtName);
            idView = view.findViewById(R.id.txtId);
            layout = view.findViewById(R.id.layout);
        }
    }
}
