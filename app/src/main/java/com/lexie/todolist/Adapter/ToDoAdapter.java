package com.lexie.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lexie.todolist.AddNewTask;
import com.lexie.todolist.MainActivity;
import com.lexie.todolist.Model.ToDoModel;
import com.lexie.todolist.R;
import com.lexie.todolist.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder>  {
    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper dataBaseHelper;
    public ToDoAdapter(DataBaseHelper dataBaseHelper,MainActivity activity){
        this.dataBaseHelper = dataBaseHelper;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull  ToDoAdapter.MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dataBaseHelper.updateStatus(item.getId(), String.valueOf(1));
                }else {
                    dataBaseHelper.updateStatus(item.getStatus(), String.valueOf(0));
                }
            }
        });

    }
    public boolean toBoolean(int num){
        return num!=0;

    }

    public Context getActivity(){
        return activity;
    }
    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    public void deleteTask(int position){
        ToDoModel item = mList.get(position);
        dataBaseHelper.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox mCheckBox;

        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
