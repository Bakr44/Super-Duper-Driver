package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE filename=#{fileName}")
    File getFileByFileName(String fileName);

    @Select("SELECT * FROM FILES WHERE fileid=#{fileId}")
    File getFileByFileId(Long fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata,userid) VALUES (#{fileName}, #{contentType}, #{fileSize},#{fileData},#{userid})")
    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    void insertFile(File file);

    @Select("SELECT * FROM FILES WHERE userid=#{userId} and filename=#{fileName}")
    File getFileByUserIdAndFileName(Long userId, String fileName);


    @Select("SELECT * FROM FILES WHERE userId=#{userId}")
    List<File> getFilesByUserId(Long userId);


    @Delete("DELETE FROM FILES WHERE fileid=#{fileId}")
    Long deleteFile(Long fileId);
}
