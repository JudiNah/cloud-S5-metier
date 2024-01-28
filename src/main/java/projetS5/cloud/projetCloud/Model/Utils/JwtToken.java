package projetS5.cloud.projetCloud.Model.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtToken {
    private String key = "vaikanet";

    public String create(String id, String role) {
        Date expirationDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000); // Date d'expiration 2 jours

        return Jwts.builder()
                .setSubject("tokenVaikaNet")
                .claim("role", role)
                .claim("id", id)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String check(String token, String expectedRole) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        String roleToken = claims.get("role", String.class);
        String id = claims.get("id", String.class);

        if ("all".equalsIgnoreCase(expectedRole) || expectedRole.equals(roleToken)) {
            return id;
        } else {
            throw new Exception("Non autorisé");
        }
    }

    public String checkBearer(String bearerToken, String expectedRole) throws Exception {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new Exception("Autorisation non valide : (pas Bearer)");
        }

        String token = bearerToken.split(" ")[1];
        Claims claims = Jwts.parser()
                .setSigningKey(key) // Utilisation de la même clé pour vérifier le token
                .parseClaimsJws(token)
                .getBody();

        String roleToken = claims.get("role", String.class);
        String id = claims.get("id", String.class);

        if ("all".equalsIgnoreCase(expectedRole) || expectedRole.equals(roleToken)) {
            return id;
        } else {
            throw new Exception("Non autorisé");
        }
    }
}
