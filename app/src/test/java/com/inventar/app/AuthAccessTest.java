package com.inventar.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventar.app.filters.AuthFilter;
import com.inventar.app.models.Role;
import com.inventar.app.models.User;
import com.inventar.app.repositories.RoleRepository;
import com.inventar.app.repositories.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthAccessTest {

    private int port = 1443;
    private String baseURL = "http://localhost:" + this.port +"/auth/clients";


    @Test
    void accessWithoutCredentials() throws Exception {

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            this.baseURL,
                HttpMethod.GET,
                new HttpEntity<>(null,
                        new HttpHeaders()), String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    void accessWithBothInvalidCredentials() throws Exception {

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                this.baseURL,
                HttpMethod.GET,
                new HttpEntity<>(new JSONObject()
                            .put("username", "abc")
                            .put("api-auth-token", "123")
                            .toString(),
                        new HttpHeaders()), String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void accessWithValidUsernameButInvalidToken() throws Exception {

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                this.baseURL,
                HttpMethod.GET,
                new HttpEntity<>(new JSONObject()
                        // token for MOL role user
                        .put("username", "mol")
                        .put("api-auth-token", "x")
                        .toString(),
                        new HttpHeaders()), String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
