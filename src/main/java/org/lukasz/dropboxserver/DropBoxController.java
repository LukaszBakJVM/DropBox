package org.lukasz.dropboxserver;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DropBoxController {
    private final DropboxServices services;

    public DropBoxController(DropboxServices services) {
        this.services = services;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    String upload(@RequestParam("file") MultipartFile file) {
        services.writeFile(file);
        return "File Saved " + file.getOriginalFilename();
    }

    @GetMapping("/files")
    @ResponseStatus(HttpStatus.OK)
    AllFiles allFiles() {
        return services.savedFiles();
    }

    @GetMapping("/file/{fileName}")
    ResponseEntity<Resource> getFile(@PathVariable String fileName) {

        Resource file = services.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file);
    }
}
