package com.soma.mvp.service;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.soma.mvp.dto.SavePollyStreamReqDto;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PollyService {

    private final AmazonPolly amazonPolly;

    public InputStream pollyStream(String text){

        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(VoiceId.Raveena)
                        .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthRes = amazonPolly.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }

    public InputStream savePollyStream(SavePollyStreamReqDto reqDto){

        String savePath = "D:\\study\\" + reqDto.getFilename()+".mp3";

        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(reqDto.getText()).withVoiceId(VoiceId.Seoyeon)
                        .withOutputFormat(OutputFormat.Mp3);


        try (FileOutputStream outputStream = new FileOutputStream(savePath)) {
            SynthesizeSpeechResult synthesizeSpeechResult = amazonPolly.synthesizeSpeech(synthReq);
            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            try (InputStream in = synthesizeSpeechResult.getAudioStream()) {
                while ((readBytes = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readBytes);

                }
                System.out.println("저장완료");
            }
        } catch (Exception e) {
            System.err.println("Exception caught: " + e);
        }

        SynthesizeSpeechResult synthRes = amazonPolly.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }
}
