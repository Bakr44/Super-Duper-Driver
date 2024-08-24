package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UserMapper userMapper;

//    @PostMapping
//    public String uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
//        String FileError = null;
//
//        String loggingUser = (String) authentication.getPrincipal();
//        User user = userMapper.getUserByUserName(loggingUser);
//
//        if (uploadFile.isEmpty()) {
//            FileError = "Please select a file to upload";
//        }
//
//        if (!fileService.isFile(user.getUserId(), uploadFile.getOriginalFilename())) {
//            FileError = "File already exists";
//        }
//
////        if(FileError!=null) {
////            redirectAttributes.addFlashAttribute("error", FileError);
////            return "redirect:/result?error";
////        }
//
//        if (FileError != null) {
//            redirectAttributes.addFlashAttribute("FileError", FileError);
//            return "redirect:/home/result?error";
//        }
//
//        fileService.addFile(uploadFile, user.getUserId());
//        return "redirect:/home/result?success";
//    }

    @PostMapping("/save")
    public String processFileUpload(
            @RequestParam(value = "uploadFile") MultipartFile UploadFile ,
            Authentication authentication,
            RedirectAttributes redirectAttributes) throws IOException {

        String currentUser = authentication.getName();
        User user = userMapper.getUserByUserName(currentUser);

        // Check if the file is empty and redirect with an error message if it is
        if (UploadFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/home/result?error";
        }

        // Check if the file already exists for this user and redirect with an error message if it does
        if (!fileService.isFile(user.getUserId(), UploadFile.getOriginalFilename())) {
            redirectAttributes.addFlashAttribute("error", "File already exists.");
            return "redirect:/home/result?error";
        }

        // Add the file
        fileService.addFile(UploadFile, user.getUserId());
        return "redirect:/home/result?success";
    }


    @GetMapping("/removeFile")
    public String removeFile(@RequestParam("fileId") Long fileId, Authentication authentication) {
        String currentUser = authentication.getName();
        User user = userMapper.getUserByUserName(currentUser);

        // Check if the fileId is valid
        if (fileId != null && fileId > 0) {
            Boolean fileRemoved = fileService.deleteFile(fileId);

            // Check if the file was successfully removed
            if (fileRemoved) {
                return "redirect:/home/result?success"; // Redirect to the success page
            }
        }
        return "redirect:/home/result?error"; // Redirect to the error page
    }



    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId){
        File file = fileService.getFileByFileId(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFileName()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
