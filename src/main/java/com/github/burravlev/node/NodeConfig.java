package com.github.burravlev.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class NodeConfig {
    private String key;
    @JsonProperty("storage_path")
    private String storagePath = "app.db";
    private List<String> addresses = new ArrayList<>();
    private Integer port;
}
