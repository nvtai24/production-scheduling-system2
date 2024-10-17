package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByPname(String pname);

    Product findByPid(int pid);

    List<Product> findAll();

    void deleteByPid(int pid);

    void deleteByPname(String pname);

    boolean existsByPname(String pname);

    boolean existsByPid(int pid);

    Product save(Product product);
}