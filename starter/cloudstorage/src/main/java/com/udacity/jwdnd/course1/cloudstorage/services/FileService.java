package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {


    private final FileMapper fileMapper;


    public void addFile(MultipartFile multipartFile, Long userId) throws IOException {
        File file=new File();
        try {

         file.setUserid(userId);
         file.setFileName(multipartFile.getOriginalFilename());
         file.setContentType(multipartFile.getContentType());
         file.setFileSize(Long.toString(multipartFile.getSize()));
         file.setFileData(multipartFile.getBytes());
     }catch (IOException e){
            throw e;
        }

        fileMapper.insertFile(file);
    }

    public List<File> getFileUser(Long userId){
        return fileMapper.getFilesByUserId(userId);
    }

    public Boolean deleteFile(Long fileId){
        Long rowsAffected = fileMapper.deleteFile(fileId);
        return rowsAffected > 0;
    }


    public boolean isFile(Long userId, String fileName) {
        File file = fileMapper.getFileByUserIdAndFileName(userId, fileName);

        if(file != null) {
            return false;
        }
        return true;
    }
    public File getFileByFileId(Long fileId){
        return fileMapper.getFileByFileId(fileId);
    }
}

