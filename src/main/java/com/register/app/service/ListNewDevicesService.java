package com.register.app.service;

import com.register.app.entities.DeviceEntity;
import com.register.app.RegisterRepository;
import com.register.app.request.NewDeviceRequest;
import com.register.app.response.ListNewDevicesResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Save New Devices Service.
 */
@Log4j2
@Service
public class ListNewDevicesService {
    private final RegisterRepository registerRepository;
    public static final String ACCESS_POINT = "Access Point";
    public static final String SWITCH = "Switch";
    public static final String GATEWAY = "Gateway";

    public ListNewDevicesService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    private final ListNewDevicesResponse printStatus = new ListNewDevicesResponse();

    public ListNewDevicesResponse executeRequest(List<NewDeviceRequest> newDeviceRequests) {
        Validate.notNull(newDeviceRequests, "a78c5503-f62f-4d36-b193-3481fd999ed4 - Missing request list");
        Validate.notEmpty(newDeviceRequests, "69eb9ed3-d387-4da1-8ba1-55870a0cf421 - No new devices presented in request");
        List<String> statusList = new ArrayList<>();
        List<DeviceEntity> deviceEntities = registerRepository.retrieveDevices();
        for (NewDeviceRequest newDeviceRequest : newDeviceRequests) {
            if (validateIfAlreadyExist(deviceEntities, newDeviceRequest)) {
                DeviceEntity newDeviceEntity =
                        DeviceEntity.builder()
                                .id(UUID.randomUUID().toString())
                                .deviceType(newDeviceRequest.getDeviceType())
                                .macAddress(newDeviceRequest.getMacAddress())
                                .uplinkMacAddress(newDeviceRequest.getUplinkMacAddress())
                                .publishedDate(LocalDateTime.now())
                                .build();
                registerRepository.saveDevices(newDeviceEntity);
                statusList.add("New device " + newDeviceRequest.getMacAddress() + " stored in register");
            } else {
                statusList.add("Device " + newDeviceRequest.getMacAddress() + " is already in register");
            }
        }
        printStatus.setSaveStatus(statusList);
        return printStatus;
    }

    /**
     * Check if device already stored in DB
     */
    public boolean validateIfAlreadyExist(
            List<DeviceEntity> deviceEntities, NewDeviceRequest newDeviceRequest) {
        for (DeviceEntity deviceEntity : deviceEntities) {
            validateType(newDeviceRequest.getDeviceType());
            validateMacAddress(newDeviceRequest.getMacAddress());
            validateUplinkMacAddress(newDeviceRequest.getUplinkMacAddress());
            if (newDeviceRequest.getMacAddress().equals(deviceEntity.getMacAddress())) return false;
        }
        return true;
    }

    /**
     * Validates device type
     */
    private void validateType(String type) {
        Validate.notNull(type, "6087e32e-c548-47e2-a93b-85b9a0b56743 - Device type cannot be null");
        if (!type.equals(ACCESS_POINT) && !type.equals(SWITCH) && !type.equals(GATEWAY)) {
            throw new RuntimeException("588ceeaf-99a4-429c-8ebc-65f429b39833 - Device type is wrong");
        }
    }

    /**
     * Validates device MacAddress
     */
    private void validateMacAddress(String macAddress) {
        Validate.notNull(macAddress, "15f81b69-531a-476c-8746-b188c671204b - Device MacAddress cannot be null");
        Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
        Matcher matcher = pattern.matcher(macAddress);
        if (!matcher.find()) {
            throw new RuntimeException("66fcd95c-0a19-4873-8198-4e4064740250 - Device MacAddress is wrong");
        }
    }

    /**
     * Validates device UplinkMacAddress
     */
    private void validateUplinkMacAddress(String uplinkMacAddress) {
        Validate.notNull(uplinkMacAddress, "4753e23c-eb81-46b5-8b20-8bec4050bae9 - Device UplinkMacAddress cannot be null");
        Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
        Matcher matcher = pattern.matcher(uplinkMacAddress);
        if (!matcher.find()) {
            throw new RuntimeException("5fe2e589-9b32-45e5-968d-2e89f809477a - Device UplinkMacAddress is wrong");
        }
    }
}
