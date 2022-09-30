package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.repository.UserRepository;
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
        return (UserResponseDTO) genericMapper.toDTO(userRepository.saveAndFlush(user), userResponseDTO.getClass());
    }

    @Transactional(readOnly = true)
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

}
