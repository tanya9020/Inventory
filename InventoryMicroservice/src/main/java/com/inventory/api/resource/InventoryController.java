package com.inventory.api.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.api.dto.ChangeLocationInventoryAttributes;
import com.inventory.api.dto.LocationInventoryCreate;
import com.inventory.api.dto.LocationInventoryUpdate;
import com.inventory.api.entity.Item;
import com.inventory.api.entity.Location;
import com.inventory.api.entity.LocationInventory;
import com.inventory.api.exceptions.LocationOrItemNotFoundException;
import com.inventory.api.exceptions.ResourceNotFoundException;
import com.inventory.api.exceptions.illegalArgument;
import com.inventory.api.responsetemplate.GenericResponse;
import com.inventory.api.service.InventoryServiceImpl;


@RestController
public class InventoryController {

	
	@Autowired private InventoryServiceImpl invRepo;
	

	
	
	@PostMapping("/addInventory")
	public String saveInventory(@Valid @RequestBody LocationInventoryCreate locInv) {
		
		
		String str = invRepo.saveInventory(locInv);
		
		return str;
		 
	}
	
	
	
	@Transactional
	@PutMapping("/ubyId/{id}")
	public ResponseEntity<?> updateInventoryDec1(@PathVariable int id,@RequestBody LocationInventoryUpdate inv) throws OptimisticLockingFailureException, illegalArgument, ResourceNotFoundException, LocationOrItemNotFoundException {
		LocationInventory l=invRepo.updateInventorybyId(id, inv);
		return new ResponseEntity<>(new GenericResponse(HttpStatus.OK, l), HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping("/changebyId/{id}")
	public ResponseEntity<?> updateInventoryDec12(@PathVariable int id,@RequestBody ChangeLocationInventoryAttributes inv) throws OptimisticLockingFailureException, illegalArgument, ResourceNotFoundException, LocationOrItemNotFoundException {
		LocationInventory l=invRepo.changebyId(id, inv);
		return new ResponseEntity<>(new GenericResponse(HttpStatus.OK, l), HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping("/updateInventory")
	public ResponseEntity<?> iDec1(@RequestBody LocationInventoryUpdate inv) throws OptimisticLockingFailureException, illegalArgument, ResourceNotFoundException, LocationOrItemNotFoundException {
		LocationInventory l=invRepo.idec2(inv);
		return new ResponseEntity<>(new GenericResponse(HttpStatus.OK, l), HttpStatus.OK);
	}
	@Transactional
	@PutMapping("/changeInventoryAttributes")
	public ResponseEntity<?> changeLoc(@RequestBody ChangeLocationInventoryAttributes inv) throws OptimisticLockingFailureException, illegalArgument, ResourceNotFoundException, LocationOrItemNotFoundException {
		LocationInventory l=invRepo.changeLoc(inv);
		return new ResponseEntity<>(new GenericResponse(HttpStatus.OK, l), HttpStatus.OK);
	}

	@GetMapping("/findAllItems")
	public List<Item> getBook() {
		return invRepo.findAll();
	}
	
	
	@GetMapping("/getLocationInventory/{id}")
	public ResponseEntity<GenericResponse> getLocInv(@PathVariable int id) throws illegalArgument, ResourceNotFoundException, LocationOrItemNotFoundException {
		
		LocationInventory l=invRepo.getAllInvbyId(id);
		return /*invRepo.getAllInvbyId(id);*/new ResponseEntity<>(new GenericResponse(HttpStatus.FOUND, l), HttpStatus.FOUND);
	}
	
	/*
	 * @GetMapping("/item") public ResponseEntity<GenericResponse> getAllItems() {
	 * List<ItemDTO> items = catalogService.getAllItems(); return new
	 * ResponseEntity<>(new GenericResponse(HttpStatus.OK, items), HttpStatus.OK); }
	 */
	
	
//	@Cacheable(value="LocInv" ,key ="#id")
//	@GetMapping("/getLocationInventory/{id}")
//	public Optional<LocationInventory> getLocInv(@PathVariable int id) {
//		return invRepo.getAllInvbyId(id);
//		
//	}
	
	@PostMapping("/addItem")
	public String saveItem(@RequestBody Item it) {
		invRepo.saveItem(it);
		return "Added  with id : " + it.getItemName();
	}
	
	@GetMapping("/findLoc")
	public List<Location> getloc() {
		return invRepo.findAllLoc();
	}
	
	@PostMapping("/addLocation")
	public String saveLoc(@RequestBody Location it) {
		invRepo.saveLoc(it);
		return "Added  with id : " + it.getId();
	}
	
	
}
