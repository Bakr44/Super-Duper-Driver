package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteMapper noteMapper;

    public List<Note> getAllNotes(Long userId){
        return noteMapper.getNotes(userId);
    }


    public void addNote(Note note, Long userId){
        Note newNote = new Note();
        newNote.setUserid(userId);
        newNote.setNotetitle(note.getNotetitle());
        newNote.setNotedescription(note.getNotedescription());

        noteMapper.addNote(newNote);
    }

    public void updateNote(Note note){
        noteMapper.updateNote(note);
    }

    public void deleteNote(Long noteId){
        noteMapper.deleteNote(noteId);
    }
}
