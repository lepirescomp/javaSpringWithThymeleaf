package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addFile(File file) {
        fileMapper.insert(file);
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }

    public File getFileByName(String userId) {
        return fileMapper.getFileByName(userId);
    }


    public void deleteFile(Integer id){
        fileMapper.deleteFile(id);
    }

    public File findById(Long id){
        return fileMapper.getFile(id);
    }
}
