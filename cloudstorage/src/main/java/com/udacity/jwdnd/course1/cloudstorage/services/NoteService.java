package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(Note note) {
        noteMapper.insert(note);
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getNotes(userId);
    }

    public void deleteNote(Long id){
         noteMapper.deleteNote(id);
    }

    public void updateNote(Note note){
        noteMapper.edit(note);
    }

    public Note findById(Long id){
        return noteMapper.getNote(id);
    }
}