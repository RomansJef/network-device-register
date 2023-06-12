package com.register.app.service;

import com.register.app.request.NewDeviceRequest;
import com.register.app.response.ListNewDevicesResponse;
import java.util.List;

/**
 * Save New Devices Service Interface
 */
public interface iListNewDevicesService {

    ListNewDevicesResponse executeRequest(List<NewDeviceRequest> newDeviceRequests);
}
