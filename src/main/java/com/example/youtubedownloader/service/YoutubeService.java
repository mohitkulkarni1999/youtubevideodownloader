package com.example.youtubedownloader.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
public class YoutubeService {

    private static final String YT_DLP_RESOURCE_PATH = "yt-dlp.exe"; // Path for yt-dlp in resources

    public InputStream downloadVideo(String url, String quality) throws Exception {
        // Extract yt-dlp from resources to a temporary file
        File ytDlpFile = extractYtDlp();

        // Create ProcessBuilder with the path to the extracted yt-dlp executable
        ProcessBuilder processBuilder = new ProcessBuilder(
                ytDlpFile.getAbsolutePath(),
                "--format", quality,
                "-o", "-",
                url
        );
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        try (InputStream inputStream = process.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            process.waitFor();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    private File extractYtDlp() throws IOException {
        // Extract yt-dlp executable from resources to a temporary file
        ClassPathResource resource = new ClassPathResource(YT_DLP_RESOURCE_PATH);
        File tempFile = File.createTempFile("yt-dlp", null);
        tempFile.deleteOnExit();

        try (InputStream inputStream = resource.getInputStream();
             OutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // Make the file executable
        tempFile.setExecutable(true);
        return tempFile;
    }
}
