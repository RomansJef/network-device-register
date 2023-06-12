package com.register.app.service;

import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceTopologyResponse;

/**
 * Get Device Topology Interface
 */
public interface iGetDeviceTopologyService {

    GetDeviceTopologyResponse executeRequest(GetDeviceByMacAddressRequest getDeviceByMacAddressRequest);
}
