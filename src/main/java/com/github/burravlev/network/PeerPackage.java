package com.github.burravlev.network;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
class PeerPackage {
    private String key;
    private String data;
}
