package com.inventar.app.controllers;

import com.inventar.app.models.Product;
import com.inventar.app.models.ProductType;
import com.inventar.app.repositories.ProductRepository;
import com.inventar.app.repositories.ProductTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Don't bother returning view.
// Return data instead.
@RestController
public class ProductsController {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductsController(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @GetMapping("/auth/products")
    public ResponseEntity index(
            @RequestParam(required = true, name = "type") String type
    ) {
        try {
            List<Product> products = null;

            switch (type) {
                case "dma":
                    products = this.productRepository.getDMAProducts();
                    break;
                case "ma":
                    products = this.productRepository.getMAProducts();
                    break;
                case "trashed":
                    products = this.productRepository.getTrashedProducts();
                    break;
                case "available":
                    products = this.productRepository.getAvailableProducts();
                    break;
                case "taken":
                    products = this.productRepository.getTakenProducts();
                    break;
                default:
                    products = this.productRepository.findAll();

            }
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/create-product")
    public ResponseEntity store(
            @RequestParam(required = true, name = "name") String name,
            @RequestParam(required = true, name = "year") int year,
            @RequestParam(required = true, name = "type") long productTypeId,
            @RequestParam(required = true, name = "amorthizationIndex") int amorthizationIndex,
            @RequestParam(required = true, name = "description") String description
    ) {
        try {
            ProductType productType = this.productTypeRepository.getById(productTypeId);
            Product product = new Product(productType, name, year, amorthizationIndex, description, false, true);

            this.productRepository.save(product);
            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/edit-product")
    public ResponseEntity update(
            @RequestParam(required = true, name = "id") Long id,
            @RequestParam(required = true, name = "name") String name,
            @RequestParam(required = true, name = "year") int year,
            @RequestParam(required = true, name = "type") long productTypeId,
            @RequestParam(required = true, name = "amorthizationIndex") int amorthizationIndex,
            @RequestParam(required = true, name = "description") String description
    ) {
        try {
            ProductType productType = this.productTypeRepository.getById(productTypeId);
            Product product = this.productRepository.getById(id);

            product.setName(name);
            product.setYear(year);
            product.setType(productType);
            product.setAmorthizationIndex(amorthizationIndex);
            product.setDescription(description);

            this.productRepository.save(product);
            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/trash-product-by-year")
    public ResponseEntity trashOldProducts(
            @RequestParam(required = true, name = "year") int year
    ) {
        try {
            this.productRepository.trashProductsOlderThan(year);
            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/trash-product")
    public ResponseEntity trashProduct(
            @RequestParam(required = true, name = "product_id") Long productID
    ) {
        try {
            Product product = this.productRepository.getById(productID);
            product.setDeleted(true);

            this.productRepository.save(product);
            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
