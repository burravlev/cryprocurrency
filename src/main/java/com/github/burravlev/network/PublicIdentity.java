package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PublicIdentity implements Identity {
    private String host;
    private int port;
    private String id;

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
