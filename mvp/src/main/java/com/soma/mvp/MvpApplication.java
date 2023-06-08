package com.soma.mvp;

import com.soma.mvp.controller.PollyController;
import javazoom.jl.decoder.JavaLayerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MvpApplication {

	public static void main(String[] args) throws IOException, JavaLayerException {
		SpringApplication.run(MvpApplication.class, args);
	}

}
