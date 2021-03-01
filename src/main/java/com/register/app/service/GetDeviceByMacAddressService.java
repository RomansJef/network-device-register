package com.register.app.service;

import com.register.app.entities.DeviceEntity;
import com.register.app.RegisterRepository;
import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceByMacAddressResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Get Devices By macAddress Service.
 */
@Log4j2
@Service
public class GetDeviceByMacAddressService {

    private final RegisterRepository registerRepository;

    public GetDeviceByMacAddressService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public GetDeviceByMacAddressResponse executeRequest(GetDeviceByMacAddressRequest getDeviceByMacAddressRequest) {
        validateMacAddress(getDeviceByMacAddressRequest.getMacAddress());
        String macAddress = getDeviceByMacAddressRequest.getMacAddress();
        DeviceEntity deviceByMacAddress = registerRepository.getDeviceByMacAddress(macAddress).orElseThrow(
                () -> new RuntimeException("580425d2-b7db-4b56-b244-5fec900a8741 - Find by MacAddress error"));
        return GetDeviceByMacAddressResponse.builder()
                .deviceType(deviceByMacAddress.getDeviceType())
                .macAddress(deviceByMacAddress.getMacAddress())
                .build();
    }

    /**
     * Validates MacAddress in request
     */
    private void validateMacAddress(String macAddress) {
        Validate.notNull(macAddress, "0b9cb287-b79e-4e82-9d16-9c2415d4c00b - Device MacAddress in GetDeviceByMacAddressRequest cannot be null");
        Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
        Matcher matcher = pattern.matcher(macAddress);
        if (!matcher.find()) {
            throw new RuntimeException("a7839efe-12d1-4c2d-b7d1-3fc2b621dced - Device MacAddress in GetDeviceByMacAddressRequest is wrong");
        }
    }
}
