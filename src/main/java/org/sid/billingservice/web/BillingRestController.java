package org.sid.billingservice.web;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.feign.CustomerServiceClient;
import org.sid.billingservice.feign.InventoryServiceClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingRestController {
    private BillRepository billRepo;
    private ProductItemRepository productItemRepo;
    private InventoryServiceClient inventoryServiceCl;
    private CustomerServiceClient customerServiceCl;

    public BillingRestController(BillRepository billRepo, ProductItemRepository productItemRepo, InventoryServiceClient inventoryServiceCl, CustomerServiceClient customerServiceCl) {
        this.billRepo = billRepo;
        this.productItemRepo = productItemRepo;
        this.inventoryServiceCl = inventoryServiceCl;
        this.customerServiceCl = customerServiceCl;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name="id") Long id) {
        Bill bill = billRepo.findById(id).get();
        bill.setCustomer(customerServiceCl.getCustomerById(bill.getCustomerID()));
        bill.setProductItems(productItemRepo.findByBillId(id));
        bill.getProductItems().forEach(pi -> {
            //pi.setProduct(inventoryServiceCl.getProductById(pi.getProductID()));
            Product product = inventoryServiceCl.getProductById(pi.getProductID());
            pi.setProductName(product.getName());
        });
        return bill;
    }

}
