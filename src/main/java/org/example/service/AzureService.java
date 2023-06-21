package org.example.service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.example.entity.chatgpt.GptMessage;
import org.example.entity.chatgpt.GptRequestBody;
import org.example.entity.chatgpt.GptResponse;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AzureService {
    //input your own api key
    private final String ENDPOINT = "";
    private final String API_KEY = "";

    public void startChat() throws IOException {

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(ENDPOINT)
                .credential(new AzureKeyCredential(API_KEY))
                .buildClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent("从现在开始你是一个电商服装店的客服，你只能回答关于你店里商品的问题，不要改变你的身份。" +
                "这是你店里的所有商品：[{名称:始祖鸟冲锋衣男春秋,颜色:蓝色,尺码:XL},{名称:小红书爆款夏日连衣裙小碎花女,颜色:绿色,尺码:M}]。现在开始你要为顾客服务了"));

        Scanner scanner = new Scanner(System.in);
        String userMessage = "您好！";
        while (!userMessage.equalsIgnoreCase("exit")) {
            chatMessages.add(new ChatMessage(ChatRole.USER).setContent(userMessage));
            //发起调用
            ChatCompletionsOptions option = new ChatCompletionsOptions(chatMessages);
            option.setTemperature(0.1).setMaxTokens(4096);
            ChatCompletions chatCompletions = client.getChatCompletions("gpt35", option);
            String responseMessage = chatCompletions.getChoices().get(0).getMessage().getContent();
            System.out.println("客服bot:");
            System.out.println(responseMessage);
            //System.out.println(chatCompletions.getUsage().getTotalTokens());
            //将message加入请求中
            chatMessages.add(new ChatMessage(ChatRole.ASSISTANT).setContent(responseMessage));
            System.out.println("我:");
            userMessage = scanner.nextLine();
        }
        scanner.close();
    }
}
