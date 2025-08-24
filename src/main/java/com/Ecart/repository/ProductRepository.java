package com.Ecart.repository;

import com.Ecart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByActiveTrue();
//    List<Product> findByNameContainingIgnoreCase(String name);
//}
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActive(boolean active);
    List<Product> findByCategoryAndActive(String category, boolean active);
    List<Product> findByNameContainingIgnoreCaseAndActive(String name, boolean active);
}