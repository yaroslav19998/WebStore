package WebStoreGroup.WebStore.secutity.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import WebStoreGroup.WebStore.secutity.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
public class JWTFilter extends OncePerRequestFilter
{
    private final JWTUtils jwtUtils;

    @Autowired
    public JWTFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        System.out.println("asdasdas");
        System.out.println(request.getServletPath());
            if(request.getServletPath().equals("/login")  || request.getServletPath().equals("/users/refresh_token") ||
                    request.getServletPath().equals("/users/registration"))
            {
                System.out.println("asdasdas2");
                filterChain.doFilter(request,response);
            }
            else
            {    System.out.println("asdasdas3");
                String authorizationHeader=request.getHeader(AUTHORIZATION);
                if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
                {

                    try {
                       String token = authorizationHeader.substring("Bearer ".length());
                       DecodedJWT decodedJWT = jwtUtils.verifyToken(token);;
                        String username = decodedJWT.getSubject();
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        stream(roles).forEach(role ->
                        {
                            authorities.add(new SimpleGrantedAuthority(role));
                        });
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    }
                    catch (Exception exception){
                        response.setHeader("error",exception.getMessage());
                        System.out.println(exception.getMessage());
                        response.setStatus(UNAUTHORIZED.value());
                        Map<String,String> error=new HashMap<>();
                        error.put("error_message",exception.getMessage());

                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(),error);
                    }
                }
                else {
                        filterChain.doFilter(request,response);
                }

            }

    }
}

