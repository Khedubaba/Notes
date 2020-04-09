package com.adityakhedekar.khedubaba.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editNote = findViewById(R.id.editNoteText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1){
            editNote.setText(MainActivity.notes.get(noteId));
        }
        else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
        }

        editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.notes.set(noteId, String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.adityakhedekar.khedubaba.notes",
                        Context.MODE_PRIVATE);
                //Use hashset instead of objectserializer when order in list doesnt matter.
                HashSet<String> set = new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
