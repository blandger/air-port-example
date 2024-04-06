package org.airport.example.mapper;

import org.airport.example.model.UserModel;
import org.airport.example.repository.entity.UserEntity;
import org.airport.example.rest.request.UserLoginRequest;
import org.airport.example.rest.request.UserRegistrationRequest;
import org.airport.example.rest.respose.UserRegistrationResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * Component for mapping data between different User object's representations.
 */
@Mapper(componentModel = "cdi",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserEntityModelMapper {
    UserModel toModel(UserEntity entity);
    UserEntity toEntity(UserModel model);
    UserModel requestToModel(UserRegistrationRequest registrationRequest);
    UserRegistrationResponse modelToResponse(UserModel model);
    UserModel loginRequestToModel(UserLoginRequest userLoginRequest);
}
