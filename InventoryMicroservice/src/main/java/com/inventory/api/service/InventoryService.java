package com.inventory.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;

import com.inventory.api.dto.ChangeLocationInventoryAttributes;
import com.inventory.api.dto.LocationInventoryCreate;
import com.inventory.api.dto.LocationInventoryUpdate;
import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.Item;
import com.inventory.api.entity.Items;
import com.inventory.api.entity.Location;
import com.inventory.api.entity.LocationAttributes;
import com.inventory.api.entity.LocationInventory;
import com.inventory.api.exceptions.LocationOrItemNotFoundException;
import com.inventory.api.exceptions.ResourceNotFoundException;
import com.inventory.api.exceptions.illegalArgument;
import com.inventory.api.responsetemplate.GenericResponse;



@EnableCaching
public interface InventoryService {

	
	public int getSequenceNumber(String sequenceName);
	
	public String saveInventory(LocationInventoryCreate inv) ;
	
	 public LocationInventory saveInventory_dup(LocationInventoryCreate inv,int id1);
	 
	 public Item getItemName(String s);
	 
	 public boolean validItem(Items i);
	 
	 public boolean validLoc(LocationAttributes l);
	 
	 public Location getLocationName(String s);
	 
	 @Cacheable(value="Loc" ,key ="1")
	 public boolean ValidLoc(String s);
	 
	 @Cacheable(value="idcache" ,key ="#id")
	 public LocationInventory getInvbyId(int id);
	 public List<LocationInventory> getInv(String s);
	 
	// public Optional<LocationInventory> getAllInvbyId(int id);
	 @CachePut(value="LocInv1" ,key ="#id")
	 public LocationInventory getAllInvbyId(int id) throws ResourceNotFoundException, LocationOrItemNotFoundException, illegalArgument ;
	 
	 public boolean getInv22(LocationAttributes locationId, Items Inv/* ,String i */, String invSt);
	 
	 @Cacheable(value="Loc" ,key ="'i'+'a'")	
	 public List<LocationInventory> getInv221(LocationAttributes locationId, Items Inv/* ,String i */, String i);
		
		public List<LocationInventory> getLocInv(LocationAttributes location, Inventory Inv/* ,String i */) ;
		
		
		
		public boolean exitsLoc(String loc);
		
		public boolean exitsInv(String Inv);
		
		
		
		public boolean InvalidResonCode(String Inv);
		
		public boolean InvalidInvSt(String Inv);
		
		public List<Item> findAll();
		
		public List<Location> findAllLoc();
		
		public String saveItem(Item it);
		
		public String saveLoc(Location l);
		
		public LocationInventory idec2(LocationInventoryUpdate inv)
				throws ResourceNotFoundException, LocationOrItemNotFoundException, illegalArgument ;
		
		public LocationInventory NormalBatchItem(List<LocationInventory> locInv, LocationInventoryUpdate inv, int flag) ;
		
		public LocationInventory serialItem(List<LocationInventory> locInv, LocationInventoryUpdate inv) ;
		
		public LocationInventory changeLoc(ChangeLocationInventoryAttributes inv) throws illegalArgument;
		
		





	
}
