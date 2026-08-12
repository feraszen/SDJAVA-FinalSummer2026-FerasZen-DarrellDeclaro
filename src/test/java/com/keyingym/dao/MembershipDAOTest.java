package com.keyingym.dao;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.model.Membership;

class MembershipDAOTest {

    @Test
    void shouldPerformMembershipCrudOperations() {
        MembershipDAO membershipDAO = new MembershipDAO();
        String membershipType = "Test Plan " +
                UUID.randomUUID().toString().substring(0, 8);
        int membershipId = 0;

        try {
            Membership membership = new Membership(
                    0,
                    membershipType,
                    new BigDecimal("45.00")
            );

            assertTrue(membershipDAO.addMembership(membership));

            Membership createdMembership =
                    membershipDAO.findByMembershipType(membershipType);

            assertNotNull(createdMembership);
            membershipId = createdMembership.getMembershipId();

            assertEquals(membershipType, createdMembership.getMembershipType());
            assertEquals(new BigDecimal("45.00"), createdMembership.getPrice());
            assertNotNull(membershipDAO.findById(membershipId));

            createdMembership.setMembershipType(membershipType + " Updated");
            createdMembership.setPrice(new BigDecimal("65.00"));

            assertTrue(membershipDAO.updateMembership(createdMembership));

            Membership updatedMembership =
                    membershipDAO.findById(membershipId);

            assertNotNull(updatedMembership);
            assertEquals(
                    membershipType + " Updated",
                    updatedMembership.getMembershipType()
            );
            assertEquals(new BigDecimal("65.00"), updatedMembership.getPrice());

            assertTrue(membershipDAO.deleteMembership(membershipId));
            assertNull(membershipDAO.findById(membershipId));

        } finally {
            if (membershipId != 0) {
                membershipDAO.deleteMembership(membershipId);
            } else {
                Membership remainingMembership =
                        membershipDAO.findByMembershipType(membershipType);

                if (remainingMembership != null) {
                    membershipDAO.deleteMembership(
                            remainingMembership.getMembershipId()
                    );
                }
            }
        }
    }
}