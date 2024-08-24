package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Long fileId;
    private String fileName;
    private String contentType;
    private String fileSize;
    private Long userid;
    private byte[] fileData;
}
