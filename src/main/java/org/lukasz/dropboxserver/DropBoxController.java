package org.lukasz.dropboxserver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DropBoxController {
    private final DropboxServices services;

    public DropBoxController(DropboxServices services) {
        this.services = services;
    }
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    String upload(@RequestParam("file") MultipartFile file){
        services.writeFile(file);
        return "File Saved";
    }
}
