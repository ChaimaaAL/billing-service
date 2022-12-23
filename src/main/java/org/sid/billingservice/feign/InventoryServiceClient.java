package org.sid.billingservice.feign;

import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.QueryParam;

@FeignClient(name="inventory-service")
public interface InventoryServiceClient {
    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable("id") Long id);
    @GetMapping("/products")
    PagedModel<Product> pageProducts();
}
