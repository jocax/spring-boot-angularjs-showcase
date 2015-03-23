package com.jocax.spring.boot.angularjs.showcase;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/service/image")
public class ImageRest {
    private static final Logger LOG = LoggerFactory.getLogger(ImageRest.class);
    private static final Map<String, ImageQueryResponse> images = new HashMap<String, ImageQueryResponse>();

    @PostConstruct
    private void init() throws Exception {

        byte[] bytes1 =  copy(new ClassPathResource("image1.jpg").getInputStream());
        byte[] bytes2 =  copy(new ClassPathResource("image2.jpg").getInputStream());

        images.put("1", new ImageRest.ImageQueryResponse("image1", "image/jpeg", bytes1));
        images.put("2", new ImageRest.ImageQueryResponse("image2", "image/jpeg", bytes2));
        images.put("3", new ImageRest.ImageQueryResponse("image3", "image/jpeg", bytes1));
        images.put("4", new ImageRest.ImageQueryResponse("image4", "image/jpeg", bytes2));

    }

    private byte[] copy(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @RequestMapping(value = "/{id}",   method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ImageQueryResponse getFullImage(@PathVariable("id") final String id) {
        LOG.info("Get image with ID: {}", id);
        ImageQueryResponse imageQueryResponse = images.get(getRandomKey());
        imageQueryResponse.setId(UUID.randomUUID().toString());
        return imageQueryResponse;
    }

    @RequestMapping(value = "/data/{id}",   method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public byte[] getDataImage(@PathVariable("id") final String id) {
        LOG.info("Get image with ID: {}", id);
        ImageQueryResponse imageQueryResponse = images.get(getRandomKey());
        imageQueryResponse.setId(UUID.randomUUID().toString());
        return imageQueryResponse.getContent();
    }


    /**
     * Required for AJAX calls from angularjs: callback=JSON_CALLBACK
     * https://spring.io/blog/2014/07/28/spring-framework-4-1-spring-mvc-improvements
     */
    @ControllerAdvice
    private static class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
        public JsonpAdvice() {
            super("callback");
        }
    }

    private String getRandomKey() {
        Random random = new Random();
        int randomNumber = random.nextInt(images.size()) + 1;
        return "" + randomNumber;
    }

    public static class ImageQueryResponse {
        private String id;
        private String name;
        private String contentType;
        private byte[] content;

        public ImageQueryResponse(String name, String contentType, byte[] content) {
            this.name = name;
            this.contentType = contentType;
            this.content = content;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getContentType() {
            return contentType;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
