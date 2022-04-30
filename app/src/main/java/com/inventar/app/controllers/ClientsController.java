package com.inventar.app.controllers;

import com.inventar.app.models.Client;
import com.inventar.app.models.Product;
import com.inventar.app.models.ProductType;
import com.inventar.app.repositories.ClientRepository;
import com.inventar.app.repositories.ProductRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Don't bother returning view.
// Return data instead.
@RestController
public class ClientsController {
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public ClientsController(ClientRepository clientRepository, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/auth/clients")
    public ResponseEntity index(

    ) {
        return ResponseEntity.ok(this.clientRepository.findAll());
    }

    @PostMapping("/auth/create-client")
    public ResponseEntity store(
        @RequestParam(required = true, name = "name") String name,
        @RequestParam(required = true, name = "family_name") String familyName
    ) {
        try {
            Client client = new Client(name, familyName);
            this.clientRepository.save(client);

            return ResponseEntity.ok("ok");
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/edit-client")
    public ResponseEntity update(
        @RequestParam(required = true, name = "id") Long id,
        @RequestParam(required = true, name = "name") String name,
        @RequestParam(required = true, name = "family_name") String familyName
    ) {
        try {
            Client client = this.clientRepository.getById(id);
            client.setName(name);
            client.setFamilyName(familyName);

            this.clientRepository.save(client);

            return ResponseEntity.ok("ok");
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/add-product-to-client")
    public ResponseEntity addProductToClient(
            @RequestParam(required = true, name = "client_id") Long clientID,
            @RequestParam(required = true, name = "product_id") Long productID
    ) {
        try {
            Client client = this.clientRepository.getById(clientID);
            Product product = this.productRepository.getById(productID);

            if(!productRepository.getAvailableProducts().contains(product)) {
                throw new Exception("Product is already taken.");
            }

            client.getProducts().add(product);
            this.clientRepository.save(client);

            product.setAvailable(false);
            this.productRepository.save(product);

            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/remove-product-from-client")
    public ResponseEntity removeProductFromClient(
            @RequestParam(required = true, name = "client_id") Long clientID,
            @RequestParam(required = true, name = "product_id") Long productID
    ) {
        try {
            Client client = this.clientRepository.getById(clientID);

            client.setProducts(
                    client.getProducts().stream()
                            .filter(prod -> !prod.getId().equals(productID))
                            .collect(Collectors.toSet())
            );
            this.clientRepository.save(client);

            Product product = this.productRepository.getById(productID);
            product.setAvailable(true);
            this.productRepository.save(product);

            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/auth/rent-products")
    public ResponseEntity getRentProducts(
            @RequestParam(required = true, name = "client_id") Long clientID
    ) {
        try {
            Optional<Client> clients = clientRepository.findById(clientID);

            if (clients.isEmpty()) {
                throw new Exception("Client doesn't exist.");
            }

            return ResponseEntity.ok(clients.get().getProducts());
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/auth/free-products")
    public ResponseEntity getFreeProducts() {
        try {
            return ResponseEntity.ok(productRepository.getAvailableProducts());
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
