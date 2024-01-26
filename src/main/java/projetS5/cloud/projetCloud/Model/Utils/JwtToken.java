package projetS5.cloud.projetCloud.Model.Utils;

import java.sql.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtToken {
    private String key = "vaikanet";
    private String subjet = "tokenVaikaNet";
    /*
     */
    public String create(String id,String role){
        Date expirationDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000); // Date d'expiration 2 jours
        String token = Jwts.builder()
                .setSubject("tokenVaikaNet")
                .claim("role", role)
                .claim("id", id)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, key) 
                .compact();
        return token;
    }

    public String check(String token,String role) throws Exception{
        Claims claims = Jwts.parser()
            .setSigningKey("projetclouds5") // Remplacez "votre-cle-secrete" par la même clé secrète que celle utilisée pour signer le token
            .parseClaimsJws(token)
            .getBody();

            String roleToken = claims.get("role", String.class);
            String id = claims.get("id", String.class);
            if (role.compareToIgnoreCase("all")==0) {
                return id;
            }else{
                if (roleToken.compareToIgnoreCase(role)==0) {
                    return id;
                }else{ throw new Exception("Non autorise");}
            }
    }
}
