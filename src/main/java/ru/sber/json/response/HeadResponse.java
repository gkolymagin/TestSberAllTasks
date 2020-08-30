package ru.sber.json.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

@JsonRootName(value = "data")
public class HeadResponse {
    @Getter
    @Setter
    @JsonProperty("attributes")
    private ResponseJson responseJson;

    public HeadResponse(ResponseJson responseJson) {
        this.responseJson = responseJson;
    }

}
