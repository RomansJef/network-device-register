package com.register.app.service;

import com.register.app.RegisterRepository;
import com.register.app.entities.DeviceEntity;
import com.register.app.impl.ListNewDevicesService;
import com.register.app.request.NewDeviceRequest;
import com.register.app.response.ListNewDevicesResponse;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ListNewDeviceServiceTest {
    private final RegisterRepository registerRepository = mock(RegisterRepository.class);
    private final ListNewDevicesService listNewDevicesService =
            new ListNewDevicesService(registerRepository);
    private ListNewDevicesResponse newDevicesResponse = new ListNewDevicesResponse();
    private final List<String> statusList = new ArrayList<>();
    private static final String NEW_DEVICE = "New device B1:B2:B3:B4:B5:B6 stored in register";
    private static final String STORED_DEVICE = "Device A1:B2:C3:D4:E5:F6 is already in register";
    private DeviceEntity deviceEntity;

    @Before
    public void setUp() {
        // entity
        deviceEntity =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("Gateway")
                        .macAddress("A1:B2:C3:D4:E5:F6")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .publishedDate(LocalDateTime.now())
                        .build();
    }

    @Test
    public void whenSaveNewDeviceSuccess() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Switch")
                        .macAddress("B1:B2:B3:B4:B5:B6")
                        .uplinkMacAddress("A1:B2:C3:D4:E5:F6")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        // response
        statusList.add(NEW_DEVICE);
        newDevicesResponse = ListNewDevicesResponse.builder().saveStatus(statusList).build();

        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        ListNewDevicesResponse actual = listNewDevicesService.executeRequest(newDeviceRequests);
        assertThat(actual).isEqualTo(newDevicesResponse);
    }

    @Test
    public void whenSaveNewDeviceAlreadyExist() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Gateway")
                        .macAddress("A1:B2:C3:D4:E5:F6")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        // response
        statusList.add(STORED_DEVICE);
        newDevicesResponse = ListNewDevicesResponse.builder().saveStatus(statusList).build();

        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        ListNewDevicesResponse actual = listNewDevicesService.executeRequest(newDeviceRequests);
        assertThat(actual).isEqualTo(newDevicesResponse);
    }

    @Test
    public void whenSaveNewDeviceWrongType() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("")
                        .macAddress("21:22:23:24:25:26")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(wrongType.getMessage()).isEqualTo("588ceeaf-99a4-429c-8ebc-65f429b39833 - Device type is wrong");
    }

    @Test
    public void whenSaveNewDeviceTypeNull() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType(null)
                        .macAddress("21:22:23:24:25:26")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(wrongType.getMessage()).isEqualTo("6087e32e-c548-47e2-a93b-85b9a0b56743 - Device type cannot be null");
    }

    @Test
    public void whenSaveNewDeviceMacAddressNull() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Switch")
                        .macAddress(null)
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(wrongType.getMessage()).isEqualTo("15f81b69-531a-476c-8746-b188c671204b - Device MacAddress cannot be null");
    }

    @Test
    public void whenSaveNewDeviceWrongMacAddress() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Switch")
                        .macAddress("null")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(wrongType.getMessage()).isEqualTo("66fcd95c-0a19-4873-8198-4e4064740250 - Device MacAddress is wrong");
    }

    @Test
    public void whenSaveNewDeviceUplinkMacAddressNull() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Switch")
                        .macAddress("21:22:23:24:25:26")
                        .uplinkMacAddress(null)
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(wrongType.getMessage()).isEqualTo("4753e23c-eb81-46b5-8b20-8bec4050bae9 - Device UplinkMacAddress cannot be null");
    }

    @Test
    public void whenSaveNewDeviceWrongUplinkMacAddress() {
        // request
        NewDeviceRequest newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Switch")
                        .macAddress("21:22:23:24:25:26")
                        .uplinkMacAddress("wrong")
                        .build();
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(wrongType.getMessage()).isEqualTo("5fe2e589-9b32-45e5-968d-2e89f809477a - Device UplinkMacAddress is wrong");
    }

    @Test
    public void whenSaveNewDeviceEmptyRequestList() {
        // request
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(newDeviceRequests));
        assertThat(exception.getMessage()).isEqualTo("69eb9ed3-d387-4da1-8ba1-55870a0cf421 - No new devices presented in request");
    }

    @Test
    public void whenSaveNewDeviceRequestNull() {
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> listNewDevicesService.executeRequest(null));
        assertThat(exception.getMessage()).isEqualTo("a78c5503-f62f-4d36-b193-3481fd999ed4 - Missing request list");
    }
}
