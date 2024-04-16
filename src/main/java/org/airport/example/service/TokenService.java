package org.airport.example.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Component is used for loading PUBLIC/PRIVATE keys that are used fpr JWT token
 * generation and check.
 */
@ApplicationScoped
@Slf4j
public class TokenService {

    /**
     * A one-day token live time.
     */
    public static int TOKEN_LIFE_TIME = 14400;
    /**
     * Path to key file inside WAR archive
     * TODO!: that is security issue indeed !! We should NEVER store private key in code
     */
    public static String WAR_FILE_DIR_PRIVATE_KEY = "/META-INF/private.pem";
    /**
     * Signer instance for JWT creation
     */
    private static JWSSigner signer;
    private final static Object lock = new Object();

    /**
     * Try to load private key from resource file and use it for JWT generation
     *
     * @param fullInputFileStream file to read from
     * @return loaded key
     */
    private Optional<PrivateKey> loadPrivateKey(InputStream fullInputFileStream) {
        System.out.println("FuLL key currPath Stream = " + fullInputFileStream);
        Objects.requireNonNull(fullInputFileStream, "fullInputFileStream is NULL, ERROR !");

        log.debug("Full key current Path = {}", fullInputFileStream);

        try (fullInputFileStream) {
            byte[] contents = new byte[4096];
            int length = fullInputFileStream.read(contents);
            // remove unnecessary data from file
            String rawKey = new String(contents, 0, length, StandardCharsets.UTF_8)
                    .replaceAll("-----BEGIN (.*)-----", "")
                    .replaceAll("-----END (.*)----", "")
                    .replaceAll("\r\n", "").replaceAll("\n", "")
                    .trim();

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rawKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return Optional.of(keyFactory.generatePrivate(keySpec));
        } catch (Exception e) {
            log.error("Reading private key file, error", e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Reading private key file by 'main' program
     *
     * @return create stream
     * @throws FileNotFoundException no file by path
     */
    private InputStream readFileInProgram() throws FileNotFoundException {
        Path curr = Paths.get(".").toAbsolutePath().normalize();
        System.out.println("App private key currPath = " + curr);
        // local path for  check in 'main'
        Path fullPath = Path.of(curr.toString(), "src/main/resources/META-INF/private.pem");
        return new FileInputStream(fullPath.toFile());
    }

    private InputStream getInputFileInWebApp() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); // deployment.airport-example.war
        InputStream inputStream = classLoader.getResourceAsStream(WAR_FILE_DIR_PRIVATE_KEY);
//        log.debug("'{}' = {}", WAR_FILE_DIR_PRIVATE_KEY, inputStream);
        System.out.println("? = " + WAR_FILE_DIR_PRIVATE_KEY + ", " + inputStream);
        return inputStream;
    }

    public String generateJWT(boolean isWebApp, final String principal, final String... groups) throws Exception {
        synchronized (lock) { // prevent possible data races
            if (signer == null) {
                InputStream fullPath;
                if (isWebApp) {
                    fullPath = getInputFileInWebApp();
                } else {
                    fullPath = readFileInProgram();
                }
                log.debug("Loading private key for the first time...");
//                System.out.println("Loading private key for the first time...");
                Optional<PrivateKey> privateKeyOptional = loadPrivateKey(fullPath);
                if (privateKeyOptional.isPresent()) {
                    signer = new RSASSASigner(privateKeyOptional.get());
                } else {
                    String error = "No private Key file was found in WAR...";
                    log.error(error);
                    throw new IllegalStateException(error);
                }
            }
        }
        // compose JWT from parts
        JsonArrayBuilder groupsBuilder = Json.createArrayBuilder();
        for (String group : groups) {
            groupsBuilder.add(group);
        }

        long currentTime = System.currentTimeMillis() / 1000;
        JsonObjectBuilder claimsBuilder = Json.createObjectBuilder()
                .add("sub", principal)
                .add("upn", principal)
                .add("iss", "airport-jwt-issuer")
                .add("aud", "jwt-audience")
                .add("jti", UUID.randomUUID().toString())
                .add("iat", currentTime)
                .add("exp", currentTime + TOKEN_LIFE_TIME);

        if (groups.length > 0) {
            claimsBuilder.add("groups", groupsBuilder.build());
        }

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .type(new JOSEObjectType("jwt")).keyID("airport-key").build(),
                new Payload(claimsBuilder.build().toString()));

        jwsObject.sign(signer);

        return jwsObject.serialize();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1)
            throw new IllegalArgumentException("Usage TokenService {principal} {groups}");
        String principal = args[0];
        TokenService tokenService = new TokenService();

        String token = tokenService.generateJWT(false, principal, "user");
        String[] parts = token.split("\\.");
        System.out.printf("\nJWT Header - %s%n", new String(Base64.getDecoder().decode(parts[0]), StandardCharsets.UTF_8));
        System.out.printf("\nJWT Claims - %s%n", new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8));
        System.out.printf("\nGenerated JWT Token \n%s\n%n", token);
    }


}
