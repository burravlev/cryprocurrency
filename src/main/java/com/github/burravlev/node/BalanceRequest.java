package com.github.burravlev.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class BalanceRequest {
    private String topic = UUID.randomUUID().toString();
    private String address;
}
