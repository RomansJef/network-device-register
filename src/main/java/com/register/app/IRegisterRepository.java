package com.register.app;

import com.register.app.entities.DeviceEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "register", path = "register")
public interface IRegisterRepository extends JpaRepository<DeviceEntity, String> {

    List<DeviceEntity> findAllByOrderByPublishedDateDesc();

    Optional<DeviceEntity> findByMacAddress(@Param("deviceType") @NonNull String deviceType);
}
