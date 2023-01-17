package it.itsar.firedb;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import it.itsar.firedb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private ArrayList<Task> myTasks = new ArrayList<>();
    private ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, myTasks);
        listView = findViewById(R.id.list);

        listView.setAdapter(adapter);

        CollectionReference toDoListRef = db.collection("toDoList");

        toDoListRef
                .get()
                .addOnCompleteListener(taskQuery -> {
                    if(taskQuery.isSuccessful()) {
                        for (QueryDocumentSnapshot document: taskQuery.getResult()) {
                            Task task = document.toObject(Task.class);
                            task.setId(document.getId());
                            myTasks.add(task);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Log.d("ERROR: ", "Error getting document " + taskQuery.getException());
                    }
                });

    }
}

