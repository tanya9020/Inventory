package com.inventory.api.repository;

import static org.testng.Assert.assertEquals;

/*import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;*/

import java.util.List;

/*import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;*/
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.inventory.api.entity.Location;
import com.inventory.api.entity.LocationInventory;
import com.inventory.api.service.InventoryServiceImpl;



	@DataMongoTest
	public class LocationRepositoryTest {
		//@Autowired
		private InventoryServiceImpl repo;
		
		//@MockBean
		@Autowired
		private LocationRepository locRepo;

	//@Autowired
	//private MongoTemplate temp;


		
		@BeforeMethod
		 void setUp() throws Exception {
			Location loc = new Location(1, "loc1", "XYZ", "DC1", "Z1");
			locRepo.save(loc);
		}

		
		@AfterMethod
		 void teardown() throws Exception {
			locRepo.deleteAll();
		}

		
	
	/*
	 * @org.junit.jupiter.api.Test public void whenFindById() { Location loc =
	 * locRepo.findlocByName("loc1"); assertEquals(1, loc.getId()); }
	 * 
	 * 
	 * @org.junit.jupiter.api.Test public void whenGetLocationByLocationID() {
	 * Location loc = locRepo.findlocByName("loc1");
	 * assertEquals(loc.getLocationId(), "loc1"); }
	 */
	/*
	 * @Test public void whenGetassert() { ///Location loc =
	 * locRepo.findlocByName("loc1"); assertNotNull( "loc1"); }
	 */
		
		
		
		@Test
		void whenFindById() {
			Location loc = locRepo.findlocByName("loc1");
			assertEquals(1, loc.getId());
		}


		@Test
		void whenGetLocationByLocationID() {
			Location loc = locRepo.findlocByName("loc1");
			assertEquals(loc.getLocationId(), "loc1");
		}
		
		@org.junit.jupiter.api.Test
		void whenFindAllLocation() {
			Location loc = new Location(2, "loc2", "XYZ", "DC2", "Z2");
			locRepo.save(loc);
			List<Location> list = locRepo.findAll();
			assertEquals(list.size(), 2);
		}
		
		
		
		
	/*
	 * @Test void whenFindAllLocation() { Location loc = new Location(2, "loc2",
	 * "XYZ", "DC2", "Z2"); locRepo.save(loc); List<Location> list =
	 * locRepo.findAll(); assertEquals(list.size(), 2); }
	 */
		
//		 @Test
//		    void whenLocationExistsByLocationID() {
//			assertTrue(locRepo.locationExistsByLocationID("loc1"));
//		    }
		
//		@Test
//	    void whenValidLoc() {
//			//LocationAttributes locAtt1 = new LocationAttributes("loc2", "XYZ", "DC2", "Z2");
//			//LocationAttributes locAtt1 = new LocationAttributes();
//			//locRepo.saveAll(locAtt1);
//			Location loc1 = new Location(2, "loc2", "XYZ", "DC2", "Z2");
//			locRepo.save(loc1);
	//
//			//Location loc1 = getLocationName(inv.getLocation().getLocationId());
//			LocationAttributes loc2 = new LocationAttributes(loc1.getLocationId(), loc1.getZone(),
//					loc1.getEnterpriseCode(), loc1.getNode());
	//
	//
//		assertTrue(repo.validLoc(loc2));
//	    }
		
		
	/*
		 @Test
	    void whenItemExistsByItemID() {
		assertTrue(repo.itemExistsByItemID("Test1"));
	    }

	    @Test
	    void whenNotItemExistsByItemID() {
		assertFalse(repo.itemExistsByItemID("Random"));
	    }
		
		

		@Test
		@DisplayName("Test for update check update with version")
		void whenUpdateItem() {

			Item old_item = locRepo.findById("1").get();
			String old_item_id = old_item.getId();
			assertEquals(old_item_id, "Test1");
			assertEquals(old_item.getVersion(), 0L);
			old_item.setItemID("Test2");
			Item new_item = locRepo.save(old_item);
			assertEquals(new_item.getId(), "Test2");
			assertEquals(new_item.getVersion(), 1L);
		}

		@Test
		@DisplayName("Verify select for update")
		void whengetItem2() {
			Item item1 = locRepo.findById("1").get();
			Item item2 = locRepo.getItem2("1");
			assertEquals(item1.getVersion() + 1L, item2.getVersion());
		}
	*/
	}