package com.register.app;

import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.request.NewDeviceRequest;
import com.register.app.response.GetAllDevicesTreeResponse;
import com.register.app.response.GetDeviceByMacAddressResponse;
import com.register.app.response.GetDeviceTopologyResponse;
import com.register.app.response.GetDevicesSortedByTypeResponse;
import com.register.app.response.ListNewDevicesResponse;
import com.register.app.service.iGetAllDevicesTreeService;
import com.register.app.service.iGetDeviceByMacAddressService;
import com.register.app.service.iGetDeviceTopologyService;
import com.register.app.service.iGetDevicesSortedByTypeService;
import com.register.app.service.iListNewDevicesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/** Controller for Network devices register. */
@Log4j2
@Api(tags = "Network devices register app controller")
@RestController
public class RegisterController {

  private final iListNewDevicesService listNewDevicesService;
  private final iGetAllDevicesTreeService getAllDevicesTreeService;
  private final iGetDeviceByMacAddressService getDeviceByMacAddressService;
  private final iGetDevicesSortedByTypeService getDevicesSortedByTypeService;
  private final iGetDeviceTopologyService getDeviceTopologyService;

  public RegisterController(
          iListNewDevicesService listNewDevicesService,
          iGetAllDevicesTreeService getAllDevicesTreeService,
          iGetDeviceByMacAddressService getDeviceByMacAddressService,
          iGetDevicesSortedByTypeService getDevicesSortedByTypeService,
          iGetDeviceTopologyService getDeviceTopologyService) {
    this.listNewDevicesService = listNewDevicesService;
    this.getAllDevicesTreeService = getAllDevicesTreeService;
    this.getDeviceByMacAddressService = getDeviceByMacAddressService;
    this.getDevicesSortedByTypeService = getDevicesSortedByTypeService;
    this.getDeviceTopologyService = getDeviceTopologyService;
  }

  /** Store list of new devices. */
  @ApiOperation(
      value = "Save list of new devices",
      nickname = "saveListNewDevices",
      notes = "Save list of new devices")
  @PostMapping(value = "/newDevices", consumes = "application/json")
  public ListNewDevicesResponse postNewDevices(
      @ApiParam(value = "Save list of new devices request data body") @RequestBody @Valid
          List<NewDeviceRequest> request) {
    return listNewDevicesService.executeRequest(request);
  }

  /** Get all devices. */
  @ApiOperation(
      value = "Get all devices stored in DB",
      nickname = "AllDevices",
      notes = "Get all devices")
  @GetMapping(value = "/getAllDevices", produces = "application/json")
  public GetAllDevicesTreeResponse getAllDevices() {
    return getAllDevicesTreeService.executeRequest();
  }

  /** Get devices by macAddress. */
  @ApiOperation(
      value = "Get device by macAddress",
      nickname = "DeviceByMacAddress",
      notes = "Get device by macAddress")
  @GetMapping(
      value = "/deviceByMacAddress",
      consumes = "application/json",
      produces = "application/json")
  public GetDeviceByMacAddressResponse getDeviceByMacAddress(
      @ApiParam(value = "Get device by macAddress request data body") @RequestBody @Valid
          GetDeviceByMacAddressRequest deviceByMacAddress) {
    return getDeviceByMacAddressService.executeRequest(deviceByMacAddress);
  }

  /** Get all devices sorted by type. */
  @ApiOperation(
      value = "Get all devices sorted by type",
      nickname = "AllDevicesSortedByType",
      notes = "Get all devices sorted by type")
  @GetMapping(value = "/getDevicesSortedByType", produces = "application/json")
  public GetDevicesSortedByTypeResponse getDevicesSortedByType() {
    return getDevicesSortedByTypeService.executeRequest();
  }

  /** Get topology of device. */
  @ApiOperation(
      value = "Get topology of device",
      nickname = "TopologyOfDevice",
      notes = "Get topology of device")
  @GetMapping(value = "/getDeviceTopology", produces = "application/json")
  public GetDeviceTopologyResponse getDeviceTopology(
      @ApiParam(value = "Get device topology request data body") @RequestBody @Valid
          GetDeviceByMacAddressRequest deviceByMacAddress) {
    return getDeviceTopologyService.executeRequest(deviceByMacAddress);
  }
}
