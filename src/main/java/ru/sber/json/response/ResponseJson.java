package ru.sber.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseJson {
      private String transformNumber;
      private String filePath;
}
