package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    TextView noteTitle;
    TextView noteTime;
    TextView noteBody;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        noteTitle = itemView.findViewById(R.id.noteTitle);
        noteTime = itemView.findViewById(R.id.noteTime);
        noteBody = itemView.findViewById(R.id.noteBody);
    }
}
