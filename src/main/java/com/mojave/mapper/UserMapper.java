package com.mojave.mapper;


import com.mojave.dto.UserDTO;
import com.mojave.model.User;
import com.mojave.security.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    UserDTO toUserDTO(User user);

    UserDTO principalToUserDTO(UserPrincipal principal);

    List<UserDTO> toUserDTOs(List<User> users);

    User toUser(UserDTO userDTO);
}
