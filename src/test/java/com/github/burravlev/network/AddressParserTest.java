package com.github.burravlev.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AddressParserTest {
    private static final String HOST = "192.0.0.1";
    private static final int PORT = 8080;
    private static final String ID = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9llBlZoHBraYBktHkjPZ-8vn1iNTviz9UyWNIavH3eFRCVdZEr4H6y21T97OPo4a1kogHSMiAvAg7v9DTfFrZj1iGyXTYFKoLtNM4R9U_gseEIyFW-nAfu8qVSluu0OHBR8VRjZSljwJ7STS8NCF3joUrICGWxX7lf5q_2tfGKyroNShBY_76l1X5mme1heGM3oRCw5iXrOJ0GGDCAyv_sWuwWL34rxZB6NZfDJcd9KGT9RjumAP8i5ZpHdb6eIHt1SGRZFR5HlrCUcDUUrrjRo3ndQDk0VOw3YK8gSfsnOLlBlhZ2d8k2wzYMPZPU55uH303aXIyh1WQR2GEeSrDwIDAQAB";

    @Test
    public void shouldParseAddress() {
        var parser = new AddressParser(
            HOST + "/" + PORT + "/" + ID
        );
        Assertions.assertEquals(HOST, parser.getHost());
        Assertions.assertEquals(PORT, parser.getPort());
        Assertions.assertEquals(ID, parser.getId());
    }

    @Test
    public void shouldThrowIllegalStateException() {
        Assertions.assertThrows(IllegalStateException.class,
            () -> new AddressParser(HOST + "/" + ID));
    }
}
