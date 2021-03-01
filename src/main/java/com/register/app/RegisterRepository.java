package com.register.app;

import com.register.app.entities.DeviceEntity;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** Implementation of Device Register Repository interface. */
@Service
public class RegisterRepository {

  /** Device Register Repository. */
  @Autowired private IRegisterRepository iRegisterRepository;

  public void setRegisterRepository(IRegisterRepository registerRepository) {
    this.iRegisterRepository = registerRepository;
  }

  public List<DeviceEntity> retrieveDevices() {
    return iRegisterRepository.findAllByOrderByPublishedDateDesc();
  }

  public Optional<DeviceEntity> getDeviceByMacAddress(@NonNull String macAddress) {
    return iRegisterRepository.findByMacAddress(macAddress);
  }

  public void saveDevices(@NonNull DeviceEntity deviceEntity) {
    iRegisterRepository.save(deviceEntity);
  }

  public void deleteDevice(@NonNull String id) {
    iRegisterRepository.deleteById(id);
  }
}
