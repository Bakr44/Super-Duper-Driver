package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping()
    public String signUp() {
        return "signup";
    }

    @PostMapping()
    public String signUpUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes){
        String signupUserError = null;

        if (!userService.isUserNameNotUsed(user.getUserName())){
            signupUserError = "UserName already exists";
        }
        if (signupUserError == null){
           int userAdded= userService.createUser(user);
           if (userAdded<0){
               signupUserError = "There was an error signing you up. Please try again.";
           }
        }
        if (signupUserError==null) {
            redirectAttributes.addFlashAttribute("signupUserSuccess", true);
            return "redirect:/login";
        }else {
            model.addAttribute("signupUserError", signupUserError);
        }
        return "signup";
    }
}
