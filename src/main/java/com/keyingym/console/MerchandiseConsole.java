package com.keyingym.console;

import java.math.BigDecimal;
import java.util.List;

import com.keyingym.config.AppLogger;
import com.keyingym.model.Merchandise;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.service.MerchandiseService;

/**
 * Console workflows for merchandise inventory and purchases.
 */
public class MerchandiseConsole {

    private final MerchandiseService merchandiseService;
    private final ConsoleInput input;

    public MerchandiseConsole(
            MerchandiseService merchandiseService,
            ConsoleInput input
    ) {
        this.merchandiseService = merchandiseService;
        this.input = input;
    }

    public void manageInventory(User user) {
        if (user == null || user.getRole() != UserRole.ADMIN) {
            System.out.println("Access denied. Admin access required.");
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("----- MERCHANDISE INVENTORY -----");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Merchandise");
            System.out.println("3. Update Merchandise");
            System.out.println("4. Delete Merchandise");
            System.out.println("5. Return");
            System.out.println("---------------------------------");

            if (!input.hasNextLine()) {
                return;
            }

            System.out.print("Select an option: ");
            Integer option = input.readInteger();

            if (option == null) {
                System.out.println("Invalid option.");
                continue;
            }

            switch (option) {
                case 1:
                    browse(user);
                    break;
                case 2:
                    add();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

<<<<<<< HEAD
    /**
     * Displays merchandise. Admin users also see per-item and total valuation.
     */
=======
>>>>>>> fix/final-review-priority
    public void browse(User user) {
        List<Merchandise> merchandise =
                merchandiseService.getAvailableMerchandise();

        System.out.println();
        System.out.println("----------- MERCHANDISE -----------");

        if (merchandise.isEmpty()) {
            System.out.println("No merchandise found.");
            return;
        }

<<<<<<< HEAD
        boolean admin = user != null && user.getRole() == UserRole.ADMIN;
        BigDecimal totalInventoryValue = BigDecimal.ZERO;

        for (Merchandise item : merchandise) {
            if (admin) {
                BigDecimal inventoryValue = item.getInventoryValue();
                totalInventoryValue = totalInventoryValue.add(inventoryValue);

                System.out.println(
                        "ID: " + item.getMerchId()
                                + " | Product: " + item.getProductName()
                                + " | Type: " + item.getType()
                                + " | Price: $" + item.getPrice()
                                + " | Stock: " + item.getCurrentStock()
                                + " | Inventory Value: $" + inventoryValue
                );
            } else {
                System.out.println(
                        "ID: " + item.getMerchId()
                                + " | Product: " + item.getProductName()
                                + " | Type: " + item.getType()
                                + " | Price: $" + item.getPrice()
                                + " | Stock: " + item.getCurrentStock()
                );
            }
        }

        if (admin) {
            System.out.println("Total Inventory Valuation: $" + totalInventoryValue);
=======
        BigDecimal totalValuation =
                merchandiseService.calculateInventoryValuation(merchandise);

        for (Merchandise item : merchandise) {
            BigDecimal price = item.getPrice() == null
                    ? BigDecimal.ZERO
                    : item.getPrice();

            BigDecimal itemValuation =
                    price.multiply(BigDecimal.valueOf(item.getCurrentStock()));

            System.out.println(
                    "ID: " + item.getMerchId()
                            + " | Product: " + item.getProductName()
                            + " | Type: " + item.getType()
                            + " | Price: $" + price
                            + " | Stock: " + item.getCurrentStock()
                            + " | Valuation: $" + itemValuation
            );
>>>>>>> fix/final-review-priority
        }

        System.out.println("Total Inventory Valuation: $" + totalValuation);
        System.out.println("-----------------------------------");

        if (user != null && user.getRole() != UserRole.ADMIN) {
            System.out.print(
                    "Would you like to purchase merchandise? "
                            + "Enter Y to continue: "
            );

            if (!input.hasNextLine()) {
                return;
            }

            String answer = input.readLine();
            if ("Y".equalsIgnoreCase(answer)) {
                purchase(user);
            }
        }
    }

    private void add() {
        System.out.println();
        System.out.println("----- ADD MERCHANDISE -----");

        System.out.print("Product name: ");
        String productName = input.readLine();

        System.out.print("Type: ");
        String type = input.readLine();

        System.out.print("Price: ");
        BigDecimal price = input.readBigDecimal();

        System.out.print("Current stock: ");
        Integer stock = input.readInteger();

        if (productName == null || type == null || price == null
                || stock == null || productName.isBlank() || type.isBlank()
                || price.signum() < 0 || stock < 0) {
            System.out.println("Invalid merchandise information.");
            return;
        }

        Merchandise merchandise = new Merchandise(
                0,
                productName,
                type,
                price,
                stock
        );

        if (merchandiseService.addMerchandise(merchandise)) {
            System.out.println("Merchandise added successfully.");
            AppLogger.info(
                    "Admin override: added merchandise "
                            + merchandise.getProductName()
            );
        } else {
            System.out.println("Unable to add merchandise.");
        }
    }

    private void update() {
        System.out.print("Enter merchandise ID to update: ");
        Integer merchId = input.readInteger();

        if (merchId == null || merchId <= 0) {
            System.out.println("Invalid merchandise ID.");
            return;
        }

        Merchandise existing = merchandiseService.findMerchandiseById(merchId);

        if (existing == null) {
            System.out.println("Merchandise not found.");
            return;
        }

        System.out.println("Current product: " + existing.getProductName());
        System.out.print("New product name: ");
        String productName = input.readLine();

        System.out.print("New type: ");
        String type = input.readLine();

        System.out.print("New price: ");
        BigDecimal price = input.readBigDecimal();

        System.out.print("New stock: ");
        Integer stock = input.readInteger();

        if (productName == null || type == null || price == null
                || stock == null || productName.isBlank() || type.isBlank()
                || price.signum() < 0 || stock < 0) {
            System.out.println("Invalid merchandise information.");
            return;
        }

        existing.setProductName(productName);
        existing.setType(type);
        existing.setPrice(price);
        existing.setCurrentStock(stock);

        if (merchandiseService.updateMerchandise(existing)) {
            System.out.println("Merchandise updated successfully.");
            AppLogger.info(
                    "Admin override: updated merchandise "
                            + existing.getProductName()
            );
        } else {
            System.out.println("Unable to update merchandise.");
        }
    }

    private void delete() {
        System.out.print("Enter merchandise ID to delete: ");
        Integer merchId = input.readInteger();

        if (merchId == null || merchId <= 0) {
            System.out.println("Invalid merchandise ID.");
            return;
        }

        Merchandise merchandise = merchandiseService.findMerchandiseById(merchId);

        if (merchandise == null) {
            System.out.println("Merchandise not found.");
            return;
        }

        System.out.print("Confirm deletion? Enter Y to continue: ");
        String confirmation = input.readLine();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println("Deletion cancelled.");
            return;
        }

        if (merchandiseService.deleteMerchandise(merchId)) {
            System.out.println("Merchandise deleted successfully.");
            AppLogger.info("Admin override: deleted merchandise ID " + merchId);
        } else {
            System.out.println("Unable to delete merchandise. Purchase history may prevent deletion.");
        }
    }

    public void purchase(User user) {
        if (user == null || (user.getRole() != UserRole.MEMBER
                && user.getRole() != UserRole.TRAINER)) {
            System.out.println("Access denied. Member or Trainer access required.");
            return;
        }

        List<Merchandise> merchandise =
                merchandiseService.getAvailableMerchandise();

        if (merchandise.isEmpty()) {
            System.out.println("No merchandise available.");
            return;
        }

        System.out.print("Enter merchandise ID to purchase or 0 to cancel: ");
        Integer merchId = input.readInteger();

        if (merchId == null) {
            System.out.println("Invalid merchandise ID.");
            return;
        }

        if (merchId == 0) {
            System.out.println("Purchase cancelled.");
            return;
        }

        Merchandise item = merchandiseService.findMerchandiseById(merchId);

        if (item == null) {
            System.out.println("Merchandise not found.");
            return;
        }

        System.out.println(
                "Selected: " + item.getProductName()
                        + " | Price: $" + item.getPrice()
                        + " | Stock: " + item.getCurrentStock()
        );

        System.out.print("Quantity: ");
        Integer quantity = input.readInteger();

        if (quantity == null || quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (quantity > item.getCurrentStock()) {
            System.out.println("Insufficient stock.");
            return;
        }

        boolean purchased = merchandiseService.purchaseMerchandise(
                user.getUserId(),
                merchId,
                quantity
        );

        if (purchased) {
            System.out.println("Merchandise purchased successfully.");
        } else {
            System.out.println("Unable to complete the purchase.");
        }
    }
}
