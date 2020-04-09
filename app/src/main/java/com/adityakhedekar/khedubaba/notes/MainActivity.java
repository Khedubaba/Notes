package com.adityakhedekar.khedubaba.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.adityakhedekar.khedubaba.notes",
                Context.MODE_PRIVATE);
        ListView listView = findViewById(R.id.listView);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null){
            notes.add("Example Note ");
        }
        else{
            notes = new ArrayList<>(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_warning_yellow_24dp)
                        .setTitle("Delete Note")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();

                                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    //Menu code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote){
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return false;
        }
    }
}
