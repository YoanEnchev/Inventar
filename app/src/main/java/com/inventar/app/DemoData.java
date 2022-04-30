package com.inventar.app;

import com.inventar.app.models.*;
import com.inventar.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DemoData {

    @Autowired
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public DemoData(RoleRepository roleRepository, UserRepository userRepository, ProductTypeRepository productTypeRepository,
                    ProductRepository productRepository, ClientRepository clientRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

        Role adminRole = new Role("admin");
        Role molRole = new Role("mol");

        roleRepository.save(adminRole);
        roleRepository.save(molRole);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        userRepository.save(new User("admin",bCryptPasswordEncoder.encode("123456"), adminRole));
        userRepository.save(new User("mol",bCryptPasswordEncoder.encode("123"), molRole));

        ProductType dmaProduct = new ProductType("dma");
        ProductType maProduct = new ProductType("ma");
        
        productTypeRepository.save(dmaProduct);
        productTypeRepository.save(maProduct);

        Client client1 = new Client("Иван", "Петров");
        Client client2 = new Client("Димитър", "Медведев");
        Client client3 = new Client("Петър", "Стоянов");

        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);

        Product product1 = new Product(maProduct, "Син Диван", 2020, 3, "Хубав син диван", false, false);
        Product product2 = new Product(dmaProduct, "Немски стол", 2018, 4, "Немски стол", false, false);
        Product product3 = new Product(maProduct, "Въртящ стол", 2020, 2, "Син стол, който се върти", true, false);
        Product product4 = new Product(dmaProduct, "Дървена маса", 1980, 5, "Маса направена през социализма", true, false);
        Product product5 = new Product(dmaProduct, "Поставка за телефон", 2016, 2, "Поставка за телефон със залепващ механизъм", false, false);

        Product product6 = new Product(dmaProduct, "Зелен Диван", 2019, 3, "Мек зелен диван", false, true);
        Product product7 = new Product(dmaProduct, "Квадратно огледало", 2019, 1, "Огледало с форма на квадрат", false, true);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);

        client1.getProducts().add(product1);
        client1.getProducts().add(product2);
        client2.getProducts().add(product3);
        client2.getProducts().add(product4);
        client3.getProducts().add(product5);

        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
    }
}