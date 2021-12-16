package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private ArrayList<Note> notesList;
    private MainActivity mainAct;

    NotesAdapter(ArrayList<Note> notesList, MainActivity ma){
        this.notesList = notesList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, parent, false);

        inflatedLayout.setOnClickListener(mainAct);
        inflatedLayout.setOnLongClickListener(mainAct);

        return new NotesViewHolder(inflatedLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.noteTime.setText(note.getDateTime());
        String tempTitle = note.getTitle();
        if (tempTitle.length() <= 80){
            holder.noteTitle.setText(note.getTitle());
        } else{
            holder.noteTitle.setText(String.format("%s...", tempTitle.substring(0, 80)));
        }
        String tempBody = note.getBody();
        if (tempBody.length() <= 80){
            holder.noteBody.setText(note.getBody());
        } else{
            holder.noteBody.setText(String.format("%s...", tempBody.substring(0, 80)));
        }

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
