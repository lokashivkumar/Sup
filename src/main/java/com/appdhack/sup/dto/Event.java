package com.appdhack.sup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String channel;
    private String text;
    private String team;
    private String user;
    private String type;
    private String reply_to;
    private String ts;
    private String url;
}
