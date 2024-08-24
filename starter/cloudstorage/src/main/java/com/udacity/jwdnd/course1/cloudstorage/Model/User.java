package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private Long userId;
    private String userName;
    private String salt;
    private String password;
    private String firstName;
    private String lastName;


}
