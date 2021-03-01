package com.register.app.service;

import static com.register.app.service.ListNewDevicesService.ACCESS_POINT;
import static com.register.app.service.ListNewDevicesService.SWITCH;
import static com.register.app.service.ListNewDevicesService.GATEWAY;

import com.register.app.RegisterRepository;
import com.register.app.entities.DeviceEntity;
import com.register.app.entities.ResponseEntity;
import com.register.app.response.GetDevicesSortedByTypeResponse;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Return all devices stored in DB and sort by type.
 */
@Log4j2
@Service
public class GetDevicesSortedByTypeService {
    private final RegisterRepository registerRepository;

    public GetDevicesSortedByTypeService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public GetDevicesSortedByTypeResponse executeRequest() {
        List<ResponseEntity> responseList = new ArrayList<>();
        List<DeviceEntity> gateways = new ArrayList<>();
        List<DeviceEntity> switches = new ArrayList<>();
        List<DeviceEntity> accessPoints = new ArrayList<>();
        List<DeviceEntity> deviceEntities = registerRepository.retrieveDevices();
        for (DeviceEntity device : deviceEntities) {
            if ((GATEWAY).equals(device.getDeviceType())) {
                gateways.add(device);
            } else if ((SWITCH).equals(device.getDeviceType())) {
                switches.add(device);
            } else if ((ACCESS_POINT).equals(device.getDeviceType())) {
                accessPoints.add(device);
            } else
                throw (new RuntimeException(
                        "d60cd427-4fbd-4367-8308-166cc57d7486 - Device type is not defined."));
        }
        responseList.addAll(addDeviceToList(gateways));
        responseList.addAll(addDeviceToList(switches));
        responseList.addAll(addDeviceToList(accessPoints));
        return GetDevicesSortedByTypeResponse.builder().devicesList(responseList).build();
    }

    /**
     * Return list of devices of one type.
     */
    private List<ResponseEntity> addDeviceToList(List<DeviceEntity> deviceEntities) {
        List<ResponseEntity> responseEntities = new ArrayList<>();
        for (DeviceEntity deviceEntity : deviceEntities) {
            ResponseEntity responseEntity =
                    ResponseEntity.builder()
                            .deviceType(deviceEntity.getDeviceType())
                            .macAddress(deviceEntity.getMacAddress())
                            .build();
            responseEntities.add(responseEntity);
        }
        return responseEntities;
    }
}
