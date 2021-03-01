package com.register.app.service;

import com.register.app.RegisterRepository;
import com.register.app.entities.DeviceEntity;
import com.register.app.response.GetAllDevicesTreeResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllDevicesTreeServiceTest {
    private final RegisterRepository registerRepository = mock(RegisterRepository.class);
    private final GetAllDevicesTreeService getAllDevicesTreeService =
            new GetAllDevicesTreeService(registerRepository);
    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;

    @Before
    public void setUp() {
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
    }

    @Test
    public void getAllDevicesTreeSuccess() {
        List<String> entityList = new ArrayList<>();
        List<DeviceEntity> deviceList = new ArrayList<>();
        Map<String, List<String>> mapz = new HashMap<>();
        Map<String, Map<String, List<String>>> mapzzz = new HashMap<>();
        deviceList.add(deviceEntity1);
        deviceList.add(deviceEntity2);
        // response
        mapz.put("B1:B2:B3:B4:B5:B6", entityList);
        mapzzz.put("A1:B2:C3:D4:E5:F6", mapz);

        when(registerRepository.retrieveDevices()).thenReturn(deviceList);
        GetAllDevicesTreeResponse actual = getAllDevicesTreeService.executeRequest();
        verify(registerRepository, times(1)).retrieveDevices();
        Assertions.assertThat(actual.getDevicesList().toString()).isEqualTo(mapzzz.toString());
    }
}
