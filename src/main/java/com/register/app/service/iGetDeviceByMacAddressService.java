package com.register.app.service;

import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceByMacAddressResponse;
import org.springframework.stereotype.Service;

/**
 * Get Devices By macAddress Service.
 */
public interface iGetDeviceByMacAddressService {

    GetDeviceByMacAddressResponse executeRequest(GetDeviceByMacAddressRequest getDeviceByMacAddressRequest);
}
