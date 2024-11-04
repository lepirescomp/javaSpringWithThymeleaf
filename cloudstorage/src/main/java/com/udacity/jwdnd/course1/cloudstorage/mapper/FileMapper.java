package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFile(Long fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File getFileByName(String filename);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getFiles(Integer userId);

    @Select("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userId, filedata) VALUES(#{filename}, #{contenttype}, #{filesize}, #{userId}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insert(File file);

}
