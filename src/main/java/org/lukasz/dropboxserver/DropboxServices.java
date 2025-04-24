package org.lukasz.dropboxserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DropboxServices {
    private final Path folderPath;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public DropboxServices(@Value("${folderPath}") String folderPath, KafkaTemplate<String, Object> kafkaTemplate) {
        this.folderPath = Paths.get(folderPath);
        this.kafkaTemplate = kafkaTemplate;
    }


    void writeFile(MultipartFile file) {
        Path save = Paths.get(folderPath + "/" + file.getOriginalFilename());


        try {
            Files.copy(file.getInputStream(), save, StandardCopyOption.REPLACE_EXISTING);
            sendTopic(file.getOriginalFilename());


        } catch (IOException e) {
            throw new FileException("Internal Server Error");
        }
    }

    private void sendTopic(String fileName) {
        kafkaTemplate.send("new_file", fileName);
    }

    public List<String> savedFiles() {
        try (Stream<Path> paths = Files.list(folderPath)) {
            return paths.map(path -> path.getFileName().toString()).toList();
        } catch (IOException e) {
            throw new FileException("Folder not found");
        }
    }

    Resource downloadFile(String fileName) {
        try {
            Path filePath = folderPath.resolve(fileName);
            return new UrlResource(filePath.toUri());

        } catch (MalformedURLException e) {
            throw new FileException("File not found");
        }
    }
}
