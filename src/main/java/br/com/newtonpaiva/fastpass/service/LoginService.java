package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.EmailType;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.UserRepository;
import br.com.newtonpaiva.fastpass.util.CardGenerator;
import br.com.newtonpaiva.fastpass.util.EmailGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class LoginService {

    private final UserResponseDTO userResponseDTO;


    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private EmailGenerator emailGenerator;
    @Autowired
    private MailSenderService mailSenderService;

    public LoginService() {
        this.userResponseDTO = new UserResponseDTO();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO login(User user) {
        User loggedUser = userRepository.
                findByEmailAndPassword(user.getEmail(), user.getPassword()).orElseThrow(EntityNotFoundException::new);
        return (UserResponseDTO) genericMapper.toDTO(loggedUser, userResponseDTO.getClass());
    }

    @Transactional
    public UserResponseDTO register(User user) {
        user.setEnabled(Boolean.TRUE);
        userRepository.saveAndFlush(user);
        Card card = CardGenerator.generate(user);
        cardRepository.saveAndFlush(card);
        mailSenderService.sendSimpleMessage(emailGenerator.generate(user, EmailType.REGISTER));
        return (UserResponseDTO) genericMapper.toDTO(user, userResponseDTO.getClass());
    }

    @Transactional(readOnly = true)
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        mailSenderService.sendSimpleMessage(emailGenerator.generate(user, EmailType.FORGOT_PASSWORD));
    }

}
