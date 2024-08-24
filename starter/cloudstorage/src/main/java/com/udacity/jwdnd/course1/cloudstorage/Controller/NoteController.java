package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    private final UserMapper userMapper;

    @PostMapping
    public String addAndUpdateNote(Authentication authentication, Note note){
        String loginUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUserByUserName(loginUserName);
        Long userId = user.getUserId();

        if (note.getNoteid() !=null) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note, userId);
        }

        return "redirect:/home/result?success";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Long noteId, RedirectAttributes redirectAttributes) {
        if (noteId >0) {
            noteService.deleteNote(noteId);
            return "redirect:/home/result?success";
        }
        redirectAttributes.addAttribute("error","cant delete Note");
                return "redirect:/home/result?error";
    }

}
