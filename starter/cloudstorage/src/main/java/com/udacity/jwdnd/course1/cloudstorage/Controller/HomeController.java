package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredintialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserMapper userMapper;
    private final FileService fileService;
    private final NoteService noteService;
    private final EncryptionService encryptionService;
    private final CredintialService credintialService;

    @GetMapping()
    public String homePage(Authentication authentication, Model model){
        String loggingUser = (String) authentication.getPrincipal();
        User user=userMapper.getUserByUserName(loggingUser);
//        System.out.println("Logged in User: " + loggingUser);
//        System.out.println("Logged in User: " +  user.getUserId());

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + loggingUser);
        }
        model.addAttribute("files",fileService.getFileUser(user.getUserId()));
        model.addAttribute("notes",noteService.getAllNotes(user.getUserId()));
        model.addAttribute("credentials",credintialService.getCredentialByUserId(user.getUserId()));
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }


    @GetMapping("/result")
    public String result(){
        return "result";
    }

}
