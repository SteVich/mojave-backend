package com.mojave.mapper;


import com.mojave.dto.UserDTO;
import com.mojave.model.User;
import com.mojave.security.UserPrincipal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    UserDTO principalToUserDTO(UserPrincipal principal);

    List<UserDTO> toUserDTOs(List<User> users);

    User toUser(UserDTO userDTO);
}
