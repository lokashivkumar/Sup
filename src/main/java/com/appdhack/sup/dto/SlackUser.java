package com.appdhack.sup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackUser {
    private String id;
    private String name;
    private boolean isBot;

    public SlackUser(String name, String id, boolean isBot) {
        this.name = name;
        this.id = id;
        this.isBot = isBot;
    }
}
