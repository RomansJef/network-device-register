package com.register.app.controller;

import com.register.app.RegisterController;
import com.register.app.impl.GetAllDevicesTreeService;
import com.register.app.response.GetAllDevicesTreeResponse;
import com.register.app.service.iGetDeviceByMacAddressService;
import com.register.app.service.iGetDeviceTopologyService;
import com.register.app.service.iGetDevicesSortedByTypeService;
import com.register.app.service.iListNewDevicesService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class GetAllDevicesTreeControllerTest {
  private final iListNewDevicesService listNewDevicesService = mock(iListNewDevicesService.class);
  private final GetAllDevicesTreeService getAllDevicesTreeService =
      mock(GetAllDevicesTreeService.class);
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
    List<String> entityList = new ArrayList<>();
    Map<String, List<String>> mapz = new HashMap<>();
    Map<String, Map<String, List<String>>> mapzzz = new HashMap<>();
    // response
    entityList.add("A1:B2:C3:D4:E5:E6");
    entityList.add("C1:C2:C3:C4:C5:C6");
    mapz.put("00:01:02:03:04:05:06", entityList);
    mapzzz.put("10:11:12:13:14:15:16", mapz);

    GetAllDevicesTreeResponse getDevices =
            GetAllDevicesTreeResponse.builder().devicesList(mapzzz).build();
    when(getAllDevicesTreeService.executeRequest()).thenReturn(getDevices);

    // then
    mockMvc
        .perform(
            get("/getAllDevices")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.devicesList").exists())
        .andDo(print());
  }
}
