package com.inventar.app.filters;

import com.inventar.app.models.User;
import com.inventar.app.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthFilter(UserRepository userRepository) {
        super();

        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        List<User> users =  this.userRepository.findByUsername(request.getParameter("username"));
        System.out.println("-----aaaa" + users.size());
        if(users.isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid username.");
        }
        else {
System.out.println("-----bbb" + users.size());
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if(!bCryptPasswordEncoder.matches(users.get(0).getId() + "", request.getParameter("api-auth-token")) ) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token.");
            }

            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return !path.startsWith("/auth");
    }
}