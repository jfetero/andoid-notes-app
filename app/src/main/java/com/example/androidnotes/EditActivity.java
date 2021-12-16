package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText body;
    private Note note;
    private boolean newNote;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.notes_title);
        body = findViewById(R.id.notes_body);
        body.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent.hasExtra("Note")){
            note = (Note) intent.getSerializableExtra("Note");
            if (note != null){
                title.setText(note.getTitle());
                body.setText(note.getBody());
            }
        }
        if (intent.hasExtra("position")){
            pos = intent.getIntExtra("position", 0);
        }
        if (intent.hasExtra("newNote")){
            newNote = intent.getBooleanExtra("newNote", false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_save){
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", (dialog, id) -> {
            saveNote();
        });
        builder.setNegativeButton("NO", (dialog, id) -> {
            super.onBackPressed();
        });

        builder.setTitle("Your note is not saved!");
        builder.setMessage("Save note?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveNote(){
        String titleText = title.getText().toString();
        String bodyText = body.getText().toString();

        if (titleText.trim().isEmpty()){
            noTitle();
        }
        else if (!titleText.equals(note.getTitle()) || !bodyText.equals(note.getBody())) {
            note.setTitle(titleText);
            note.setBody(bodyText);
            note.setDateTime();

            Intent data = new Intent();
            data.putExtra("Note", note);
            data.putExtra("edited", true);
            data.putExtra("position", pos);
            data.putExtra("newNote", newNote);
            setResult(RESULT_OK, data);
            finish();
        }
        else if (titleText.equals(note.getTitle()) && bodyText.equals(note.getBody())){
            finish();
        }

    }

    public void noTitle(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", (dialog, id) -> {
            Intent data = new Intent();
            if (newNote){
                data.putExtra("newNote", false);
                data.putExtra("edited", false);
                setResult(RESULT_OK, data);
                finish();
            }
            else {
                String curTitle = note.getTitle();
                String curBody = note.getBody();
                String curDateTime = note.getDateTime();

                note.setBody(curBody);
                note.setTitle(curTitle);
                note.setDateTime(curDateTime);

                data.putExtra("Note", note);
                data.putExtra("edited", false);
                data.putExtra("position", pos);
                data.putExtra("newNote", newNote);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
        });

        builder.setTitle("Note will NOT be saved without a title.");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}