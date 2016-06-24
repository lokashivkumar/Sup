package com.appdhack.sup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Events {
    private String channel;
    private String type;
    private String team;
    private String sub_type;
    private String text;
    private String url;
    private String user;
}
