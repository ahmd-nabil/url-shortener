package nabil.urlshortener.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BijectiveFunctionTest {
    BijectiveFunction bijectiveFunction = new BijectiveFunction();
    long MAX_ID = (long) Math.pow(62, 9) -1;
    @Test
    public void test_encode_success() {
        long id = 1234567890L;
        String expected = "bvIhFu";
        assertEquals(expected, bijectiveFunction.encode(id));

        id = 124;
        assertEquals("ca", bijectiveFunction.encode(id));

        assertEquals("999999999", bijectiveFunction.encode(MAX_ID));
    }

    @Test
    void test_decode_success() {
        long actualId = 1234567890L;
        String shorUrl = bijectiveFunction.encode(actualId);
        long decodedId = bijectiveFunction.decode(shorUrl);
        assertEquals(actualId, decodedId);

        shorUrl = "999999999";
        assertEquals(bijectiveFunction.decode(shorUrl), MAX_ID);
    }
}