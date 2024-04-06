package org.airport.example.mapper;

import org.airport.example.model.AirPortModel;
import org.airport.example.repository.entity.AirPortEntity;
import org.airport.example.rest.request.AirPortCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Component for data mapping between different AirPort object's representations.
 */
@Mapper(componentModel = "cdi",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AirPortEntityModelMapper {
    AirPortModel toModel(AirPortEntity entity);
    List<AirPortModel> toModelList(List<AirPortEntity> entityList);
    AirPortEntity toEntity(AirPortModel model);
    AirPortModel requestToModel(AirPortCreateRequest createRequest);
}
