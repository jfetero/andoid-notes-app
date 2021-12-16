package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    private final ArrayList<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter nAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesList.clear();
        notesList.addAll(loadFile());
        recyclerView = findViewById(R.id.recycler);
        nAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult
        );
    }

    @Override
    protected void onPause(){
        saveNote();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_about){
            goToAbout();
        }
        if (item.getItemId() == R.id.menu_add){
            Note n = new Note("", "");
            editNote(n, -1, true);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Note n = notesList.get(pos);
        editNote(n, pos, false);

    }

    @Override
    public boolean onLongClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        deleteNote(pos);
        return true;
    }

    private void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null){
            Log.d(TAG, "handleResult NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK){
            Note temp = (Note) data.getSerializableExtra("Note");
            if (data.getBooleanExtra("newNote", true)){
                notesList.add(0, temp);
                saveNote();
                nAdapter.notifyItemInserted(0);
            }
            if (!data.getBooleanExtra("newNote", false)
                && !data.getBooleanExtra("edited", false)){
                Toast.makeText(this, "Notes with no title are not saved", Toast.LENGTH_LONG).show();
                return;
            }
            if (!data.getBooleanExtra("newNote", false)
                && data.getBooleanExtra("edited", true)){
                int origin = data.getIntExtra("position", 0);
                notesList.remove(origin);
                notesList.add(0, temp);
                saveNote();
                nAdapter.notifyItemMoved(origin, 0);
                nAdapter.notifyItemChanged(0);
            }
        }
    }

    public void goToAbout(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void editNote(Note n, int pos, boolean newNote){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Note", n);
        intent.putExtra("position", pos);
        intent.putExtra("newNote", newNote);
        activityResultLauncher.launch(intent);
    }

    public void deleteNote(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", (dialog, id) -> {
            notesList.remove(pos);
            saveNote();
            nAdapter.notifyItemRemoved(pos);
        });

        builder.setNegativeButton("NO", (dialog, id) -> {});

        builder.setTitle("Delete Note?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Note> loadFile() {
        ArrayList<Note> nList = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");
                String dateTime = jsonObject.getString("dateTime");
                Note note = new Note(title, body, dateTime);
                nList.add(note);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return nList;
    }

    private void saveNote(){
        if (notesList.size() > 0) {
            setTitle("Android Notes " + "(" + notesList.size() + ")");
        } else {setTitle(R.string.app_name);}
        try{
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter pw = new PrintWriter(fos);
            pw.print(notesList);
            pw.close();
            fos.close();
        } catch (Exception e){
            e.getStackTrace();
        }
    }
}