package org.example;
import org.example.service.AzureService;
import org.example.service.GptService;
import org.example.service.MinimaxService;

import java.io.IOException;


public class App {

    public static void main(String[] args) throws IOException {
        //AzureService service = new AzureService();
        //GptService service = new GptService();
        MinimaxService service = new MinimaxService();
        service.startChat();
    }

}
