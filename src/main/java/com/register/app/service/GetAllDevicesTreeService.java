package com.register.app.service;

import static com.register.app.service.ListNewDevicesService.ACCESS_POINT;
import static com.register.app.service.ListNewDevicesService.SWITCH;
import static com.register.app.service.ListNewDevicesService.GATEWAY;

import com.register.app.entities.DeviceEntity;
import com.register.app.RegisterRepository;
import com.register.app.response.GetAllDevicesTreeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.*;

/**
 * Get All Devices Service.
 */
@Log4j2
@Service
public class GetAllDevicesTreeService {

    private final RegisterRepository registerRepository;

    public GetAllDevicesTreeService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public GetAllDevicesTreeResponse executeRequest() {
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

        Map<String, Map<String, List<String>>> root = new HashMap<>();
        Map<String, List<String>> switchnodes = new HashMap<>(getDevicesToMap(accessPoints));
        List<String> emptyList = new ArrayList<>();

        for (DeviceEntity gateway : gateways) {
            Map<String, List<String>> swnodes = new HashMap<>();
            for (DeviceEntity aSwitch : switches) {
                if (gateway.getMacAddress().equals(aSwitch.getUplinkMacAddress())) {
                    if (switchnodes.get(aSwitch.getMacAddress()) != null) {
                        swnodes.put(aSwitch.getMacAddress(), switchnodes.get(aSwitch.getMacAddress()));
                    }
                    if (switchnodes.get(aSwitch.getMacAddress()) == null) {
                        swnodes.put(aSwitch.getMacAddress(), emptyList);
                    }
                }
                root.put(gateway.getMacAddress(), swnodes);
            }
        }
        return GetAllDevicesTreeResponse.builder().devicesList(root).build();
    }

    private Map<String, List<String>> getDevicesToMap(List<DeviceEntity> deviceList) {
        return deviceList.stream()
                .collect(
                        groupingBy(
                                DeviceEntity::getUplinkMacAddress,
                                LinkedHashMap::new,
                                mapping(DeviceEntity::getMacAddress, toList())));
    }
}
