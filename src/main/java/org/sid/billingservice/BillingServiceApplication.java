package org.sid.billingservice;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.sid.billingservice.feign.CustomerServiceClient;
import org.sid.billingservice.feign.InventoryServiceClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(BillRepository billRepo,
							ProductItemRepository productItemRepo,
							CustomerServiceClient customerServiceCl,
							InventoryServiceClient inventoryServiceCl){
		return args -> {
			Customer customer=customerServiceCl.getCustomerById(1L);
			Bill bill1 = billRepo.save(new Bill(null, new Date(), null,null, customer.getId()));
			PagedModel<Product> productPagedModel = inventoryServiceCl.pageProducts();
			productPagedModel.forEach(p -> {
				ProductItem productItem = new ProductItem();
				productItem.setPrice(p.getPrice());
				productItem.setQuantity(1+new Random().nextInt(100));
				productItem.setProductID(p.getId());
				productItem.setBill(bill1);
				productItemRepo.save(productItem);
			});
		};
	}

}
