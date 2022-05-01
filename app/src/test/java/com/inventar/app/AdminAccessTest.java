package com.inventar.app;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdminAccessTest {
    private int port = 1443;
    private String baseURL = "http://localhost:" + this.port + "/admin/create-mol";

    @Test
    void accessWithValidUsernameButInvalidToken() throws Exception {

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                this.baseURL,
                HttpMethod.POST,
                new HttpEntity<>(new JSONObject()
                        // token for MOL role user
                        .put("admin-username", "admin")
                        .put("api-auth-token", "x")
                        .put("username", "new user")
                        .put("password", "new user pass")
                        .toString(),
                        new HttpHeaders()), String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void accessWithNonAdminUser() throws Exception {

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                this.baseURL,
                HttpMethod.POST,
                new HttpEntity<>(new JSONObject()
                        // token for MOL role user
                        .put("admin-username", "mol")
                        .put("api-auth-token", new BCryptPasswordEncoder().encode("2"))
                        .put("username", "new user")
                        .put("password", "new user pass")
                        .toString(),
                        new HttpHeaders()), String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
