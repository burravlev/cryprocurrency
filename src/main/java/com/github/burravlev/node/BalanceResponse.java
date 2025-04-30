package com.github.burravlev.node;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BalanceResponse {
    private String amount;
}
