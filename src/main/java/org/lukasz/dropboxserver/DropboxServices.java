package org.lukasz.dropboxserver;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DropboxServices {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DropboxServices(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void writeFile(MultipartFile file) {
        Path folderPath = Paths.get("/home/dropbox/" + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), folderPath, StandardCopyOption.REPLACE_EXISTING);
            sendTopic(file.getOriginalFilename());


        } catch (IOException e) {
            throw new FileException("Internal Server Error");
        }
    }
    private void sendTopic(String fileName){
        kafkaTemplate.send("new_file",fileName );
    }
}
