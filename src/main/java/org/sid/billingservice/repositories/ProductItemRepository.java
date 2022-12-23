package org.sid.billingservice.repositories;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem,Long> {
    List<ProductItem> findByBillId(Long billID);
}
