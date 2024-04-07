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

    /**
     * Create new AirPort instance
     * @param airPortModel airport model with data
     * @param userModel user model with PK ID
     * @return created airport
     */
    @Transactional
    public AirPortModel create(AirPortModel airPortModel, UserModel userModel) {
        Objects.requireNonNull(airPortModel, "airport model is NULL");
        Objects.requireNonNull(userModel, "user model is NULL");

        AirPortEntity airPortEntity = airPortMapper.toEntity(airPortModel);
        UserEntity userEntity = userMapper.toEntity(userModel);
        airPortEntity.setUser(userEntity);
        log.trace("Prepare saving AirPort: {}", airPortEntity);
//        System.out.println("Prepare saving AirPort: " + airPortEntity);

        try {
            portRepository.createAirPort(airPortEntity);
            log.debug("Created AirPort: {}", airPortModel);
//            System.out.println("Created AirPort: " + airPortEntity);
        } catch (Exception e) {
            throw new AirPortCreateException("Failed AirPort creation", e);
        }
//        return airPortMapper.toModel(airPortEntity);
        return airPortMapper.toModel(airPortEntity, new AirPortModel());
    }

    /**
     * Update existing AirPort record with new data.
     * @param airPortModel new airport data
     * @param userModel existing user
     * @return updated airport model
     */
    @Transactional
    public AirPortModel update(AirPortModel airPortModel, UserModel userModel) {
        Objects.requireNonNull(airPortModel, "airport model is NULL");
        Objects.requireNonNull(userModel, "user model is NULL");

        AirPortEntity airPortEntity = airPortMapper.toEntity(airPortModel);
        UserEntity userEntity = userMapper.toEntity(userModel);
        airPortEntity.setUser(userEntity);
//        System.out.println("Prepare saving AirPort: " + airPortEntity);
        log.debug("Prepare saving AirPort: {}", airPortEntity);

        try {
            portRepository.update(airPortEntity);
//            log.debug("Updated AirPort: {}", airPortEntity);
            System.out.println("Updated AirPort: " + airPortEntity);
        } catch (Exception e) {
            throw new AirPortCreateException("Failed AirPort update", e);
        }
//        return airPortMapper.toModel(airPortEntity);
        return airPortMapper.toModel(airPortEntity, new AirPortModel());
    }

    /**
     * Delete AirPort by PK ID if exists. Do nothing when record doesn't exist.
     * @param airPortId to be deleted Id
     * @return one if deleted, zero otherwise
     */
    @Transactional
    public int delete(Long airPortId) {
        Objects.requireNonNull(airPortId, "airport ID is NULL");

        log.trace("Prepare deleting AirPort by {}", airPortId);
//        System.out.println("Prepare deleting AirPort by " + airPortId);

        int deletedCount;
        try {
            deletedCount = portRepository.delete(airPortId);
            log.debug("Deleted AirPort with: {}", airPortId);
//            System.out.println("Deleted AirPort with: " + airPortId);
        } catch (Exception e) {
//            log.error("Deleting error", e);
            throw new AirPortCreateException("Failed AirPort delete", e);
        }
        return deletedCount;
    }

    /**
     * Search AirPort records by using possible parameters.
     *
     * @param airPortName name field
     * @param code code field
     * @return found list
     */
    public List<AirPortModel> getByParameters(String airPortName, String code) {
        var airPortEntityList =  portRepository.findByNameOrCode(airPortName, code);
//        System.out.println("AP list = " + airPortEntityList);
        List airPortList = airPortMapper.toModelList(airPortEntityList);
//        System.out.println("Found by name: " + airPortName + ", = " + airPortList.size());
        return airPortList;
    }

}
