package org.example.entity.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GptMessage {
    public String role;
    public String content;
}
