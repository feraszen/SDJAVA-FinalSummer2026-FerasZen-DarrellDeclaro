package com.keyingym.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Tests the Membership model to verify that membership plan data
 * is stored and retrieved correctly.
 */
class MembershipTest {

    @Test
    void shouldCreateMembershipWithExpectedValues() {
        Membership membership = new Membership(
                1,
                "Monthly",
                new BigDecimal("45.00")
        );

        assertNotNull(membership);
        assertEquals(1, membership.getMembershipId());
        assertEquals("Monthly", membership.getMembershipType());
        assertEquals(new BigDecimal("45.00"), membership.getPrice());
    }
}