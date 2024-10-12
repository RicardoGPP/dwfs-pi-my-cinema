package br.pucminas.dwfs.pi.core.user.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.user.boundary.dto.UserCreateDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserUpdateDto;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mapper(componentModel = "jakarta", imports = UserRole.class)
public interface UserMapper {

    List<UserDto> fromUsers_toUsersDto(List<User> users);

    UserDto fromUser_toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(UserRole.USER)")
    User fromUserCreateDto_toUser(UserCreateDto userCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    User fromUserUpdateDto_toUser(UserUpdateDto userUpdateDto);
}