package com.register.app.service;

import com.register.app.RegisterRepository;
import com.register.app.entities.DeviceEntity;
import com.register.app.entities.ResponseEntity;
import com.register.app.response.GetDevicesSortedByTypeResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

public class GetDevicesSortedByTypeServiceTest {
    private final RegisterRepository registerRepository = mock(RegisterRepository.class);
    private final GetDevicesSortedByTypeService getDevicesSortedByTypeService =
            new GetDevicesSortedByTypeService(registerRepository);
    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;
    private DeviceEntity deviceEntity3;
    private ResponseEntity responseEntity1;
    private ResponseEntity responseEntity2;
    private ResponseEntity responseEntity3;

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
        deviceEntity3 =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("Access Point")
                        .macAddress("31:32:33:34:35:36")
                        .uplinkMacAddress("B1:B2:B3:B4:B5:B6")
                        .publishedDate(LocalDateTime.now())
                        .build();
        // response entities
        responseEntity1 =
                ResponseEntity.builder()
                        .deviceType(deviceEntity1.getDeviceType())
                        .macAddress(deviceEntity1.getMacAddress())
                        .build();
        responseEntity2 =
                ResponseEntity.builder()
                        .deviceType(deviceEntity2.getDeviceType())
                        .macAddress(deviceEntity2.getMacAddress())
                        .build();
        responseEntity3 =
                ResponseEntity.builder()
                        .deviceType(deviceEntity3.getDeviceType())
                        .macAddress(deviceEntity3.getMacAddress())
                        .build();
    }

    @Test
    public void getDevicesSortedByTypeSuccess() {
        // response from db
        List<DeviceEntity> deviceList = new ArrayList<>();
        deviceList.add(deviceEntity1);
        deviceList.add(deviceEntity2);
        deviceList.add(deviceEntity3);
        // response
        List<ResponseEntity> entityList = new ArrayList<>();
        entityList.add(responseEntity1);
        entityList.add(responseEntity2);
        entityList.add(responseEntity3);

        when(registerRepository.retrieveDevices()).thenReturn(deviceList);
        GetDevicesSortedByTypeResponse actual = getDevicesSortedByTypeService.executeRequest();
        verify(registerRepository, times(1)).retrieveDevices();
        Assertions.assertThat(actual.getDevicesList()).isEqualTo(entityList);
    }

    @Test
    public void getDevicesSortedByTypeWrongType() {
        DeviceEntity deviceEntity =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("way")
                        .macAddress("A1:B2:C3:D4:E5:F6")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .publishedDate(LocalDateTime.now())
                        .build();
        // response from db
        List<DeviceEntity> deviceList = new ArrayList<>();
        deviceList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                getDevicesSortedByTypeService::executeRequest);
        assertThat(wrongType.getMessage()).isEqualTo("d60cd427-4fbd-4367-8308-166cc57d7486 - Device type is not defined.");
    }
}
