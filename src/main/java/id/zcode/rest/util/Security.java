package id.zcode.rest.util;

import com.google.common.hash.Hashing;
import id.zcode.rest.model.AuthModel;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class Security {

    public static String hashPassword(String raw) {
        if (raw == null) return null;
        return Hashing.sha256().hashString(raw, StandardCharsets.UTF_8).toString();
    }

    public static boolean match(String raw, String hashed) {
        String a = Hashing.sha256().hashString(raw, StandardCharsets.UTF_8).toString();
        return hashed.equals(a);
    }

    public static String createToken(String userId, String roles, String tenantId, String salt, long expire) {
        return t(userId, roles, tenantId, salt, expire);
    }

    public static String createToken(String userId, String roles, String tenantId, String salt) {
        return t(userId, roles, tenantId, salt, -1);
    }

    private static String t(String userId, String roles, String tenantId, String salt, long expire) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(salt);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setId(userId)
                .setIssuedAt(now)
                .setSubject(roles)
                .setAudience(tenantId)
                .signWith(signatureAlgorithm, signingKey);
        if (expire > 0) {
            long expMillis = nowMillis + (expire * 60 * 1000);
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    private static String rt(String token, long expire, String salt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(salt))
                    .parseClaimsJws(token)
                    .getBody();
            return t(
                    claims.getId(),
                    claims.getSubject(),
                    claims.getAudience(),
                    salt, expire);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String recreateToken(String token, String salt) {
        return rt(token, -1, salt);
    }

    public static String recreateToken(String token, long expire, String salt) {
        return rt(token, expire, salt);
    }

    public static AuthModel getModel(String token, String salt)
            throws ExpiredJwtException, MalformedJwtException {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(salt))
                .parseClaimsJws(token)
                .getBody();
        return new AuthModel(claims.getId(), claims.getSubject(), claims.getAudience());
    }
}


