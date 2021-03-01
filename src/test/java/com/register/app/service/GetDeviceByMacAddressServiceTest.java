package com.register.app.service;

import com.register.app.entities.DeviceEntity;
import com.register.app.RegisterRepository;
import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceByMacAddressResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GetDeviceByMacAddressServiceTest {

    private final RegisterRepository registerRepository = mock(RegisterRepository.class);
    private final GetDeviceByMacAddressService getDeviceByMacAddressService =
            new GetDeviceByMacAddressService(registerRepository);
    private GetDeviceByMacAddressRequest getDeviceByMacAddressRequest;
    private DeviceEntity deviceEntity;

    @Before
    public void setUp() {
        // request
        getDeviceByMacAddressRequest =
                GetDeviceByMacAddressRequest.builder().macAddress("A1:B2:C3:D4:E5:F6").build();
        // entity
        deviceEntity =
                DeviceEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .deviceType("Switch")
                        .macAddress("A1:B2:C3:D4:E5:F6")
                        .uplinkMacAddress("01:02:03:04:05:06")
                        .publishedDate(LocalDateTime.now())
                        .build();
    }

    @Test
    public void getDeviceByMacAddressSuccess() {
        when(registerRepository.getDeviceByMacAddress(getDeviceByMacAddressRequest.getMacAddress()))
                .thenReturn(Optional.of(deviceEntity));
        GetDeviceByMacAddressResponse actual =
                getDeviceByMacAddressService.executeRequest(getDeviceByMacAddressRequest);
        verify(registerRepository, times(1))
                .getDeviceByMacAddress(getDeviceByMacAddressRequest.getMacAddress());
        assertThat(actual.getDeviceType()).isEqualTo(deviceEntity.getDeviceType());
        assertThat(actual.getMacAddress()).isEqualTo(deviceEntity.getMacAddress());
    }

    @Test
    public void getDeviceByMacAddressError() {
        when(registerRepository.getDeviceByMacAddress("")).thenReturn(Optional.of(deviceEntity));
        assertThrows(
                RuntimeException.class,
                () -> getDeviceByMacAddressService.executeRequest(getDeviceByMacAddressRequest));
    }

    @Test
    public void whenGetDeviceTopologyMacAddressNull() {
        // request
        GetDeviceByMacAddressRequest deviceRequest =
                GetDeviceByMacAddressRequest.builder()
                        .macAddress(null)
                        .build();
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> getDeviceByMacAddressService.executeRequest(deviceRequest));
        assertThat(wrongType.getMessage()).isEqualTo("0b9cb287-b79e-4e82-9d16-9c2415d4c00b - Device MacAddress in GetDeviceByMacAddressRequest cannot be null");
    }

    @Test
    public void whenGetDeviceTopologyMacAddressWrong() {
        // request
        GetDeviceByMacAddressRequest deviceRequest =
                GetDeviceByMacAddressRequest.builder()
                        .macAddress("wrong")
                        .build();
        List<DeviceEntity> deviceEntityList = new ArrayList<>();
        deviceEntityList.add(deviceEntity);
        when(registerRepository.retrieveDevices()).thenReturn(deviceEntityList);
        RuntimeException wrongType = assertThrows(
                RuntimeException.class,
                () -> getDeviceByMacAddressService.executeRequest(deviceRequest));
        assertThat(wrongType.getMessage()).isEqualTo("a7839efe-12d1-4c2d-b7d1-3fc2b621dced - Device MacAddress in GetDeviceByMacAddressRequest is wrong");
    }
}
