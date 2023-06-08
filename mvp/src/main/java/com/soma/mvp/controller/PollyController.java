package com.soma.mvp.controller;

import com.soma.mvp.dto.SavePollyStreamReqDto;
import com.soma.mvp.service.PollyService;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PollyController {
    private final PollyService pollyService;

    @PostMapping("/polly")
    public ResponseEntity<byte[]> pollyStream(@RequestBody final String text) throws IOException, JavaLayerException {
        InputStream speechStream = pollyService.pollyStream(text);

        byte[] audioBytes = speechStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(audioBytes.length);
        headers.set("Content-Disposition", "attachment; filename=audio.mp3");


        // 재생
        AdvancedPlayer player = new AdvancedPlayer(speechStream,
                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                System.out.println("Playback started");
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                System.out.println("Playback finished");
            }


        });
        player.play();

        return ResponseEntity.ok().headers(headers).body(audioBytes);
    }

    @PostMapping("/save/polly")
    public ResponseEntity<byte[]> savePollyStream(@RequestBody final SavePollyStreamReqDto reqDto) throws IOException, JavaLayerException {
        InputStream speechStream = pollyService.savePollyStream(reqDto);

        byte[] audioBytes = speechStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(audioBytes.length);
        headers.set("Content-Disposition", "attachment; filename=audio.mp3");


        return ResponseEntity.ok().headers(headers).body(audioBytes);
    }
}
