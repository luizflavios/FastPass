package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserRequestDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenericMapper genericMapper;

    @Transactional
    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        try {
            User user = userRepository.findById(userRequestDTO.getId()).orElseThrow(EntityNotFoundException::new);
            user.setFullName(userRequestDTO.getFullName());
            user.setEmail(userRequestDTO.getEmail());
            //TODO VERIFICAR PASSWORD - CRYPT QUANDO ATUALIZAR
            user.setPassword(user.getPassword());
            user.setUserImage(userRequestDTO.getUserImage().getBytes());
            userRepository.saveAndFlush(user);
            return (UserResponseDTO) genericMapper.toDTO(user, UserResponseDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

}
