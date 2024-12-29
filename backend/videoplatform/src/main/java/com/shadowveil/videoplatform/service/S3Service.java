package com.shadowveil.videoplatform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class S3Service {
//    private final S3Client s3Client;
//
//    public S3Service() {
//        s3Client = S3Client.builder()
//                .region(Region.US_EAST_1)
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY")
//                ))
//                .build();
//    }
//
//    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
//        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
//        s3Client.putObject(PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileName)
//                .build(), software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
//
//        return fileName;
//    }
//
//    public void transcodeVideo(String inputPath, String outputPath) throws IOException {
//        ProcessBuilder pb = new ProcessBuilder(
//                "ffmpeg", "-i", inputPath, "-vf", "scale=1280:720", outputPath
//        );
//        pb.redirectErrorStream(true);
//        Process process = pb.start();
//        process.waitFor();
//    }
}