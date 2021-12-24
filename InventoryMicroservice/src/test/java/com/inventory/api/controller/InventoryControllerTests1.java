package com.inventory.api.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.api.DemoApplication;
import com.inventory.api.dto.ChangeLocationInventoryAttributes;
import com.inventory.api.dto.LocationInventoryUpdate;
import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.ItemAttributes;
import com.inventory.api.entity.ItemTagAttributes;
import com.inventory.api.entity.Items;
import com.inventory.api.entity.LocationAttributes;
import com.inventory.api.entity.LocationInventory;
import com.inventory.api.entity.ToInventory;
import com.inventory.api.resource.InventoryController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.api.dto.LocationInventoryUpdate;
import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.ItemAttributes;
import com.inventory.api.entity.ItemTagAttributes;
import com.inventory.api.entity.Items;
import com.inventory.api.entity.Location;
import com.inventory.api.entity.LocationAttributes;
import com.inventory.api.entity.LocationInventory;
import com.inventory.api.repository.LocationRepository;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers; 


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;



@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
@SpringBootTest(classes = DemoApplication.class)
public class InventoryControllerTests1 extends AbstractTestNGSpringContextTests {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@BeforeClass
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		  LocationInventoryUpdate l=new LocationInventoryUpdate(2, new
		  Inventory("BAD",0.0,null,new ItemAttributes(new
		  Items("NOR","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))), new
		  LocationAttributes("loc2","Z2","XYZ","DC2"), "ERROR", 1); LocationInventory
		  lI=new LocationInventory(2,new Inventory("BAD",0.0,null,new
		  ItemAttributes(new Items("NOR","NORMAL","FQ","EACH"),new
		  ItemTagAttributes(null,null))), new
		  LocationAttributes("loc2","Z2","XYZ","DC2"), 0);
		 
	}

	@Test
	public void testgetInventorybyId() throws Exception {
		mockMvc.perform(get("/getLocationInventory/2")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.inventory.inventoryStatus").value("BAD"));

	}
	//@Test
	@Test /* ( enabled=false ) */
	public void testUpdateInventoryWithoutId() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/updateInventory")
			      .content(asJsonString(new LocationInventoryUpdate(0, new Inventory("GOOD",0.0,null,new ItemAttributes(new Items("NOR","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))),
			    		  new LocationAttributes("loc2","Z2","XYZ","DC2"), "ERROR", 1)))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}
	
	@Test 
	public void testUpdateInventoryWithoutIdNegative() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/updateInventory")
			      .content(asJsonString(new LocationInventoryUpdate(0, new Inventory("BAD",0.0,null,new ItemAttributes(new Items("NOR","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))),
			    		  new LocationAttributes("loc30","Z30","XYZ","DC30"), "ERROR", 1)))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isBadRequest());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}
	
	
	@Test 
	public void testUpdateInventoryWithIdNegative() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/updateInventory")
			      .content(asJsonString(new LocationInventoryUpdate(20, new Inventory("BAD",0.0,null,new ItemAttributes(new Items(null,null,null,null),new ItemTagAttributes(null,null))),
			    		  new LocationAttributes(null,null,null,null), "ERROR", 1)))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isInternalServerError());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}
	
	
	
	@Test /* ( enabled=false ) */
	public void testUpdateInventoryWithId() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/updateInventory")
			      .content(asJsonString(new LocationInventoryUpdate(2, new Inventory("BAD",0.0,null,new ItemAttributes(new Items(null,null,null,null),new ItemTagAttributes(null,null))),
			    		  new LocationAttributes(null,null,null,null), "ERROR", 1)))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}
	
	
	@Test /* ( enabled=false ) */
	public void testChangeLocAttrbyId() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/changeInventoryAttributes")
			      .content(asJsonString2(new ChangeLocationInventoryAttributes(2, new Inventory("BAD",0.0,null,new ItemAttributes(new Items("NOR","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))),
			  			new ToInventory("GOOD","FQ","",""),new LocationAttributes("loc2","Z2","XYZ","DC2"), "ERROR")))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}
	


	@Test /* ( enabled=false ) */
	public void testChangeLocAttrbyIdNegative() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/changeInventoryAttributes")
			      .content(asJsonString2(new ChangeLocationInventoryAttributes(21, new Inventory("BAD",0.0,null,new ItemAttributes(new Items("NOR","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))),
				  			new ToInventory("GOOD","FQ",null,""),new LocationAttributes("loc2","Z2","XYZ","DC2"), "ERROR")))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isInternalServerError());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}

	@Test /* ( enabled=false ) */
	public void testChangeLocAttr() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/changeInventoryAttributes")
			      .content(asJsonString2(new ChangeLocationInventoryAttributes(0, new Inventory("GOOD",0.0,null,new ItemAttributes(new Items("NOR","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))),
				  			new ToInventory("GOOD","FQ","",""),new LocationAttributes("loc2","Z2","XYZ","DC2"), "ERROR")))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}

	@Test /* ( enabled=false ) */
	public void testChangeLocAttrNegative() throws Exception {
		
				
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/changeInventoryAttributes")
			      .content(asJsonString2(new ChangeLocationInventoryAttributes(0, new Inventory("BAD",0.0,null,new ItemAttributes(new Items("NOR1","NORMAL","FQ","EACH"),new ItemTagAttributes(null,null))),
				  			new ToInventory("GOOD","FQ","",""),new LocationAttributes("loc2","Z2","XYZ","DC2"), "ERROR")))
			      .contentType(MediaType.APPLICATION_JSON))
			      //.accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isBadRequest());
			      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
			}
	
	
	
	private String asJsonString2(ChangeLocationInventoryAttributes l) {
		// TODO Auto-generated method stub
		 try {
		        final ObjectMapper mapper = new ObjectMapper();
		         String jsonContent = mapper.writeValueAsString(l);
		        //jsonContent+=mapper.writeValueAsString(locationAttributes);
		        System.out.print(jsonContent);
		        return jsonContent;
		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }
	}
	

	private String asJsonString(LocationInventoryUpdate l) {
		// TODO Auto-generated method stub
		 try {
		        final ObjectMapper mapper = new ObjectMapper();
		         String jsonContent = mapper.writeValueAsString(l);
		        //jsonContent+=mapper.writeValueAsString(locationAttributes);
		        System.out.print(jsonContent);
		        return jsonContent;
		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }
	}

}
