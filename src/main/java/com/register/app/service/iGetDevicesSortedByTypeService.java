package com.register.app.service;

import com.register.app.response.GetDevicesSortedByTypeResponse;

/**
 * Interface to get all devices stored in DB and sort by type
 */
public interface iGetDevicesSortedByTypeService {

    GetDevicesSortedByTypeResponse executeRequest();
}
