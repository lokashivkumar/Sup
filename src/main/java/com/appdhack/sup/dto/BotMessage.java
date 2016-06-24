package com.appdhack.sup.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BotMessage {
    private Integer id;
    private final String type = "message";
    private final String sub_type = "bot_message";
    private String channel;
    private String text;
}
