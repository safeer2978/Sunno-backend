package com.sunno.playservice.AWS;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class AwsUrlService {

    @Value("${sunno.aws.cloudfront.distributionDomainName}")
    static String distributionDomainName;  // the DNS name of your CloudFront distribution, or a registered alias

    @Value("${sunno.aws.cloudfront.keypairId}")
    static String cloudFrontKeyPairId;  // the unique ID assigned to your CloudFront key pair in the console


    final static long expInMils = System.currentTimeMillis() + 10 * 1000 * 60; //TODO: change this as per your requirement

    //TODO: path to your CloudFront private certificate in .der format
    @Value("${sunno.aws.pvtkeyPath}")
    static String pvtkeyPath;

    public static String getUrl(String track_id) throws IOException, InvalidKeySpecException {
        // the private key you created in the AWS Management Console
        ClassPathResource classPathResource = new ClassPathResource(pvtkeyPath);
        InputStream inputStream = classPathResource.getInputStream();
        File cloudFrontPrivateKeyFile = File.createTempFile("pvk",".der");

        try {
            OutputStream outputStream = new FileOutputStream(cloudFrontPrivateKeyFile);
            IOUtils.copy(inputStream,outputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        Date expirationDate = new Date(expInMils);

        String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                SignerUtils.Protocol.https,
                distributionDomainName,
                cloudFrontPrivateKeyFile,
                "sunno/"+track_id+".mp3",       //TODO: Change to your path in S3 bucket
                cloudFrontKeyPairId,
                expirationDate);
        System.out.println(signedUrl);
        return signedUrl;
    }
}
