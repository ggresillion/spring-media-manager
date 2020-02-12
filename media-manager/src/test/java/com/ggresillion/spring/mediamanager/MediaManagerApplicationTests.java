package com.ggresillion.spring.mediamanager;

import com.ggresillion.spring.mediamanager.configurations.MediaManagerConfiguration;
import com.ggresillion.spring.mediamanager.dto.MediaContent;
import com.ggresillion.spring.mediamanager.exceptions.MediaException;
import com.ggresillion.spring.mediamanager.services.MediaService;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaManagerApplicationTests {

    @Autowired
    private MediaService mediaService;
    @Autowired
	private MediaManagerConfiguration configuration;

    @Test
	@Transactional
    public void shouldCreateAMedia() throws MediaException, IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("media/test.png");
		byte[] input = ByteStreams.toByteArray(is);
		MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG.toString(),
				new ByteArrayInputStream(input));
        MediaContent media = mediaService.createMedia(file);

        byte[] output = ByteStreams.toByteArray(media.getContent());
        assertEquals(media.getType(), MediaType.IMAGE_PNG);
        assertArrayEquals(input, output);
		for(Map.Entry<String, Integer> size: configuration.getSizes().entrySet()) {
			MediaContent smedia = mediaService.getMedia(media.getId(), size.getKey());
			BufferedImage bimg = ImageIO.read(smedia.getContent());
            assertEquals(smedia.getType(), MediaType.IMAGE_PNG);
            assertEquals(bimg.getWidth(), (int)size.getValue());
		}
	}

}
