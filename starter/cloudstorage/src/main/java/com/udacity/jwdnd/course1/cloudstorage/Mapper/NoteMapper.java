package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

//    @Results({
//            @Result(property = "noteId", column = "noteid"),
//            @Result(property = "notetitle", column = "notetitle"),
//            @Result(property = "notedescription", column = "notedescription"),
//            @Result(property = "userid", column = "userid")
//    })

    @Select("SELECT * FROM NOTES WHERE userId=#{userId}")
    List<Note> getNotes(Long userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true,keyProperty = "noteid")
    void addNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    int deleteNote(Long noteid);
    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    void updateNote(Note note);

}
