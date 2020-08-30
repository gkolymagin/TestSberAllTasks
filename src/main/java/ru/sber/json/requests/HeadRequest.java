package ru.sber.json.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;

@JsonRootName(value = "data")
public class HeadRequest {
    @Getter
    @JsonProperty("attributes")
    private RequestJson requestJson;
}
