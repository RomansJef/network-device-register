package com.register.app.service;

import com.register.app.RegisterRepository;
import com.register.app.entities.DeviceEntity;
import com.register.app.impl.GetDeviceTopologyService;
import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceTopologyResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllDeviceTopologyServiceTest {
    private final RegisterRepository registerRepository = mock(RegisterRepository.class);
    private final GetDeviceTopologyService getDeviceTopologyService =
            new GetDeviceTopologyService(registerRepository);
    private GetDeviceByMacAddressRequest getDeviceByMacAddressRequest;
    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;
    private DeviceEntity deviceEntity3;

    @Before
    public void setUp() {
        // request
        getDeviceByMacAddressRequest =
                GetDeviceByMacAddressRequest.builder().macAddress("A1:B2:C3:D4:E5:F6").build();
        // entities
        deviceEntity1 =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("Gateway")
                        .macAddress("A1:B2:C3:D4:E5:F6")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .publishedDate(LocalDateTime.now())
                        .build();
        deviceEntity2 =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("Switch")
                        .macAddress("B1:B2:B3:B4:B5:B6")
                        .uplinkMacAddress("A1:B2:C3:D4:E5:F6")
                        .publishedDate(LocalDateTime.now())
                        .build();
        deviceEntity3 =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("Access Point")
                        .macAddress("31:32:33:44:35:36")
                        .uplinkMacAddress("B1:B2:B3:B4:B5:B6")
                        .publishedDate(LocalDateTime.now())
                        .build();
    }

    @Test
    public void getDeviceTopologySuccess() {
        List<String> entityList = new ArrayList<>();
        List<DeviceEntity> deviceList = new ArrayList<>();
        Map<String, List<String>> mapz = new HashMap<>();
        Map<String, Map<String, List<String>>> mapzzz = new HashMap<>();
        deviceList.add(deviceEntity1);
        deviceList.add(deviceEntity2);
        deviceList.add(deviceEntity3);
        // response
        entityList.add("31:32:33:44:35:36");
        mapz.put("B1:B2:B3:B4:B5:B6", entityList);
        mapzzz.put("A1:B2:C3:D4:E5:F6", mapz);

        when(registerRepository.retrieveDevices()).thenReturn(deviceList);
        GetDeviceTopologyResponse actual =
                getDeviceTopologyService.executeRequest(getDeviceByMacAddressRequest);
        verify(registerRepository, times(1)).retrieveDevices();
        Assertions.assertThat(actual.getDevices()).isEqualTo(mapzzz.toString());
    }

    @Test
    public void whenGetDeviceTopologyMacAddressNull() {
        // request
        GetDeviceByMacAddressRequest deviceRequest =
                GetDeviceByMacAddressRequest.builder()
                        .macAddress(null)
                        .build();
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity1);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> getDeviceTopologyService.executeRequest(deviceRequest));
        assertThat(wrongType.getMessage()).isEqualTo("60437ec1-9d7b-45fb-81db-50d1bd68045d - Device MacAddress in GetDeviceTopologyRequest cannot be null");
    }

    @Test
    public void whenGetDeviceTopologyMacAddressWrong() {
        // request
        GetDeviceByMacAddressRequest deviceRequest =
                GetDeviceByMacAddressRequest.builder()
                        .macAddress("wrong")
                        .build();
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity1);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> getDeviceTopologyService.executeRequest(deviceRequest));
        assertThat(wrongType.getMessage()).isEqualTo("109987e8-059c-4cab-b499-066c7e76526a - Device MacAddress in GetDeviceTopologyRequest is wrong");
    }
}
