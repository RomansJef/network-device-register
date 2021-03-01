package com.register.app.service;

import static com.register.app.service.ListNewDevicesService.ACCESS_POINT;
import static com.register.app.service.ListNewDevicesService.SWITCH;
import static com.register.app.service.ListNewDevicesService.GATEWAY;

import com.register.app.RegisterRepository;
import com.register.app.entities.DeviceEntity;
import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceTopologyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * cGet Device Topology
 */
@Log4j2
@Service
public class GetDeviceTopologyService {
    private final RegisterRepository registerRepository;

    public GetDeviceTopologyService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public GetDeviceTopologyResponse executeRequest(
            GetDeviceByMacAddressRequest getDeviceByMacAddressRequest) {
        validateMacAddress(getDeviceByMacAddressRequest.getMacAddress());
        List<DeviceEntity> deviceEntities = registerRepository.retrieveDevices();

        Map<String, List<String>> switchnodes =
                new HashMap<>(getDevicesToMap(getAccessPoints(deviceEntities)));
        String result = null;

        for (DeviceEntity device : deviceEntities) {
            if (getDeviceByMacAddressRequest.getMacAddress().equals(device.getMacAddress())
                    && (ACCESS_POINT).equals(device.getDeviceType())) {
                result = getDeviceByMacAddressRequest.getMacAddress();
            } else if (getDeviceByMacAddressRequest.getMacAddress().equals(device.getMacAddress())
                    && (SWITCH).equals(device.getDeviceType())) {
                if (switchnodes.get(device.getMacAddress()) != null && !switchnodes.get(device.getMacAddress()).isEmpty()) {
                    result = device.getMacAddress() + ": " + switchnodes.get(device.getMacAddress()).toString();
                } else {
                    Map<String, List<String>> emptyMap = new HashMap<>();
                    result = device.getMacAddress() + ": " + emptyMap;
                }
            } else if (getDeviceByMacAddressRequest.getMacAddress().equals(device.getMacAddress())
                    && (GATEWAY).equals(device.getDeviceType())) {
                result =
                        getTopologyForGateway(deviceEntities, getDeviceByMacAddressRequest.getMacAddress());
            }
        }
        return GetDeviceTopologyResponse.builder().devices(result).build();
    }

    /**
     * Get map of Gateways, Switches and Access Points
     */
    private String getTopologyForGateway(List<DeviceEntity> deviceEntities, String macAddress) {
        List<String> emptyList = new ArrayList<>();
        Map<String, List<String>> switchnodes =
                new HashMap<>(getDevicesToMap(getAccessPoints(deviceEntities)));
        Map<String, Map<String, List<String>>> root = new HashMap<>();

        for (DeviceEntity gateway : getGateways(deviceEntities)) {
            if (gateway.getMacAddress().equals(macAddress)) {
                Map<String, List<String>> swnodes = new HashMap<>();
                for (DeviceEntity aSwitch : getSwitches(deviceEntities)) {
                    if (macAddress.equals(aSwitch.getUplinkMacAddress())) {
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
        }
        return root.toString();
    }

    /**
     * Get map of Switches and Access Points.
     */
    private Map<String, List<String>> getDevicesToMap(List<DeviceEntity> deviceList) {
        return deviceList.stream()
                .collect(
                        groupingBy(
                                DeviceEntity::getUplinkMacAddress,
                                LinkedHashMap::new,
                                mapping(DeviceEntity::getMacAddress, toList())));
    }

    /**
     * Get list of Gateways.
     */
    private List<DeviceEntity> getGateways(List<DeviceEntity> deviceList) {
        List<DeviceEntity> gateways = new ArrayList<>();
        for (DeviceEntity device : deviceList) {
            if ((GATEWAY).equals(device.getDeviceType())) {
                gateways.add(device);
            }
        }
        return gateways;
    }

    /**
     * Get list of Switches.
     */
    private List<DeviceEntity> getSwitches(List<DeviceEntity> deviceList) {
        List<DeviceEntity> switches = new ArrayList<>();
        for (DeviceEntity device : deviceList) {
            if ((SWITCH).equals(device.getDeviceType())) {
                switches.add(device);
            }
        }
        return switches;
    }

    /**
     * Get list of Access Points.
     */
    private List<DeviceEntity> getAccessPoints(List<DeviceEntity> deviceList) {
        List<DeviceEntity> accessPoints = new ArrayList<>();
        for (DeviceEntity device : deviceList) {
            if ((ACCESS_POINT).equals(device.getDeviceType())) {
                accessPoints.add(device);
            }
        }
        return accessPoints;
    }

    /**
     * Validates device MacAddress
     */
    private void validateMacAddress(String macAddress) {
        Validate.notNull(macAddress, "60437ec1-9d7b-45fb-81db-50d1bd68045d - Device MacAddress in GetDeviceTopologyRequest cannot be null");
        Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
        Matcher matcher = pattern.matcher(macAddress);
        if (!matcher.find()) {
            throw new RuntimeException("109987e8-059c-4cab-b499-066c7e76526a - Device MacAddress in GetDeviceTopologyRequest is wrong");
        }
    }
}
