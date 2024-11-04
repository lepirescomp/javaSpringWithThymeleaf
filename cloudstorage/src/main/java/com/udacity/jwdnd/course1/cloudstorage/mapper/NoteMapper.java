package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(Long noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotes(Integer userId);

    @Select("DELETE FROM NOTES WHERE noteId = #{noteId}")
    void deleteNote(Long noteId);

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    void insert(Note note);

    @Update("UPDATE NOTES SET noteDescription=#{noteDescription}, noteTitle=#{noteTitle} WHERE noteId = #{noteId}")
    void edit(Note note);

}
