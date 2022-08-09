package exp.exalt.bookshop.util;

import exp.exalt.bookshop.constants.ConstVar;
import exp.exalt.bookshop.exceptions.GeneralException;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorGeneralException;
import exp.exalt.bookshop.models.BookShopUser;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private String secretKey = "Exalt_00";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // helper method  for user
    public String getUserName(String token) {
        String jwt = null;
        String usernameExtracted = null;
        try {
            if (token != null && token.startsWith("Bearer ")) {
                jwt = token.substring(7);
                usernameExtracted = extractUsername(jwt);
            }
            if (usernameExtracted != null
                    && !usernameExtracted.isEmpty()) {
                return usernameExtracted;
            }
        } catch (SignatureException ex){
            throw new GeneralException("Invalid JWT signature");
        } catch (MalformedJwtException ex){
            throw new GeneralException("Invalid JWT token");
        } catch (ExpiredJwtException ex){
            throw new GeneralException("Expired JWT token");
        } catch (UnsupportedJwtException ex){
            throw new GeneralException("Unsupported JWT token");
        } catch (IllegalArgumentException ex){
            throw new GeneralException("JWT claims string is empty");
        }
        return "";
    }

    public boolean checkAuthentication(String token, String username){
        String jwt = null;
        String usernameExtracted = null;
        try {
            if (token != null && token.startsWith("Bearer ")) {
                jwt = token.substring(7);
                usernameExtracted = extractUsername(jwt);
            }
            if (usernameExtracted != null
                    && !usernameExtracted.isEmpty()) {
                return username.equals(usernameExtracted);
            }
        } catch (SignatureException ex){
            throw new GeneralException("Invalid JWT signature");
        } catch (MalformedJwtException ex){
            throw new GeneralException("Invalid JWT token");
        } catch (ExpiredJwtException ex){
            throw new GeneralException("Expired JWT token");
        } catch (UnsupportedJwtException ex){
            throw new GeneralException("Unsupported JWT token");
        } catch (IllegalArgumentException ex){
            throw new GeneralException("JWT claims string is empty");
        }
        return false;
    }

    public boolean isValidateToken(String token, BookShopUser bookShopUser) {
        try {
            final String username = getUserName(token);
            return (username.equals(bookShopUser.getUsername()) && !isTokenExpired(token));
        } catch (SignatureException ex){
            throw new GeneralException("Invalid JWT signature");
        } catch (MalformedJwtException ex){
            throw new GeneralException("Invalid JWT token");
        } catch (ExpiredJwtException ex){
            throw new GeneralException("Expired JWT token");
        } catch (UnsupportedJwtException ex){
            throw new GeneralException("Unsupported JWT token");
        } catch (IllegalArgumentException ex){
            throw new GeneralException("JWT claims string is empty");
        } catch (NullPointerException ex) {
            throw new GeneralException(ConstVar.SERVER_ERROR);
        }
    }
}
