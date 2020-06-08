package com.ttl.messageverify;

import junit.framework.TestCase;

import org.junit.Test;

import static com.ttl.messageverify.Utility.extractDigits;

public class UtilityTest extends TestCase {

    @Test
    public void testExtractDigits() {
        String expected = "234567";
        String otpMessage = "Otp received from TML is 234567, do not share this OTP.";
        assertEquals(expected, extractDigits(otpMessage));
    }
}