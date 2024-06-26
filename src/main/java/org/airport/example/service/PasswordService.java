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

    /**
     * Hash supplied password
     * @param password to be hashed
     * @return hashed string
     */
    public String hashPassword(char[] password) {
        return BCrypt.withDefaults().hashToString(HASH_COMPLEXITY, password);
    }

    /**
     * Check if suppieled password is equal to previously stored hashed version
     * @param receivedPassword to be checked
     * @param originalHash hash from database
     * @return trie if correct, false otherwise
     */
    public boolean isCorrectPassword(String receivedPassword, String originalHash) {
        BCrypt.Result verify = BCrypt.verifyer().verify(receivedPassword.toCharArray(), originalHash);
        return verify.verified;
    }

    /**
     * For inplace testing purpose
     */
    public static void main(String[] args) {
        PasswordService passwordService = new PasswordService();

        String password = "Dfks$5d*Q";
        String bcryptHashString = passwordService.hashPassword(password.toCharArray());
        System.out.println("bcryptHashString = " + bcryptHashString);
        boolean result = passwordService.isCorrectPassword(password, bcryptHashString);
        System.out.println("result = " + result);

        String storedHashedPassword = "$2a$12$AA0z0LFi7sF.M065NxV3ZuREtjLPZE7elrOrbjn0DZlPl3v7zwIFq";
        boolean result2 = passwordService.isCorrectPassword(password, storedHashedPassword);
        System.out.println("result2 = " + result2);
    }


}
