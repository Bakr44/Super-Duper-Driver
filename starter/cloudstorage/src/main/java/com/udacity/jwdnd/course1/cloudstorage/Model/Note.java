package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class Note {
    private Long noteid;
    private String notetitle;
    private String notedescription;
    private Long userid;


}
