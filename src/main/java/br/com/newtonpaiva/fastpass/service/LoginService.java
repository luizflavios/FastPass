package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.EmailType;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.model.Pix;
import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.PaymentMethodRepository;
import br.com.newtonpaiva.fastpass.repository.UserRepository;
import br.com.newtonpaiva.fastpass.util.CardGenerator;
import br.com.newtonpaiva.fastpass.util.EmailGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
public class LoginService {

    public static final String MD_5 = "MD5";
    public static final String NOT_USED = "N/A";
    private final UserResponseDTO userResponseDTO;


    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private EmailGenerator emailGenerator;
    @Autowired
    private MailSenderService mailSenderService;

    public LoginService() {
        this.userResponseDTO = new UserResponseDTO();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO login(User user) {
        encryptPassword(user);
        User loggedUser = userRepository.
                findByEmailAndPassword(user.getEmail(), user.getPassword()).orElseThrow(EntityNotFoundException::new);

        if (Boolean.FALSE.equals(loggedUser.getEnabled())) {
            return null;
        }

        return (UserResponseDTO) genericMapper.toDTO(loggedUser, userResponseDTO.getClass());
    }

    @Transactional
    public UserResponseDTO register(User user) {
        encryptPassword(user);
        user.setEnabled(Boolean.FALSE);
        user.setCode(UUID.randomUUID().toString().split("-")[0]);
        userRepository.saveAndFlush(user);
        mailSenderService.sendSimpleMessage(emailGenerator.generate(user, EmailType.REGISTER));
        return (UserResponseDTO) genericMapper.toDTO(user, userResponseDTO.getClass());
    }

    @Transactional
    public void activeRegister(String code) {
        User user = userRepository.findByCode(code).orElseThrow(EntityNotFoundException::new);

        if (Boolean.TRUE.equals(user.getEnabled())) {
            return;
        }

        user.setEnabled(Boolean.TRUE);
        userRepository.saveAndFlush(user);

        Card card = CardGenerator.generate(user);
        cardRepository.saveAndFlush(card);

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .active(Boolean.TRUE)
                .user(user)
                .pix(Pix.builder().qrCode(NOT_USED).build())
                .build();
        paymentMethodRepository.saveAndFlush(paymentMethod);

        log.info("Active user completed with success! - " + user.getEmail());
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        user.setPassword(UUID.randomUUID().toString().split("-")[0]);
        mailSenderService.sendSimpleMessage(emailGenerator.generate(user, EmailType.FORGOT_PASSWORD));
        userRepository.saveAndFlush(user);
        log.info("Recover password completed with success! - " + user.getEmail());
    }

    @Transactional(readOnly = true)
    public String existsByEmail(String email) {
        return userRepository.existsByEmail(email).toString();
    }

    private void encryptPassword(User user) {
        try {
            MessageDigest m = MessageDigest.getInstance(MD_5);
            m.update(user.getPassword().getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            user.setPassword(s.toString());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
    }
}
