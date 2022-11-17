package br.com.newtonpaiva.fastpass.util;

import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.User;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Random;

import static java.security.SecureRandom.getInstanceStrong;

public class CardGenerator {

    private CardGenerator() {

    }

    public static Card generate(User user) {
        return Card.builder()
                .balance(0.0)
                .active(Boolean.TRUE)
                .duoDate(LocalDate.now().plusYears(1))
                .cardNumber(numberGenerator())
                .user(user)
                .build();
    }

    private static String numberGenerator() {
        Random rand = null;
        try {
            rand = getInstanceStrong();
            byte[] array = new byte[7];
            rand.nextBytes(array);
            return new String(array, StandardCharsets.UTF_8).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
