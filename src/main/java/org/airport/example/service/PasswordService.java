package org.airport.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * Stateless component for password encryption and verification.
 */
@ApplicationScoped
@Slf4j
public class PasswordService {
    // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
    private static final int HASH_COMPLEXITY = 12;

    public String hashPassword(char[] password) {
        return BCrypt.withDefaults().hashToString(HASH_COMPLEXITY, password);
    }

    public boolean isCorrectPassword(String receivedHashedPassword, String originalHash) {
        return BCrypt.verifyer().verify(receivedHashedPassword.toCharArray(), originalHash).verified;
    }

    public static void main(String[] args) {
        String password = "Dfks$5d*Q";
//        String password = "guestPwd1!";
        String bcryptHashString = BCrypt.withDefaults().hashToString(HASH_COMPLEXITY, password.toCharArray());
        System.out.println("bcryptHashString = " + bcryptHashString);
        // $2a$12$AA0z0LFi7sF.M065NxV3ZuREtjLPZE7elrOrbjn0DZlPl3v7zwIFq
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        System.out.println("result = " + result.verified);

        String hashed = "$2a$12$AA0z0LFi7sF.M065NxV3ZuREtjLPZE7elrOrbjn0DZlPl3v7zwIFq";
        BCrypt.Result result2 = BCrypt.verifyer().verify(password.toCharArray(), hashed);
        System.out.println("result = " + result2.verified);
    }

}
