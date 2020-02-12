package com.example.demo;

import com.ggresillion.spring.mediamanager.dto.Media;
import com.ggresillion.spring.mediamanager.exceptions.MediaException;
import com.ggresillion.spring.mediamanager.services.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private MediaService mediaService;

	@Test
	void contextLoads() {
	}

	@Test
	void should() throws MediaException, IOException {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.png",
				MediaType.IMAGE_PNG.toString(),
				getClass().getClassLoader()
						.getResourceAsStream("media/test.png")
		);
		Media media = mediaService.createMedia(file);
	}

}
