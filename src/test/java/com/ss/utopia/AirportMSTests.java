// package com.ss.utopia;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.ss.utopia.exceptions.AirportAlreadyExistsException;
// import com.ss.utopia.exceptions.AirportNotFoundException;
// import com.ss.utopia.models.Airport;
// import com.ss.utopia.models.HttpError;
// import com.ss.utopia.services.AirportService;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Locale;
// import java.util.Map;
// import java.util.stream.Collectors;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @SpringBootTest
// class AirportMSTests {

//   final String SERVICE_PATH_AIRPORTS = "/airports";

//   @Mock
//   AirportService service;

//   @InjectMocks
//   AirportController controller;

//   MockMvc mvc;
//   Airport testAirport;
//   List<Airport> testAirportList;

//   @BeforeEach
//   void setup() throws Exception {
//     mvc = MockMvcBuilders.standaloneSetup(controller).build();
//     Mockito.reset(service);

//     testAirport = new Airport("TES", "Testsville");

//     testAirportList = new ArrayList<Airport>();
//     testAirportList.add(new Airport("DCA", "Washington DC"));
//     testAirportList.add(new Airport("IAH", "Huston"));
//     testAirportList.add(new Airport("JFK", "New York"));
//     testAirportList.add(new Airport("LGA", "New York"));
//     testAirportList.add(new Airport("LAX", "Los Angeles"));
//     testAirportList.add(new Airport("MCO", "Orlando"));
//     testAirportList.add(new Airport("MIA", "Miami"));
//     testAirportList.add(new Airport("ORD", "Chicago"));
//     testAirportList.add(new Airport("SAN", "San Diego"));
//     testAirportList.add(new Airport("SFO", "San Francisco"));
//     testAirportList.add(new Airport("SJU", "Puerto Rico"));
//   }

//   @Test
//   void test_validAirportModel_getAirportIataId() throws Exception {
//     assertEquals("TES", testAirport.getAirportIataId());
//   }

//   @Test
//   void test_validAirportModel_getAirportCityName() throws Exception {
//     assertEquals("Testsville", testAirport.getAirportCityName());
//   }

//   @Test
//   void test_findAllAirports_withValidAirports_thenStatus200() throws Exception {
//     when(service.findAll()).thenReturn(testAirportList);

//     MvcResult response = mvc
//       .perform(get(SERVICE_PATH_AIRPORTS).header("Accept", "application/json"))
//       .andExpect(status().is(200))
//       .andReturn();

//     List<Airport> actual = Arrays
//       .stream(
//         new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), Airport[].class)
//       )
//       .collect(Collectors.toList());

//     assertEquals(testAirportList.size(), actual.size());
//     for (int i = 0; i < testAirportList.size(); i++) {
//       assertEquals(
//         testAirportList.get(i).getAirportIataId(),
//         actual.get(i).getAirportIataId()
//       );
//       assertEquals(
//         testAirportList.get(i).getAirportCityName(),
//         actual.get(i).getAirportCityName()
//       );
//     }
//   }

//   @Test
//   void test_findAllAirports_withNoValidAirports_thenStatus204()
//     throws Exception {
//     when(service.findAll()).thenReturn(Collections.emptyList());

//     mvc
//       .perform(get(SERVICE_PATH_AIRPORTS).header("Accept", "application/json"))
//       .andExpect(status().is(204))
//       .andReturn();
//   }

//   @Test
//   void test_findByIataId_withValidAirport_thenStatus200() throws Exception {
//     when(service.findByIataId(testAirport.getAirportIataId()))
//       .thenReturn(testAirport);

//     MvcResult response = mvc
//       .perform(
//         get(SERVICE_PATH_AIRPORTS + "/" + testAirport.getAirportIataId())
//           .header("Accept", "application/json")
//       )
//       .andExpect(status().is(200))
//       .andReturn();

//     Airport actual = new ObjectMapper()
//     .readValue(response.getResponse().getContentAsString(), Airport.class);

//     assertEquals(testAirport.getAirportIataId(), actual.getAirportIataId());
//     assertEquals(testAirport.getAirportCityName(), actual.getAirportCityName());
//   }

//   @Test
//   void test_findByIataId_withInvalidAirport_thenStatus404() throws Exception {
//     String invalidIataId = "ZZZ";
//     String expectedErrorResponse =
//       "No airport with IATA code: " + invalidIataId + " exists";
//     when(service.findByIataId(invalidIataId))
//       .thenThrow(new AirportNotFoundException(expectedErrorResponse));

//     MvcResult response = mvc
//       .perform(
//         get(SERVICE_PATH_AIRPORTS + "/" + invalidIataId)
//           .header("Accept", "application/json")
//       )
//       .andExpect(status().is(404))
//       .andReturn();

//     assertEquals(
//       expectedErrorResponse,
//       new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), HttpError.class)
//         .getError()
//     );
//   }

//   @Test
//   void test_findByIataId_withBadParams_thenStatus400() throws Exception {
//     String invalidIataId = "123";
//     when(service.findByIataId(invalidIataId))
//       .thenThrow(new IllegalArgumentException());

//     mvc
//       .perform(
//         get(SERVICE_PATH_AIRPORTS + "/" + invalidIataId)
//           .header("Accept", "application/json")
//       )
//       .andExpect(status().is(400))
//       .andReturn();
//   }

//   // @Test
//   // void test_findByCity_withValidAirports_singleResult_thenStatus200()
//   //   throws Exception {
//   //   String testCitySearch = "wash"; // Should only return DCA
//   //   Map<String, String> filterMap = new HashMap<String, String>();
//   //   filterMap.put("searchTerms", testCitySearch);
//   //   when(service.findBySearchAndFilter(filterMap))
//   //     .thenReturn(
//   //       testAirportList
//   //         .stream()
//   //         .filter(
//   //           airport ->
//   //           (airport.getAirportIataId() +
//   //           airport.getAirportCityName())
//   //           .toLowerCase(Locale.getDefault())
//   //           .contains(testCitySearch)
//   //         )
//   //         .collect(Collectors.toList())
//   //     );

//   //   MvcResult response = mvc
//   //     .perform(
//   //       post(SERVICE_PATH_AIRPORTS + "/search")
//   //         .header("Accept", "application/json")
//   //         .content(new ObjectMapper().writeValueAsString(filterMap))
//   //     )
//   //     .andExpect(status().is(200))
//   //     .andReturn();

//   //   List<Airport> actual = Arrays
//   //     .stream(
//   //       new ObjectMapper()
//   //       .readValue(response.getResponse().getContentAsString(), Airport[].class)
//   //     )
//   //     .collect(Collectors.toList());

//   //   assertEquals(1, actual.size());
//   //   assertEquals("DCA", actual.get(0).getAirportIataId());
//   //   assertEquals("Washington DC", actual.get(0).getAirportCityName());
//   // }

//   // @Test
//   // void test_findByCity_withValidAirports_multiResult_thenStatus200()
//   //   throws Exception {
//   //   String testCitySearch = "york"; // Should return both JFK & LGA
//   //   Map<String, String> filterMap = new HashMap<String, String>();
//   //   filterMap.put("searchTerms", testCitySearch);
//   //   when(service.findBySearchAndFilter(filterMap))
//   //     .thenReturn(
//   //       testAirportList
//   //         .stream()
//   //         .filter(
//   //           airport ->
//   //             airport
//   //               .getAirportCityName()
//   //               .toLowerCase()
//   //               .contains(testCitySearch)
//   //         )
//   //         .collect(Collectors.toList())
//   //     );

//   //   MvcResult response = mvc
//   //     .perform(
//   //       get(SERVICE_PATH_AIRPORTS + "/search")
//   //         .header("Accept", "application/json")
//   //         .content(filterMap.toString())
//   //     )
//   //     .andExpect(status().is(200))
//   //     .andReturn();

//   //   List<Airport> actual = Arrays
//   //     .stream(
//   //       new ObjectMapper()
//   //       .readValue(response.getResponse().getContentAsString(), Airport[].class)
//   //     )
//   //     .collect(Collectors.toList());

//   //   assertEquals(2, actual.size());
//   //   assertTrue(
//   //     actual.get(0).getAirportIataId().equals("JFK") ||
//   //     actual.get(1).getAirportIataId().equals("LGA")
//   //   );
//   //   assertTrue(
//   //     actual.get(0).getAirportIataId().equals("JFK") ||
//   //     actual.get(1).getAirportIataId().equals("LGA")
//   //   );
//   // }

//   // @Test
//   // void test_findByCity_withNoValidAirports_thenStatus204() throws Exception {
//   //   String testCitySearch = "dal"; // Should return no content as Dallas is not in the list
//   //   Map<String, String> filterMap = new HashMap<String, String>();
//   //   filterMap.put("searchTerms", testCitySearch);
//   //   when(service.findBySearchAndFilter(filterMap))
//   //     .thenReturn(
//   //       testAirportList
//   //         .stream()
//   //         .filter(
//   //           airport ->
//   //             airport
//   //               .getAirportCityName()
//   //               .toLowerCase()
//   //               .contains(testCitySearch)
//   //         )
//   //         .collect(Collectors.toList())
//   //     );

//   //   mvc
//   //     .perform(
//   //       get(SERVICE_PATH_AIRPORTS + "/search")
//   //         .header("Accept", "application/json")
//   //         .content(filterMap.toString())
//   //     )
//   //     .andExpect(status().is(204))
//   //     .andReturn();
//   // }

//   @Test
//   void test_insert_withValidAirport_thenStatus201() throws Exception {
//     when(
//       service.insert(
//         testAirport.getAirportIataId(),
//         testAirport.getAirportCityName()
//       )
//     )
//       .thenReturn(testAirport);

//     System.out.print(new ObjectMapper().writeValueAsString(testAirport));

//     MvcResult response = mvc
//       .perform(
//         post(SERVICE_PATH_AIRPORTS)
//           .header("Accept", "application/json")
//           .content(new ObjectMapper().writeValueAsString(testAirport))
//       )
//       .andExpect(status().is(201))
//       .andReturn();

//     Airport actual = new ObjectMapper()
//     .readValue(response.getResponse().getContentAsString(), Airport.class);

//     assertEquals(testAirport.getAirportIataId(), actual.getAirportIataId());
//     assertEquals(testAirport.getAirportCityName(), actual.getAirportCityName());
//   }

//   @Test
//   void test_insert_withDuplicateAirport_thenStatus409() throws Exception {
//     when(
//       service.insert(
//         testAirport.getAirportIataId(),
//         testAirport.getAirportCityName()
//       )
//     )
//       .thenThrow(new AirportAlreadyExistsException("duplicate"));

//     mvc
//       .perform(
//         post(SERVICE_PATH_AIRPORTS)
//           .header("Accept", "application/json")
//           .content(new ObjectMapper().writeValueAsString(testAirport))
//       )
//       .andExpect(status().is(409))
//       .andReturn();
//   }

//   @Test
//   void test_insert_withInvalidAirport_thenStatus400() throws Exception {
//     String invalidIataId = "ThisIsTooLong";
//     when(service.insert(invalidIataId, testAirport.getAirportCityName()))
//       .thenThrow(new IllegalArgumentException());

//     MvcResult response = mvc
//       .perform(
//         post(SERVICE_PATH_AIRPORTS)
//           .header("Accept", "application/json")
//           .content(
//             new ObjectMapper()
//             .writeValueAsString(
//                 new Airport(invalidIataId, testAirport.getAirportCityName())
//               )
//           )
//       )
//       .andExpect(status().is(400))
//       .andReturn();

//     String expectedErrorResponse =
//       "The IATA Code: " +
//       invalidIataId +
//       "is invalid (only uppercase English letter are allowed)";
//     assertEquals(
//       expectedErrorResponse,
//       new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), HttpError.class)
//         .getError()
//     );
//   }

//   @Test
//   void test_insert_withBadParams_thenStatus400() throws Exception {
//     MvcResult response = mvc
//       .perform(
//         post(SERVICE_PATH_AIRPORTS)
//           .header("Accept", "application/json")
//           .content("")
//       )
//       .andExpect(status().is(400))
//       .andReturn();

//     String expectedErrorResponse =
//       "The IATA Code: " +
//       "" +
//       "is invalid (only uppercase English letter are allowed)";
//     assertEquals(
//       expectedErrorResponse,
//       new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), HttpError.class)
//         .getError()
//     );
//   }

//   @Test
//   void test_update_withValidAirport_thenStatus202() throws Exception {
//     String newCityName = "newcityname"; // only Airport names can be updated, as other data relies on the unique iataId
//     when(service.update(testAirport.getAirportIataId(), newCityName))
//       .thenReturn(new Airport(testAirport.getAirportIataId(), newCityName));

//     MvcResult response = mvc
//       .perform(
//         put(SERVICE_PATH_AIRPORTS + "/" + testAirport.getAirportIataId())
//           .header("Accept", "application/json")
//           .content(newCityName)
//       )
//       .andExpect(status().is(202))
//       .andReturn();

//     Airport actual = new ObjectMapper()
//     .readValue(response.getResponse().getContentAsString(), Airport.class);

//     assertEquals(testAirport.getAirportIataId(), actual.getAirportIataId());
//     assertEquals(newCityName, actual.getAirportCityName());
//   }

//   @Test
//   void test_update_withInvalidAirport_thenStatus404() throws Exception {
//     String invalidIataId = "NOT"; // as the IATA Code "NOT" is not present in the testAirportsList
//     when(service.update(invalidIataId, testAirport.getAirportCityName()))
//       .thenThrow(new AirportNotFoundException());

//     MvcResult response = mvc
//       .perform(
//         put(SERVICE_PATH_AIRPORTS + "/" + invalidIataId)
//           .header("Accept", "application/json")
//           .content(testAirport.getAirportCityName())
//       )
//       .andExpect(status().is(404))
//       .andReturn();

//     String expectedErrorResponse =
//       "The IATA Code: " +
//       invalidIataId +
//       "is invalid (only uppercase English letter are allowed)";
//     assertEquals(
//       expectedErrorResponse,
//       new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), HttpError.class)
//         .getError()
//     );
//   }

//   @Test
//   void test_update_withInvalidInput_thenStatus400() throws Exception {
//     String invalidNewCityName = ""; // as names cannot be empty

//     MvcResult response = mvc
//       .perform(
//         put(SERVICE_PATH_AIRPORTS + "/" + testAirport.getAirportIataId())
//           .header("Accept", "application/json")
//           .content(invalidNewCityName)
//       )
//       .andExpect(status().is(400))
//       .andReturn();

//     String expectedErrorResponse =
//       "The city name: " +
//       invalidNewCityName +
//       " is invalid (only alphanumeric characters are allowed).";
//     assertEquals(
//       expectedErrorResponse,
//       new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), HttpError.class)
//         .getError()
//     );
//   }

//   @Test
//   void test_update_withBadParams_thenStatus400() throws Exception {
//     MvcResult response = mvc
//       .perform(
//         post(SERVICE_PATH_AIRPORTS)
//           .header("Accept", "application/json")
//           .content("")
//       )
//       .andExpect(status().is(400))
//       .andReturn();

//     String expectedErrorResponse =
//       "The IATA Code: " +
//       "" +
//       "is invalid (only uppercase English letter are allowed)";
//     assertEquals(
//       expectedErrorResponse,
//       new ObjectMapper()
//         .readValue(response.getResponse().getContentAsString(), HttpError.class)
//         .getError()
//     );
//   }

//   @Test
//   void test_delete_withValidAirport_thenStatus204() throws Exception {
//     mvc
//       .perform(
//         delete(SERVICE_PATH_AIRPORTS + "/" + testAirport.getAirportIataId())
//           .header("Accept", "application/json")
//       )
//       .andExpect(status().is(204))
//       .andReturn();
//   }
// }
