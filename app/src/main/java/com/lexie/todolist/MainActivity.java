package com.lexie.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lexie.todolist.Adapter.ToDoAdapter;
import com.lexie.todolist.Model.ToDoModel;
import com.lexie.todolist.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DataBaseHelper dataBaseHelper;
    private List<ToDoModel> mlist;
    private ToDoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        mlist = new ArrayList<>();
        adapter = new ToDoAdapter(dataBaseHelper,MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mlist = dataBaseHelper.getAllTasks();
        Collections.reverse(mlist);
        adapter.setTasks(mlist);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDialogInterface(DialogInterface dialogInterface) {
        mlist = dataBaseHelper.getAllTasks();
        Collections.reverse(mlist);
        adapter.setTasks(mlist);
        adapter.notifyDataSetChanged();

    }
}