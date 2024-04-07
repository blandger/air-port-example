package org.airport.example.mapper;

import org.airport.example.model.AirPortModel;
import org.airport.example.repository.entity.AirPortEntity;
import org.airport.example.repository.entity.UserEntity;
import org.airport.example.rest.request.AirPortCreateRequest;
import org.mapstruct.*;

import java.util.List;

/**
 * Component for data mapping between different AirPort object's representations.
 */
@Mapper(componentModel = "cdi",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AirPortEntityModelMapper {
    @Named("AirPortEntityToModel")
    @Mapping(target = "created_by", source = "entity.user.username", dateFormat = "yyyy/MM/dd")
//    AirPortModel toModel(AirPortEntity entity);
    AirPortModel toModel(AirPortEntity entity, @MappingTarget AirPortModel model);

    @Condition
    default boolean isUserNotEmpty(UserEntity value) {
        return value != null && !value.getUsername().isEmpty();
    }

//    @Mapping(target = "created_by", source = "entity.user.username")
    @IterableMapping(/*qualifiedByName = "AirPortEntityToModel",*/ dateFormat = "yyyy/MM/dd")
    List<AirPortModel> toModelList(List<AirPortEntity> entityList);
    AirPortEntity toEntity(AirPortModel model);
    AirPortModel requestToModel(AirPortCreateRequest createRequest);
}
