package org.lukasz.dropboxserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DropboxServerApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @DynamicPropertySource
    static void dynamicProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("folderPath", () -> "mock/dropbox");
    }

    @Test
    void findAllFiles() {

        String json = """
                ["ffmpeg.dll","vulkan-1.dll"]""";

        webTestClient.get().uri("/files").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(json);
    }

}
