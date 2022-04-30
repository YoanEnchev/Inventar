package com.inventar.app.repositories;

import com.inventar.app.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query( "select o from Product o where id not in :ids" )
    List<Product> whereNotIds(@Param("ids") List<Long> postIdsList);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Product t SET is_deleted = true WHERE t.year < :year")
    int trashProductsOlderThan(@Param("year") int year);

    @Query( "select o from Product o where is_available = true" )
    List<Product> getAvailableProducts();

    @Query( "select o from Product o where is_available = false" )
    List<Product> getTakenProducts();

    @Query( "select o from Product o where type_id = 3" )
    List<Product> getDMAProducts();

    @Query( "select o from Product o where type_id = 4" )
    List<Product> getMAProducts();

    @Query( "select o from Product o where is_deleted = true" )
    List<Product> getTrashedProducts();
}
