package WebStoreGroup.WebStore.secutity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import WebStoreGroup.WebStore.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class JWTUtils {

    private final String secretKey="secret";
    private final int access_time=60;
    private final int refresh_time=60;
    private final String issuer="WebStoreApi";
    private UserService userService;

    @Autowired
    public JWTUtils(UserService userService) {
        this.userService = userService;
    }


    public Map<String,String> GenerateTokens(User user){
        Long userid= userService.getUserIdByUsername(user.getUsername());
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        String accessToken= JWT.create()
                .withSubject(String.valueOf(userid))
                .withExpiresAt(new Date(System.currentTimeMillis()+60*1000*10))
                .withIssuer(issuer)
                .withClaim("username",user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken= JWT.create()
                .withSubject(String.valueOf(userid))
                .withExpiresAt(new Date(System.currentTimeMillis()+60*1000*3000))
                .withIssuer(issuer)
                .sign(algorithm);
        Map<String,String> tokens=new HashMap<>();;
        tokens.put("access_token",accessToken);
        tokens.put("refresh_token",refreshToken);
        return tokens;
    }


    public Map<String,String> RefreshToken(String refreshToken) throws Exception{
        DecodedJWT decodedJWT = verifyToken(refreshToken);
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        long userid= Long.parseLong(decodedJWT.getSubject());
        WebStoreGroup.WebStore.User.User user=userService.getUser(userid);
        String accessToken= JWT.create()
                .withSubject(String.valueOf(userid))
                .withExpiresAt(new Date(System.currentTimeMillis()+60*1000*10))
                .withIssuer(issuer)
                .withClaim("username",user.getUsername())
                .withClaim("roles", user.getRoles().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        Map<String,String> tokens=new HashMap<>();
        tokens.put("access_token",accessToken);
        tokens.put("refresh_token",refreshToken);
        return tokens;
    }

    public DecodedJWT verifyToken(String token) throws Exception {
       Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
       JWTVerifier verifier = JWT.require(algorithm).build();
       return verifier.verify(token);
    }
}
