package com.register.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.app.RegisterController;
import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.response.GetDeviceTopologyResponse;
import com.register.app.service.iGetDeviceByMacAddressService;
import com.register.app.service.iGetDeviceTopologyService;
import com.register.app.service.iGetDevicesSortedByTypeService;
import com.register.app.service.iListNewDevicesService;
import com.register.app.service.iGetAllDevicesTreeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GetDeviceTopologyControllerTest {
  private final iListNewDevicesService listNewDevicesService = mock(iListNewDevicesService.class);
  private final iGetAllDevicesTreeService getAllDevicesTreeService =
      mock(iGetAllDevicesTreeService.class);
  private final iGetDeviceByMacAddressService getDeviceByMacAddressService =
      mock(iGetDeviceByMacAddressService.class);
  private final iGetDevicesSortedByTypeService getDevicesSortedByTypeService =
      mock(iGetDevicesSortedByTypeService.class);
  private final iGetDeviceTopologyService getDeviceTopologyService =
      mock(iGetDeviceTopologyService.class);
  private final RegisterController registerController =
      new RegisterController(
          listNewDevicesService,
          getAllDevicesTreeService,
          getDeviceByMacAddressService,
          getDevicesSortedByTypeService,
          getDeviceTopologyService);
  private MockMvc mockMvc;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new Exception(), registerController).build();
  }

  @Test
  public void whenGetAllBooksSuccess() throws Exception {
    // request
    GetDeviceByMacAddressRequest request =
        GetDeviceByMacAddressRequest.builder().macAddress("A1:B2:C3:D4:E5:E6").build();
    // response
    GetDeviceTopologyResponse getDevices =
        GetDeviceTopologyResponse.builder().devices("A1:B2:C3:D4:E5:E6").build();
    // when
    when(getDeviceTopologyService.executeRequest(request)).thenReturn(getDevices);
    ObjectMapper mapper = new ObjectMapper();

    // then
    mockMvc
        .perform(
            get("/getDeviceTopology")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.devices").exists())
        .andDo(print());
  }
}
