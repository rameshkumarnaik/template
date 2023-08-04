package com.naik.awss3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String accessKey="";
        String secretKey="";

        String bucketName = "";

        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();



        //uploadContent(s3client, bucketName, "naik/cow.txt", "I have a cow. Her name is Baula. Most of the time, the cow speak in foreign language.");
        //delete(s3client, bucketName, "naik/abcd.txt");


        LocalDate uploadDate=LocalDate.of(2023,8,31);
        System.out.println("Upload date: "+uploadDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        LocalDate expiryDate = uploadDate.plusMonths(1);
        System.out.println("Expiry date: "+expiryDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));


    }


    public static void list(AmazonS3 s3client, String bucketName, String key, String localFile) throws IOException {
        ObjectListing objectListing = s3client.listObjects(bucketName);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }

    }

    public static void download(AmazonS3 s3client, String bucketName, String key, String localFile) throws IOException {
        S3Object s3object = s3client.getObject(bucketName, key);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, new File(localFile));
    }

    public static void upload(AmazonS3 s3client, String bucketName, String key, String localFile) throws IOException {
        ObjectMetadata om=new ObjectMetadata();

        ObjectTagging a=new ObjectTagging(List.of(new Tag("type","activation"),new Tag("type","activation")));
        PutObjectRequest r = new PutObjectRequest(null, null, null, null).withTagging(a);
        PutObjectResult aaa = s3client.putObject(r);
        s3client.putObject(bucketName, key, new File(localFile));
    }

    public static void uploadContent(AmazonS3 s3client, String bucketName, String key, String content) throws IOException {
        s3client.putObject(bucketName, key, content);
    }

    public static void delete(AmazonS3 s3client, String bucketName, String key) throws IOException {
        s3client.deleteObject(bucketName, key);
    }

}