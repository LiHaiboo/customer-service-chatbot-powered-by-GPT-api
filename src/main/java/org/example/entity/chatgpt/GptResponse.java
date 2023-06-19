package org.example.entity.chatgpt;

import lombok.Data;

@Data
public class GptResponse {
    public String id;
    public String object;
    public long created;
    public Choice[] choices;
    public Usage usage;

    @Data
    public static class Choice {
        public int index;
        public Message message;
        public String finish_reason;
        @Data
        public static class Message {
            public String role;
            public String content;

        }
    }

    @Data
    public static class Usage {
        public int prompt_tokens;
        public int completion_tokens;
        public int total_tokens;
    }
}
