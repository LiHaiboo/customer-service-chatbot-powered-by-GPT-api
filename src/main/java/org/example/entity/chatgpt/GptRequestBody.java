package org.example.entity.chatgpt;

import lombok.Data;
import org.example.entity.minimax.MyMessage;
import org.example.entity.minimax.Payload;

import java.util.List;

@Data
public class GptRequestBody {
    public String model;
    public boolean stream;
    public List<GptMessage> messages;
    public double temperature;


}
