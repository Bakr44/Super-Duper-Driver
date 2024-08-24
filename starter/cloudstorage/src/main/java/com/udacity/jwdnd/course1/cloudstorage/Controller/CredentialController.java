package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredintialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/credential")
@Controller
@RequiredArgsConstructor
public class CredentialController {
    private final CredintialService credintialService;
    private final UserMapper userMapper;


    @PostMapping
    public String addAndUpdateCredentials(Authentication authentication, Credential credential){
        String loginUser = (String) authentication.getPrincipal();
        User user = userMapper.getUserByUserName(loginUser);
        Long userId = user.getUserId();

        if (credential.getCredentialid() != null) {
            credintialService.updateCredential(credential);
        } else {
            credintialService.inertCredential(credential, userId);
        }

        return "redirect:/home/result?success";
    }

    @GetMapping("/delete")
    public String deleteCredentials(@RequestParam("id") Long credentialId, Authentication authentication, RedirectAttributes redirectAttributes){
        String loginUser = (String) authentication.getPrincipal();
        User user = userMapper.getUserByUserName(loginUser);

        if(credentialId > 0){
            credintialService.deleteCredential(credentialId);
            return "redirect:/home/result?success";
        }

        redirectAttributes.addAttribute("error", "error when delete the credential.");
        return "redirect:/home/result?error";
    }


}
