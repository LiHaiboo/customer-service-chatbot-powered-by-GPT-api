package org.example.service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.example.entity.minimax.MyMessage;
import org.example.entity.minimax.MyResponse;
import org.example.entity.minimax.Payload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MinimaxService {

    //input your own api key
    private final String group_id = "";
    private final String api_key = "";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final Gson gson = new GsonBuilder().setLenient().disableHtmlEscaping().create();


    private String parseChunkDelta(String chunk) {
        String jsonStr;
        if (chunk.startsWith("data:")) {
            jsonStr = chunk.substring(6).trim();
        } else {
            jsonStr = chunk;
        }
        //System.out.println(jsonStr);
        MyResponse response = gson.fromJson(jsonStr, MyResponse.class);

        return response.getReply();
    }

    public void startChat() throws IOException {
        String url = "https://api.minimax.chat/v1/text/chatcompletion?GroupId=" + group_id;

        OkHttpClient client = new OkHttpClient();

        Payload payload = new Payload();
        payload.model = "abab5-chat";
        payload.prompt = "从现在开始你是一个电商服装店的客服，你只能回答关于你店里商品的问题，不要改变你的身份。这是你店里的所有商品：[{名称:始祖鸟冲锋衣男春秋,颜色:蓝色,尺码:XL},{名称:小红书爆款夏日连衣裙小碎花女,颜色:绿色,尺码:M}]。现在开始你要为顾客服务了";
        payload.role_meta = new Payload.RoleMeta();
        payload.role_meta.user_name = "我";
        payload.role_meta.bot_name = "客服";
        payload.stream = false;
        payload.use_standard_sse = true;
        payload.messages = new ArrayList<>();
        payload.temperature = 0.01;
        Scanner scanner = new Scanner(System.in);
        String userMessage = "从现在开始你是一个电商服装店的客服，你只能回答关于你店里商品的问题，不要改变你的身份。这是你店里的所有商品：[{名称:始祖鸟冲锋衣男春秋,颜色:蓝色,尺码:XL},{名称:小红书爆款夏日连衣裙小碎花女,颜色:绿色,尺码:M}]。现在开始你要为顾客服务了";
        while (!userMessage.equalsIgnoreCase("exit")) {
            payload.messages.add(new MyMessage("USER", userMessage));

            //发起调用
            String jsonPayload = gson.toJson(payload);

            RequestBody requestBody = RequestBody.create(jsonPayload, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + api_key)
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            assert responseBody != null;
            String responseMessage = parseChunkDelta(responseBody.string());
            System.out.println("客服bot:");
            System.out.println(responseMessage);
            //将message加入请求中
            payload.messages.add(new MyMessage("BOT", responseMessage));

            System.out.println("我:");
            userMessage = scanner.nextLine();
        }

        scanner.close();
    }

}
