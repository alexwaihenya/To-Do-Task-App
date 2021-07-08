package com.lexie.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lexie.todolist.Model.ToDoModel;
import com.lexie.todolist.Utils.DataBaseHelper;

public class AddNewTask extends BottomSheetDialogFragment {
    public  static final String TAG = "AddNewTask";

    private EditText editText;
    private Button saveButton;
    private DataBaseHelper db;

    public static AddNewTask newInstance(){
         return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_task,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.edittext);
        saveButton = view.findViewById(R.id.button_save);
        db = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            editText.setText(task);

            if (task.length()>0){
                saveButton.setEnabled(false);
            }

        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                }else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (finalIsUpdate){
                    db.updateStatus(bundle.getInt("id"),text);
                }else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    db.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull  DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener ){
            ((OnDialogCloseListener)activity).onDialogInterface(dialog);
        }
    }
}
