package com.github.burravlev.network;

import org.junit.jupiter.api.Test;

public class IpV4ResolverTest {
    @Test
    public void testGetAddress() {
        var resolver = new IpV4Resolver();
        System.out.println(resolver.getHost());
    }
}
