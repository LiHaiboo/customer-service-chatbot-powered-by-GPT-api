package org.example;
import org.example.service.GptService;
import org.example.service.MinimaxService;

import java.io.IOException;


public class App {

    public static void main(String[] args) throws IOException {
//        MinimaxService minimaxService = new MinimaxService();
//        minimaxService.startChat();
        GptService gptService = new GptService();
        gptService.startChat();
    }

}
