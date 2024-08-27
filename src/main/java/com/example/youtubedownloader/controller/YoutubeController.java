package com.example.youtubedownloader.controller;

import com.example.youtubedownloader.service.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class YoutubeController {

    @Autowired
    private YoutubeService youtubeService;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadVideo(@RequestParam("url") String url,
                                                             @RequestParam("quality") String quality) {
        try {
            InputStream videoStream = youtubeService.downloadVideo(url, quality);
            InputStreamResource resource = new InputStreamResource(videoStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(videoStream.available())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InputStreamResource(new ByteArrayInputStream(("Error: " + e.getMessage()).getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
