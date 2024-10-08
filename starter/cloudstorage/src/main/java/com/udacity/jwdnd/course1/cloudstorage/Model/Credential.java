package com.udacity.jwdnd.course1.cloudstorage.Model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Credential {
    private Long credentialid;
    private String url;
    private String username;
    private String key;
    private String password;
    private Long userid;
}
