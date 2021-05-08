package com.mojave.service;

import com.mojave.dto.UserDTO;
import com.mojave.exception.UserNotFoundException;
import com.mojave.mapper.UserMapper;
import com.mojave.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;


    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        return userMapper.toUserDTOs(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        return userMapper.toUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id with: " + id)));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
