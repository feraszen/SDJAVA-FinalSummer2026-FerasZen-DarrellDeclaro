package com.keyingym.console;

import java.math.BigDecimal;
import java.util.List;

import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;
import com.keyingym.model.MerchandisePurchase;
import com.keyingym.model.User;
import com.keyingym.service.MembershipService;
import com.keyingym.service.MerchandiseService;

/** Console workflows shared by Member and Trainer users. */
public class MemberConsole {
    private final MembershipService membershipService;
    private final MerchandiseService merchandiseService;
    private final MerchandiseConsole merchandiseConsole;
    private final ConsoleInput input;

    public MemberConsole(MembershipService membershipService, MerchandiseService merchandiseService, MerchandiseConsole merchandiseConsole, ConsoleInput input) {
        this.membershipService = membershipService;
        this.merchandiseService = merchandiseService;
        this.merchandiseConsole = merchandiseConsole;
        this.input = input;
    }

    public void purchaseMembership(User user) {
        if (user == null) { System.out.println("Authentication required."); return; }
        List<Membership> memberships = membershipService.getAvailableMemberships();
        System.out.println();
        System.out.println("----------- MEMBERSHIPS -----------");
        if (memberships.isEmpty()) { System.out.println("No membership plans found."); return; }
        for (Membership membership : memberships) {
            System.out.println("ID: " + membership.getMembershipId() + " | Type: " + membership.getMembershipType() + " | Price: $" + membership.getPrice());
        }
        System.out.println("-----------------------------------");
        System.out.print("Enter membership ID to purchase or 0 to cancel: ");
        Integer membershipId = input.readInteger();
        if (membershipId == null) { System.out.println("Invalid membership ID."); return; }
        if (membershipId == 0) { System.out.println("Purchase cancelled."); return; }
        Membership membership = membershipService.findMembershipById(membershipId);
        if (membership == null) { System.out.println("Membership not found."); return; }
        boolean purchased = membershipService.purchaseMembership(user.getUserId(), membershipId);
        if (purchased) {
            System.out.println("Membership purchased successfully.");
            System.out.println("Membership: " + membership.getMembershipType() + " | Price: $" + membership.getPrice());
        } else { System.out.println("Unable to purchase membership."); }
    }

    public void viewMyMembership(User user) {
        if (user == null) return;
        List<MembershipPurchase> purchases = membershipService.getUserPurchases(user.getUserId());
        System.out.println();
        System.out.println("--------- MY MEMBERSHIP ---------");
        if (purchases.isEmpty()) { System.out.println("No membership purchases found."); return; }
        for (MembershipPurchase purchase : purchases) {
            System.out.println("Purchase ID: " + purchase.getPurchaseId() + " | Membership ID: " + purchase.getMembershipId() + " | Price: $" + purchase.getPrice() + " | Purchased At: " + purchase.getPurchasedAt());
        }
    }

    public void viewMyExpenses(User user) {
        if (user == null) return;
        List<MembershipPurchase> membershipPurchases = membershipService.getUserPurchases(user.getUserId());
        List<MerchandisePurchase> merchandisePurchases = merchandiseService.getUserPurchases(user.getUserId());
        BigDecimal total = BigDecimal.ZERO;
        System.out.println();
        System.out.println("----------- MY EXPENSES -----------");
        System.out.println("Membership Purchases:");
        for (MembershipPurchase purchase : membershipPurchases) {
            BigDecimal price = purchase.getPrice();
            if (price == null) price = BigDecimal.ZERO;
            total = total.add(price);
            System.out.println("Purchase ID: " + purchase.getPurchaseId() + " | Membership ID: " + purchase.getMembershipId() + " | Amount: $" + price);
        }
        System.out.println("Merchandise Purchases:");
        for (MerchandisePurchase purchase : merchandisePurchases) {
            BigDecimal unitPrice = purchase.getUnitPrice();
            if (unitPrice == null) unitPrice = BigDecimal.ZERO;
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(purchase.getQuantity()));
            total = total.add(itemTotal);
            System.out.println("Purchase ID: " + purchase.getPurchaseId() + " | Merchandise ID: " + purchase.getMerchId() + " | Quantity: " + purchase.getQuantity() + " | Amount: $" + itemTotal);
        }
        System.out.println("Total Expenses: $" + total);
        System.out.println("----------------------------------");
    }

    public void purchaseMerchandise(User user) { merchandiseConsole.purchase(user); }
}
