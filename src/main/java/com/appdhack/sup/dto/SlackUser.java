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

    public SlackUser(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
