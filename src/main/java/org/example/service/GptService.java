package org.example.service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.example.entity.chatgpt.GptMessage;
import org.example.entity.chatgpt.GptRequestBody;
import org.example.entity.chatgpt.GptResponse;
import org.example.entity.minimax.MyMessage;
import org.example.entity.minimax.MyResponse;
import org.example.entity.minimax.Payload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GptService {
    //input your own api key
    private final String OPENAI_API_KEY = "";
    private final String CHAT_OPENAI_ACCESS_TOKEN = "";

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final Gson gson = new GsonBuilder().setLenient().disableHtmlEscaping().create();


    private String parseReturnMessage(String msg) {
        GptResponse response = gson.fromJson(msg, GptResponse.class);

        return response.choices[0].message.content;
    }

    public void startChat() throws IOException {
        String url = "https://api.openai.com/v1/chat/completions";

        OkHttpClient client = new OkHttpClient();
        GptRequestBody gptRequestBody = new GptRequestBody();
        gptRequestBody.model="gpt-3.5-turbo";
        gptRequestBody.messages = new ArrayList<>();
        gptRequestBody.temperature = 0.01;
        String systemMessage = "从现在开始你是一个电商服装店的客服，你只能回答关于你店里商品的问题，不要改变你的身份。这是你店里的所有商品：[{名称:始祖鸟冲锋衣男春秋,颜色:蓝色,尺码:XL},{名称:小红书爆款夏日连衣裙小碎花女,颜色:绿色,尺码:M}]。现在开始你要为顾客服务了";
        gptRequestBody.messages.add(new GptMessage("system", systemMessage));
        Scanner scanner = new Scanner(System.in);
        String userMessage = "你好！";
        while (!userMessage.equalsIgnoreCase("exit")) {
            gptRequestBody.messages.add(new GptMessage("user", userMessage));

            //发起调用
            String jsonRequestBody = gson.toJson(gptRequestBody);
            RequestBody requestBody = RequestBody.create(jsonRequestBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            //System.out.println(responseBody.string());
            String responseMessage = parseReturnMessage(responseBody.string());
            System.out.println("客服bot:");
            System.out.println(responseMessage);
            //将message加入请求中
            gptRequestBody.messages.add(new GptMessage("assistant", responseMessage));

            System.out.println("我:");
            userMessage = scanner.nextLine();
        }

        scanner.close();
    }

}
