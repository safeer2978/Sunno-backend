package com.sunno.uploadservice;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.factory.annotation.Value;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UploadService {

    public static List<MusicData> getMusicDataFromFolder(String folderPath){



        List<MusicData> musicDataList = new ArrayList<>();
        final ImageURL imageURL = new ImageURL();

        File dir = new File(folderPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {

                String path = child.getPath();
                try {
                    InputStream input = new FileInputStream(child);
                    ContentHandler handler = new DefaultHandler();
                    Metadata metadata = new Metadata();
                    Parser parser = new Mp3Parser();
                    ParseContext parseCtx = new ParseContext();
                    parser.parse(input, handler, metadata, parseCtx);
                    input.close();


                    String title = metadata.get("title");

                    String albumArtist = metadata.get("xmpDM:albumArtist");
                    if(albumArtist==null){
                        albumArtist = metadata.get("Author: Post Malone");
                    }

                    String album =metadata.get("xmpDM:album");

                    MusicData musicData=new MusicData();
                    musicData.setAlbumArtist(albumArtist);
                    musicData.setAlbumName(album);
                    musicData.setTrackNumber(metadata.get("xmpDM:trackNumber"));
                    musicData.setTrackTitle(title);

                    String artistNames= metadata.get("xmpDM:artist");
                    List<String> artists;
                    try {
                        String[] artistArray = artistNames.split(";");
                        artists = Arrays.asList(artistArray);
                    }catch (NullPointerException e){
                        artists = new ArrayList<>();
                        artists.add(artistNames);
                    }
                    String genre = metadata.get("xmpDM:genre");

                    musicData.setArtistNames(artists);
                    musicData.setGenre(genre);

                    musicData.setAlbum_url(imageURL.getURL(title+" "+ artists.get(0)+" album cover image"));
                    musicData.setArtist_url(imageURL.getURL(artists.get(0)));
                    musicData.setGenre_url(imageURL.getURL(genre+" music image"));

                    String seed = albumArtist+title+album;

                    musicData.setTrack_id(UUID.nameUUIDFromBytes(seed.getBytes()).toString());
                    musicData.setFile_path(path);



                    musicDataList.add(musicData);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (TikaException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }

        return musicDataList;
    }

    @Value("${sunno.aws.accesskey}")
    static String awsAccessKey;

    @Value("${sunno.aws.privateKey}")
    static String awsSecretKey;

    @Value("${sunno.aws.s3.bucketName}")
    static String bucketName;


    public static boolean uploadToAws(String filePath, String track_id) throws InterruptedException {

        Regions clientRegion = Regions.AP_SOUTH_1;
        //TODO: change these according to your requirements
        String keyName = "sunno/"+track_id+".mp3";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();
            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .build();

            // TransferManager processes all transfers asynchronously,
            // so this call returns immediately.
            Upload upload = tm.upload(bucketName, keyName, new File(filePath));
            System.out.println("Object upload started");

            // Optionally, wait for the upload to finish before continuing.
            upload.waitForCompletion();
            System.out.println("AWS: "+track_id+" upload complete");

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            return false;
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            return false;
        }
        return true;
        }
    }

