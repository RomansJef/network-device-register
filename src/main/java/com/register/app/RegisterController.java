package com.register.app;

import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.request.NewDeviceRequest;
import com.register.app.response.*;
import com.register.app.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** Controller for Network devices register. */
@Log4j2
@Api(tags = "Network devices register app controller")
@RestController
public class RegisterController {

  private final ListNewDevicesService listNewDevicesService;
  private final GetAllDevicesTreeService getAllDevicesTreeService;
  private final GetDeviceByMacAddressService getDeviceByMacAddressService;
  private final GetDevicesSortedByTypeService getDevicesSortedByTypeService;
  private final GetDeviceTopologyService getDeviceTopologyService;

  public RegisterController(
          ListNewDevicesService listNewDevicesService,
          GetAllDevicesTreeService getAllDevicesTreeService,
          GetDeviceByMacAddressService getDeviceByMacAddressService,
          GetDevicesSortedByTypeService getDevicesSortedByTypeService,
          GetDeviceTopologyService getDeviceTopologyService) {
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
