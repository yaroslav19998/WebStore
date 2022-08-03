package WebStoreGroup.WebStore.secutity.filter;


import WebStoreGroup.WebStore.secutity.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
public class AuthFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @Override
    @Autowired
    public void setAuthenticationManager(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager=authenticationManager;
    }

    @Autowired
    public AuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode message = mapper.createObjectNode();
        message.put("success", false);
        message.put("message", "Invalid credentials");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user=(User)authentication.getPrincipal();
        Map<String,String> tokens=jwtUtils.GenerateTokens(user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        
    }
}
