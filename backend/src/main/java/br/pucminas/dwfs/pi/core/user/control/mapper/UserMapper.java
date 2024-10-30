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

/**
 * Mapper of user data.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
@Mapper(componentModel = "jakarta", imports = UserRole.class)
public interface UserMapper {

    /**
     * Converts a list of users into a list of users DTO.
     * 
     * @param users The list of users to be converted.
     * @return The converted list of users DTO.
     */
    List<UserDto> fromUsers_toUsersDto(List<User> users);

    /**
     * Converts an user into an user DTO.
     * 
     * @param user The user to be converted.
     * @return The converted user DTO.
     */
    UserDto fromUser_toUserDto(User user);

    /**
     * Converts an user create DTO into an user.
     * 
     * @param userCreateDto The user create DTO to be converted.
     * @return The converted user.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(UserRole.USER)")
    User fromUserCreateDto_toUser(UserCreateDto userCreateDto);

    /**
     * Convers an user update DTO into an user.
     * 
     * @param userUpdateDto The user update DTO to be converted.
     * @return The converted user.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    User fromUserUpdateDto_toUser(UserUpdateDto userUpdateDto);
}