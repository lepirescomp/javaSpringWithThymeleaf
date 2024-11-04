package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private FileService fileService;

    private EncryptionService encryptionService;

    public HomeController(NoteService noteService, CredentialService credentialService, UserService userService, FileService fileService,  EncryptionService encryptionService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
    }


    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Note note, Credential credential,  Model model) {
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        model.addAttribute("notes", this.noteService.getNotes(userId));
        List<Credential> credentials = this.credentialService.getCredential(user.getUserId());
        for(Credential c: credentials){
            String encodedSalt = user.getSalt();
            String hashedPassword = encryptionService.decryptValue(c.getPassword(), encodedSalt);
            c.setPlainText(hashedPassword);
        }
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", this.fileService.getFiles(userId));
        return "home";
    }

    @GetMapping("/note")
    public String getNote(Note note, Model model) {
        model.addAttribute("notes", this.noteService.getNotes(note.getUserId()));
        return "home";
    }

    @PostMapping("/note")
    public String addNote(Authentication authentication, Note messageForm, Model model) {
        User user = userService.getUser(authentication.getName());
        messageForm.setUserId(user.getUserId());
        this.noteService.addNote(messageForm);
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        return "redirect:/home";
    }

    @DeleteMapping("/note/{id}")
    public String deleteNote(@PathVariable Long id) {
        this.noteService.deleteNote(id);
        return "redirect:/home";
    }

    @PostMapping("/noteUpdate/{id}")
    public String editNote(Authentication authentication, @PathVariable String id,  Note note, Model model) {
        User user = userService.getUser(authentication.getName());
        Long noteId = Long.parseLong(id);
        Note n = this.noteService.findById(noteId);
        n.setNoteTitle(note.getNoteTitle());
        n.setNoteDescription(note.getNoteDescription());
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        this.noteService.updateNote(n);

        return "redirect:/home";
    }

    @GetMapping("/credential")
    public String getCredential(Authentication authentication, Credential credential, Model model) {
        User user = userService.getUser(authentication.getName());
        List<Credential> credentials = this.credentialService.getCredential(user.getUserId());
        model.addAttribute("credentials", credentials);
        return "home";
    }


    @PostMapping("/credential")
    public String addCredential(Authentication authentication, Credential credential, Model model) {
        User user = userService.getUser(authentication.getName());
        credential.setUsername(user.getUsername());
        credential.setUserId(user.getUserId());

        String encodedSalt = user.getSalt();
        String hashedPassword = encryptionService.encryptValue(credential.getPassword(), encodedSalt);
        credential.setPassword(hashedPassword);

        this.credentialService.addCredential(credential);
        model.addAttribute("credentials", this.credentialService.getCredential(user.getUserId()));
        return "redirect:/home";
    }

    @PostMapping("/credentialUpdate/{id}")
    public String editCredential(Authentication authentication,@PathVariable String id, Credential credential, Model model) {
        User user = userService.getUser(authentication.getName());
        Long credentialId = Long.parseLong(id);
        Credential n = this.credentialService.findById(credentialId);
        n.setUrl(credential.getUrl());

        String encodedSalt = user.getSalt();
        String hashedPassword = encryptionService.encryptValue(credential.getPassword(), encodedSalt);
        credential.setPassword(hashedPassword);


        n.setUsername(credential.getUsername());
        n.setPassword(credential.getPassword());
        List<Credential> credentials = this.credentialService.getCredential(user.getUserId());

        model.addAttribute("credentials", credentials);
        this.credentialService.updateCredential(n);

        return "redirect:/home";
    }

    @DeleteMapping("/credential/{id}")
    public String deleteCredential(@PathVariable Long id) {
        this.credentialService.deleteCredential(id);
        return "redirect:/home";
    }


    @PostMapping(value = "/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String addFile(@RequestPart(value="file") MultipartFile file, Model model, Authentication authentication, RedirectAttributes rattrs) throws IOException {
        String fileRepeated = null;

        String receivedFile = file.getOriginalFilename();
        if(this.fileService.getFileByName(receivedFile) != null){
            fileRepeated = "The file already exists.";
        }

        if (fileRepeated != null) {
            rattrs.addFlashAttribute("fileRepeated", true);
            return "redirect:/home";
        }

        User user = userService.getUser(authentication.getName());
        File newFile = new File(0, file.getOriginalFilename(), file.getContentType(), file.getSize(), user.getUserId(), file.getBytes());
        this.fileService.addFile(newFile);
        model.addAttribute("files", this.fileService.getFiles(user.getUserId()));
        return "redirect:/home";
    }

    @GetMapping(value = "/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFile(MultipartFile file, Model model, Authentication authentication) throws IOException {
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files", this.fileService.getFiles(user.getUserId()));
        return "redirect:/home";
    }


    @GetMapping("/file/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse resp) throws IOException {
        Long fileIdLong = Long.parseLong(id);

        File dbFile = this.fileService.findById(fileIdLong);

        byte[] byteArray =  dbFile.getFiledata(); // read the byteArray

        if(dbFile.getFilename().contains("pdf")){
            resp.setContentType("Application/x-pdf");
        }else{
            resp.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType());
        }
        resp.setHeader("Content-Disposition", "attachment; filename=" + dbFile.getFilename());
        resp.setContentLength(byteArray.length);

        OutputStream os = resp.getOutputStream();
        try {
            os.write(byteArray, 0, byteArray.length);
        } finally {
            os.close();
        }

    }

    @DeleteMapping("/file/{id}")
    public String deleteFile(@PathVariable Integer id) {
        this.fileService.deleteFile(id);
        return "redirect:/home";
    }
}