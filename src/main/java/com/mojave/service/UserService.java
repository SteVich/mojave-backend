package com.mojave.service;

import com.mojave.dictionary.Role;
import com.mojave.dto.UserDTO;
import com.mojave.exception.ResourceNotFoundException;
import com.mojave.exception.UserNotFoundException;
import com.mojave.mapper.UserMapper;
import com.mojave.model.Project;
import com.mojave.model.User;
import com.mojave.model.UserProjectRole;
import com.mojave.model.UserProjectRoleId;
import com.mojave.payload.request.UserUpdateRequest;
import com.mojave.repository.UserRepository;
import com.mojave.security.UserPrincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        return userMapper.toUserDTOs(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        return userMapper.toUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id with: " + id)));
    }

    @Transactional(readOnly = true)
    public Optional<User> findOptionalUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO getCurrentUserDto() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userMapper.principalToUserDTO(userPrincipal);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userRepository.findById(userPrincipal.getId()).orElse(null);
    }

    @Transactional
    public void addProjectToUser(Project project) {
        User currentUser = getCurrentUser();
        currentUser.getProjects().add(project);

        setRole(project, currentUser, Role.ROLE_PROJECT_OWNER);

        userRepository.save(currentUser);
    }

    public void setRole(Project project, User currentUser, Role role) {
        UserProjectRoleId id = new UserProjectRoleId();
        id.setUserId(currentUser.getId());
        id.setProjectId(project.getId());

        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setId(id);
        userProjectRole.setRole(role);

        currentUser.getProjectRoles().add(userProjectRole);
    }

    @Transactional
    public void update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", id));

        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }
}
