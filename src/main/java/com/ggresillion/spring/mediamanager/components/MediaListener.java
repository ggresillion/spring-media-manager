package com.ggresillion.spring.mediamanager.components;

import com.ggresillion.spring.mediamanager.models.MediaEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PreRemove;

public class MediaListener implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PreRemove
    private void deleteMedia(MediaEntity media) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//        try {
            UploadClient uploadClient = applicationContext.getBean(UploadClient.class);
//            uploadClient.deleteFile(media.getUuid());
//            if(media.getScaledMedias() == null){
//                return;
//            }
//            for (MediaScaled mediaScaled : media.getScaledMedias()) {
//                uploadClient.deleteFile(mediaScaled.getUuid());
//            }
//        } catch (    MediaException ignored) {
//            // Ignored if file is not present
//        }
    }
}
