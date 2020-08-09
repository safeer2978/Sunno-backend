package com.sunno.playservice.controller;

import com.sunno.playservice.AWS.AwsUrlService;
import com.sunno.playservice.model.TrackRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class PlayController {
    @PostMapping("/play")
    public String getSongUrl(@RequestBody TrackRequest track_request) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
            return AwsUrlService.getUrl(track_request.getTrack_id());
    }
}
