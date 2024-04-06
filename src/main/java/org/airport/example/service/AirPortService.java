package org.airport.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.exception.AirPortCreateException;
import org.airport.example.mapper.AirPortEntityModelMapper;
import org.airport.example.mapper.UserEntityModelMapper;
import org.airport.example.model.AirPortModel;
import org.airport.example.model.UserModel;
import org.airport.example.repository.AirPortRepository;
import org.airport.example.repository.entity.AirPortEntity;
import org.airport.example.repository.entity.UserEntity;

import java.util.List;
import java.util.Objects;

/**
 * Business logic service for AirPort management
 */
@ApplicationScoped
@Slf4j
public class AirPortService {
    @Inject
    AirPortRepository portRepository;
    @Inject
    private AirPortEntityModelMapper airPortMapper;
    @Inject
    private UserEntityModelMapper userMapper;

    @Transactional
    public AirPortModel create(AirPortModel airPortModel, UserModel userModel) {
        Objects.requireNonNull(airPortModel, "airport model is NULL");
        Objects.requireNonNull(userModel, "user model is NULL");

        AirPortEntity airPortEntity = airPortMapper.toEntity(airPortModel);
        UserEntity userEntity = userMapper.toEntity(userModel);
        airPortEntity.setUser(userEntity);
        System.out.println("Prepare saving AirPort: " + airPortEntity);

        try {
            portRepository.createAirPort(airPortEntity);
//            log.debug("Created AirPort: {}", airPortModel);
            System.out.println("Created AirPort: " + airPortEntity);
        } catch (Exception e) {
            throw new AirPortCreateException("Failed AirPort creation", e);
        }
        return airPortMapper.toModel(airPortEntity);
    }

    @Transactional
    public AirPortModel update(AirPortModel airPortModel, UserModel userModel) {
        Objects.requireNonNull(airPortModel, "airport model is NULL");
        Objects.requireNonNull(userModel, "user model is NULL");

        AirPortEntity airPortEntity = airPortMapper.toEntity(airPortModel);
        UserEntity userEntity = userMapper.toEntity(userModel);
        airPortEntity.setUser(userEntity);
        System.out.println("Prepare saving AirPort: " + airPortEntity);

        try {
            portRepository.update(airPortEntity);
//            log.debug("Updated AirPort: {}", airPortEntity);
            System.out.println("Updated AirPort: " + airPortEntity);
        } catch (Exception e) {
            throw new AirPortCreateException("Failed AirPort update", e);
        }
        return airPortMapper.toModel(airPortEntity);
    }

    /**
     * Get all instances from DB.
     *  TODO! We should do PAGINATION here.
     * @return all instances
     */
    public List<AirPortModel> getAll() {
        return airPortMapper.toModelList(portRepository.getAll());
    }

    public List<AirPortModel> getByName(String airPortName) {
        Objects.requireNonNull(airPortName, "airport name is NULL");
        var airPortEntityList =  portRepository.findByName(airPortName);
        List<AirPortModel> airPortList = airPortMapper.toModelList(airPortEntityList);
        System.out.println("Found by name: " + airPortName + ", = " + airPortList.size());
        return airPortList;
    }

}
