package com.inventory.api.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.api.dto.ChangeLocationInventoryAttributes;
import com.inventory.api.dto.LocationInventoryCreate;
import com.inventory.api.dto.LocationInventoryUpdate;
import com.inventory.api.entity.DbSeq;
import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.Item;
import com.inventory.api.entity.ItemTagAttributes;
import com.inventory.api.entity.Items;
import com.inventory.api.entity.Location;
import com.inventory.api.entity.LocationAttributes;
import com.inventory.api.entity.LocationInventory;
import com.inventory.api.entity.ToInventory;
import com.inventory.api.exceptions.LocationOrItemNotFoundException;
import com.inventory.api.exceptions.ResourceNotFoundException;
import com.inventory.api.exceptions.illegalArgument;
import com.inventory.api.repository.InventoryRepository;
import com.inventory.api.repository.ItemRepository;
import com.inventory.api.repository.LocationRepository;
import com.inventory.api.responsetemplate.GenericResponse;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private MongoTemplate mongotemplate;

	private InventoryRepository dao;

	@Autowired
	public void setInventoryRepository(InventoryRepository dao) {
		this.dao = dao;
	}

	private ItemRepository itemRepo;

	@Autowired
	public void setItemRepository(ItemRepository itemRepo) {
		this.itemRepo = itemRepo;
	}

	private LocationRepository locRepo;

	@Autowired
	public void setLocationRepository(LocationRepository locRepo) {
		this.locRepo = locRepo;
	}

	@Autowired
	private MongoOperations mongoOperations;

	public int getSequenceNumber(String sequenceName) {

		Query query = new Query(Criteria.where("id").is(sequenceName));

		Update update = new Update().inc("seq", 1);

		DbSeq counter = mongoOperations.findAndModify(query, update, (options()).returnNew(true).upsert(true),
				DbSeq.class);

		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

	public String saveInventory(LocationInventoryCreate inv) {

		try {

			Item item1 = getItemName(inv.getInventory().getItemAttributes().getItems().getItemName());

			Items item2 = new Items(item1.getItemName(), item1.getItemDesc(), item1.getProductClass(),
					item1.getUnitOfMeasure());

			Location loc1 = getLocationName(inv.getLocation().getLocationId());
			LocationAttributes loc2 = new LocationAttributes(loc1.getLocationId(), loc1.getZone(),
					loc1.getEnterpriseCode(), loc1.getNode());

			if (inv.getInventory().getItemAttributes().getItems().equals(item2) && inv.getLocation().equals(loc2))// &&resCo.getReasonCode().equals(inv.getReasonCode()))
			{
				int id1 = getSequenceNumber(LocationInventory.SEQUENCE_NAME);

				LocationInventory l = new LocationInventory();
				l.setId(id1);
				l.setInventory(inv.getInventory());
				l.setLocation(inv.getLocation());
				dao.save(l);
				return "Added with id:" + l.getId();
			} else {

				return "Invalid Location Inventory attributes ";
			}
		} catch (Exception e) {
			return "Invalid  Location Inventory attributes ";
		}
	}

	public LocationInventory saveInventory_dup(LocationInventoryCreate inv, int id1) {

		LocationInventory l = new LocationInventory();
		l.setId(id1);
		l.setInventory(inv.getInventory());
		l.setLocation(inv.getLocation());
		dao.save(l);
		return l;

	}

	public Item getItemName(String s) {
		Item i = itemRepo.findItemByName(s);
		return i;
	}

	public boolean validItem(Items i) {

		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("itemName").is(i.getItemName()),
				Criteria.where("itemDesc").is(i.getItemDesc()), Criteria.where("productClass").is(i.getProductClass()),
				Criteria.where("unitOfMeasure").is(i.getUnitOfMeasure())));

		List<Item> Inventories = mongotemplate.find(query, Item.class);
		if (Inventories.isEmpty())
			return false;
		return true;

	}

	public boolean validLoc(LocationAttributes l) {

		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("locationId").is(l.getLocationId()),
				Criteria.where("enterpriseCode").is(l.getEnterpriseCode()), Criteria.where("node").is(l.getNode()),
				Criteria.where("zone").is(l.getZone())));

		List<Location> Inventories = mongotemplate.find(query, Location.class);
		if (Inventories.isEmpty())
			return false;
		return true;

	}

	public Location getLocationName(String s) {
		Location i = locRepo.findlocByName(s);
		return i;
	}

	public boolean ValidLoc(String s) {
		Location i = locRepo.findlocByName(s);
		if (i.equals(null))
			return false;
		return true;
	}

	public List<LocationInventory> getInv(String s) {

		List<LocationInventory> i = dao.findInv(s);

		return i;

	}

	public LocationInventory getAllInvbyId(int id) throws illegalArgument {
		// System.out.print("Fetchinh from db");
		Optional<LocationInventory> Inventories = dao.findById(id);
		if (Inventories.isEmpty()) 
			throw new illegalArgument("Invalid ID");
		
		return Inventories.get();
	}


	 
	public /* Optional< */LocationInventory getInvbyId(int id) {
		// System.out.print("Fetchinh from db");
		Optional<LocationInventory> Inventories = dao.findById(id);
		return Inventories.get();
	}

	public boolean getInv22(LocationAttributes locationId, Items Inv/* ,String i */, String invSt) {

		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("location").is(locationId),
				Criteria.where("inventory.itemAttributes.items").is(Inv),
				Criteria.where("inventory.inventoryStatus").is(invSt)));

		List<LocationInventory> Inventories = mongotemplate.find(query, LocationInventory.class);
		if (Inventories.isEmpty())
			return false;
		return true;

	}

	public List<LocationInventory> getInv221(LocationAttributes locationId, Items Inv/* ,String i */, String i) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("location").is(locationId),
				Criteria.where("inventory.itemAttributes.items").is(Inv),
				Criteria.where("inventory.inventoryStatus").is(i)));
		// System.out.print("inv221" + query);

		List<LocationInventory> Inventories = mongotemplate.find(query, LocationInventory.class);

		// System.out.println("query called"+Inventories );
		return Inventories;
	}

	public List<LocationInventory> getLocInv(LocationAttributes location, Inventory Inv/* ,String i */) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("location").is(location),
				Criteria.where("inventory").is(Inv)));

		List<LocationInventory> Inventories = mongotemplate.find(query, LocationInventory.class);

		return Inventories;
	}

	

	public boolean exitsLoc(String loc) {

		Query query = new Query(Criteria.where("location.locationId").is(loc));

		if (mongotemplate.find(query, LocationInventory.class).isEmpty())
			return false;
		return true;

	}

	public boolean exitsInv(String Inv) {

		Query query = new Query(Criteria.where("inventory.itemAttributes.items.itemName").is(Inv));

		if (mongotemplate.find(query, LocationInventory.class).isEmpty())
			return false;
		return true;

	}

	private boolean validToInv(/* Inventory toInv, */ToInventory toInv) {

		if (!InvalidInvSt(toInv.getInventoryStatus())) {

			if (toInv.getProductClass().equals("FQ") || toInv.getProductClass().equals("FD")) {
				return true;
			}
		}
		return false;
	}

	public boolean InvalidResonCode(String Inv) {

		List<String> c = new ArrayList<String>();
		c.add("RECEIPT");
		c.add("OTHERS");
		c.add("QC");
		c.add("ERROR");
		c.add("INBOUND");
		c.add("OUTBOUND");
		if (c.contains(Inv))
			return false;
		return true;

	}

	public boolean InvalidInvSt(String Inv) {

		List<String> c = new ArrayList<String>();
		c.add("GOOD");
		c.add("AI");
		c.add("QC");
		c.add("BAD");
		if (c.contains(Inv))
			return false;
		return true;

	}

	public List<Item> findAll() {

		return itemRepo.findAll();
	}

	public List<Location> findAllLoc() {

		return locRepo.findAll();
	}

	public String saveItem(Item it) {

		itemRepo.save(it);
		return "added" + it.getItemName();

	}

	public String saveLoc(Location l) {

		locRepo.save(l);
		return "added" + l.getLocationId();

	}

	public /* ResponseEntity<GenericResponse> */ LocationInventory updateInventorybyId(int id,
			LocationInventoryUpdate inv)
			throws ResourceNotFoundException, LocationOrItemNotFoundException, illegalArgument {
		LocationInventory list22 = getAllInvbyId(id);
		String desc = list22.getInventory().getItemAttributes().getItems().getItemDesc();

		int flag = 0;
		if (desc.equals("NORMAL") || desc.equals("BATCH")) {
			if (desc.equals("BATCH"))
				flag = 1;

			// LocationInventory lc1 = NormalBatchItem(list2, inv, flag);
			list22.getInventory().setQuantity(inv.getQuantityChange() + list22.getInventory().getQuantity());

			LocationInventory lc_dup = list22;

			dao.save(lc_dup);

			/*
			 * if (lc1 == null) return new ResponseEntity<GenericResponse>(new
			 * GenericResponse(HttpStatus.BAD_REQUEST, "Invalid update"),
			 * HttpStatus.BAD_REQUEST);
			 */

			/*
			 * return new ResponseEntity<GenericResponse>(new
			 * GenericResponse(HttpStatus.ACCEPTED, lc_dup), HttpStatus.ACCEPTED);
			 */
			return lc_dup;

		} else if (desc.equals("SERIAL")) {

			list22.getInventory().setQuantity(list22.getInventory().getQuantity()
					+ inv.getInventory().getItemAttributes().getItemTags().getSerialList().size());
			List<String> sl = list22.getInventory().getItemAttributes().getItemTags().getSerialList();

			sl.addAll(inv.getInventory().getItemAttributes().getItemTags().getSerialList());

			list22.getInventory().getItemAttributes().getItemTags().setSerialList(sl);
			LocationInventory lc_dup = list22;
			dao.save(lc_dup);
			/*
			 * return new ResponseEntity<GenericResponse>(new
			 * GenericResponse(HttpStatus.ACCEPTED, lc_dup), HttpStatus.ACCEPTED);
			 */
			return lc_dup;
		}

		else {

			/*
			 * return new ResponseEntity<GenericResponse>( new
			 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Update"),
			 * HttpStatus.INTERNAL_SERVER_ERROR);
			 */
			throw new illegalArgument("Invalid id");

		}

	}

	@Transactional(readOnly = false)
	@Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 5, backoff = @Backoff(3000))
	public /* ResponseEntity<GenericResponse> */ LocationInventory idec2(LocationInventoryUpdate inv)
			throws ResourceNotFoundException, LocationOrItemNotFoundException, illegalArgument {

		/*
		 * if (inv.getId() != 0) {
		 * 
		 * LocationInventory list22 = getAllInvbyId(inv.getId());
		 * 
		 * String desc =
		 * list22.getInventory().getItemAttributes().getItems().getItemDesc();
		 * 
		 * int flag = 0; if (desc.equals("NORMAL") || desc.equals("BATCH")) { if
		 * (desc.equals("BATCH")) flag = 1;
		 * 
		 * // LocationInventory lc1 = NormalBatchItem(list2, inv, flag);
		 * list22.getInventory().setQuantity(inv.getQuantityChange() +
		 * list22.getInventory().getQuantity());
		 * 
		 * LocationInventory lc_dup = list22;
		 * 
		 * dao.save(lc_dup);
		 * 
		 * 
		 * if (lc1 == null) return new ResponseEntity<GenericResponse>(new
		 * GenericResponse(HttpStatus.BAD_REQUEST, "Invalid update"),
		 * HttpStatus.BAD_REQUEST);
		 * 
		 * 
		 * 
		 * return new ResponseEntity<GenericResponse>(new
		 * GenericResponse(HttpStatus.ACCEPTED, lc_dup), HttpStatus.ACCEPTED);
		 * 
		 * return lc_dup;
		 * 
		 * } else if (desc.equals("SERIAL")) {
		 * 
		 * list22.getInventory().setQuantity(list22.getInventory().getQuantity() +
		 * inv.getInventory().getItemAttributes().getItemTags().getSerialList().size());
		 * List<String> sl =
		 * list22.getInventory().getItemAttributes().getItemTags().getSerialList();
		 * 
		 * sl.addAll(inv.getInventory().getItemAttributes().getItemTags().getSerialList(
		 * ));
		 * 
		 * list22.getInventory().getItemAttributes().getItemTags().setSerialList(sl);
		 * LocationInventory lc_dup = list22; dao.save(lc_dup);
		 * 
		 * return new ResponseEntity<GenericResponse>(new
		 * GenericResponse(HttpStatus.ACCEPTED, lc_dup), HttpStatus.ACCEPTED);
		 * 
		 * return lc_dup; }
		 * 
		 * else {
		 * 
		 * 
		 * return new ResponseEntity<GenericResponse>( new
		 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Update"),
		 * HttpStatus.INTERNAL_SERVER_ERROR);
		 * 
		 * throw new illegalArgument("Invalid id");
		 * 
		 * }
		 * 
		 * }
		 */
		System.out.println("invalid  "+InvalidResonCode(inv.getReasonCode()));
		System.out.println("invalid"+InvalidInvSt(inv.getInventory().getInventoryStatus()));

		if (InvalidResonCode(inv.getReasonCode())||InvalidInvSt(inv.getInventory().getInventoryStatus())){
				
			//throw new illegalArgument("Illegal Inventory Status ");
			System.out.print("invalid");
				throw new illegalArgument("Illegal Reason Code/Inventory Status");
		}
				
		/*
		 * return new ResponseEntity<GenericResponse>( new
		 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR,
		 * "Invalid Reasoncode/InventoryStatus"), HttpStatus.INTERNAL_SERVER_ERROR);
		 */

		if (getInv22(inv.getLocation(), inv.getInventory().getItemAttributes().getItems(),
				inv.getInventory().getInventoryStatus())) {

			Inventory invt = inv.getInventory();
			String desc = invt.getItemAttributes().getItems().getItemDesc();

			List<LocationInventory> locInv = getInv221(inv.getLocation(),
					inv.getInventory().getItemAttributes().getItems(), inv.getInventory().getInventoryStatus());

			int flag = 0;
			if (desc.equals("NORMAL") || desc.equals("BATCH")) {
				if (desc.equals("BATCH"))
					flag = 1;

				LocationInventory lc1 = NormalBatchItem(locInv, inv, flag);

				if (lc1 == null)
					/*
					 * return new ResponseEntity<GenericResponse>( new
					 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid update"),
					 * HttpStatus.INTERNAL_SERVER_ERROR);
					 */
					throw new illegalArgument("Invalid Arguments");

				/*
				 * return new ResponseEntity<GenericResponse>(new
				 * GenericResponse(HttpStatus.ACCEPTED, lc1), HttpStatus.ACCEPTED);
				 */
				return lc1;

			} else if (desc.equals("SERIAL")) {

				LocationInventory lc = serialItem(locInv, inv);

				if (lc.equals(null))
					/*
					 * return new ResponseEntity<GenericResponse>( new
					 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid update"),
					 * HttpStatus.INTERNAL_SERVER_ERROR);
					 */
					throw new illegalArgument("Invalid Arguments");

				/*
				 * return new ResponseEntity<GenericResponse>(new
				 * GenericResponse(HttpStatus.ACCEPTED, lc), HttpStatus.ACCEPTED);
				 */
				return lc;

			}

			else {

				/*
				 * return new ResponseEntity<GenericResponse>( new
				 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Update"),
				 * HttpStatus.INTERNAL_SERVER_ERROR);
				 */
				throw new illegalArgument("Invalid update");
			}

		}

		// else if (exitsLoc(inv.getLocation().getLocationId())
		// || exitsInv(inv.getInventory().getItemAttributes().getItems().getItemName()))
		// {

		else if (validItem(inv.getInventory().getItemAttributes().getItems()) && validLoc(inv.getLocation())) {

			LocationInventoryCreate lc = new LocationInventoryCreate(inv.getInventory(), inv.getLocation(),
					inv.getReasonCode());
			if (inv.getInventory().getItemAttributes().getItems().getItemDesc().equals("NORMAL")) {

				lc.getInventory().setQuantity(inv.getQuantityChange());

				ItemTagAttributes i = new ItemTagAttributes();
				lc.getInventory().getItemAttributes().setItemTags(i);
				lc.getInventory().getItemAttributes().setItems(inv.getInventory().getItemAttributes().getItems());
			} else if (inv.getInventory().getItemAttributes().getItems().getItemDesc().equals("BATCH")) {

				lc.getInventory().setQuantity(inv.getQuantityChange());
				if (inv.getInventory().getItemAttributes().getItemTags().getBatchNo().equals(""))
					/*
					 * return new ResponseEntity<GenericResponse>( new
					 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Batch No required"),
					 * HttpStatus.INTERNAL_SERVER_ERROR);
					 */
					throw new illegalArgument("BatchNo Required");

				ItemTagAttributes i = new ItemTagAttributes();
				i.setBatchNo(inv.getInventory().getItemAttributes().getItemTags().getBatchNo());
				lc.getInventory().getItemAttributes().setItemTags(i);
				lc.getInventory().getItemAttributes().setItems(inv.getInventory().getItemAttributes().getItems());
			} else if (inv.getInventory().getItemAttributes().getItems().getItemDesc().equals("SERIAL")) {

				double len = inv.getInventory().getItemAttributes().getItemTags().getSerialList().size();

				if (inv.getInventory().getItemAttributes().getItemTags().getSerialList().size() == 0)
					/*
					 * return new ResponseEntity<GenericResponse>( new
					 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Serials No required"),
					 * HttpStatus.INTERNAL_SERVER_ERROR);
					 */
					throw new illegalArgument("Serials Required");

				ItemTagAttributes i = new ItemTagAttributes();
				i.setSerialList(inv.getInventory().getItemAttributes().getItemTags().getSerialList());
				lc.getInventory().setQuantity(len);

				lc.getInventory().getItemAttributes().setItemTags(i);

				lc.getInventory().getItemAttributes().setItems(inv.getInventory().getItemAttributes().getItems());
			} else
				/*
				 * return new ResponseEntity<GenericResponse>( new
				 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Item"),
				 * HttpStatus.INTERNAL_SERVER_ERROR);
				 */
				throw new illegalArgument("Invalid update");

			LocationInventory l1 = saveInventory_dup(lc, getSequenceNumber(LocationInventory.SEQUENCE_NAME));
			/*
			 * return new ResponseEntity<GenericResponse>(new
			 * GenericResponse(HttpStatus.CREATED, l1), HttpStatus.CREATED);
			 */
			return l1;
		}

		// return new ResponseEntity<GenericResponse>(new
		// GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Item or //
		// //Location"), HttpStatus.INTERNAL_SERVER_ERROR);

		// }
		else {

			/*
			 * return new ResponseEntity<GenericResponse>( new
			 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Update"),
			 * HttpStatus.INTERNAL_SERVER_ERROR);
			 */
			throw new illegalArgument("Invalid Arguments");

		}

	}

	public LocationInventory NormalBatchItem(List<LocationInventory> locInv, LocationInventoryUpdate inv, int flag) {

		for (LocationInventory lt : locInv) {
			if (lt.getInventory().getItemAttributes().getItems().equals(
					inv.getInventory().getItemAttributes().getItems()) && lt.getLocation().equals(inv.getLocation())) {

				lt.getInventory().setQuantity(inv.getQuantityChange() + lt.getInventory().getQuantity());

				// if (flag == 1) {

				// if
				// (inv.getInventory().getItemAttributes().getItemTags().getBatchNo().equals(""))
				// return null;
				// lt.getInventory().getItemAttributes().getItemTags()
				// .setBatchNo(inv.getInventory().getItemAttributes().getItemTags().getBatchNo());
				// }

				LocationInventory lt_dup = lt;

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dao.save(lt_dup);
//System.out.println(10/0);
				return lt_dup;
			}

		}
		return null;

	}

	public LocationInventory serialItem(List<LocationInventory> locInv, LocationInventoryUpdate inv) {
		for (LocationInventory lt : locInv) {

			if (lt.getInventory().getItemAttributes().getItems().equals(
					inv.getInventory().getItemAttributes().getItems()) && lt.getLocation().equals(inv.getLocation())) {

				List<String> sl = lt.getInventory().getItemAttributes().getItemTags().getSerialList();

				sl.addAll(inv.getInventory().getItemAttributes().getItemTags().getSerialList());

				lt.getInventory().getItemAttributes().getItemTags().setSerialList(sl);

				if (inv.getInventory().getItemAttributes().getItemTags().getSerialList().size() == 0)
					return null;
				int len = inv.getInventory().getItemAttributes().getItemTags().getSerialList().size();

				lt.getInventory().setQuantity(lt.getInventory().getQuantity() + len);

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LocationInventory lt_dup = lt;
				dao.save(lt_dup);

				return lt_dup;

			}

		}
		return null;

	}

	public /* ResponseEntity<GenericResponse> */ LocationInventory changeLoc(ChangeLocationInventoryAttributes inv)
			throws illegalArgument {

		if (!validToInv(inv.getToInventory()) || InvalidResonCode(inv.getReasonCode()))

			/*
			 * return new ResponseEntity<GenericResponse>( new
			 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR,
			 * "Invalid ToInventory/ReasonCode"), HttpStatus.INTERNAL_SERVER_ERROR);
			 */
			throw new illegalArgument("Invalid To Inventory or ReasonCode");

		/*
		 * if (inv.getId() != 0) { LocationInventory list22 = getInvbyId(inv.getId());
		 * 
		 * list22.getInventory().setInventoryStatus(inv.getToInventory().
		 * getInventoryStatus()); list22.getInventory().getItemAttributes().getItems()
		 * .setProductClass(inv.getToInventory().getProductClass());
		 * 
		 * if (!inv.getToInventory().getShipByDate().isEmpty())
		 * list22.getInventory().setShipByDate(inv.getToInventory().getShipByDate()); if
		 * (!inv.getToInventory().getBatchNo().isEmpty()) { if
		 * (list22.getInventory().getItemAttributes().getItems().getItemDesc().equals(
		 * "BATCH")) list22.getInventory().getItemAttributes().getItemTags()
		 * .setBatchNo(inv.getToInventory().getBatchNo()); }
		 * 
		 * LocationInventory lt_dup = list22; dao.save(lt_dup);
		 * 
		 * //return new ResponseEntity<GenericResponse>(new
		 * GenericResponse(HttpStatus.OK, lt_dup), HttpStatus.OK); return lt_dup;
		 * 
		 * }
		 */

		if (getInv22(inv.getLocation(), inv.getFromInventory().getItemAttributes().getItems(),
				inv.getFromInventory().getInventoryStatus())) {
			List<LocationInventory> locInv = getInv221(inv.getLocation(),
					inv.getFromInventory().getItemAttributes().getItems(), inv.getFromInventory().getInventoryStatus());
			for (LocationInventory lt : locInv) {
				lt.getInventory().setInventoryStatus(inv.getToInventory().getInventoryStatus());
				lt.getInventory().getItemAttributes().getItems()
						.setProductClass(inv.getToInventory().getProductClass());

				if (!inv.getToInventory().getShipByDate().isEmpty())
					lt.getInventory().setShipByDate(inv.getToInventory().getShipByDate());

				LocationInventory lt_dup = lt;
				dao.save(lt_dup);

				/*
				 * return new ResponseEntity<GenericResponse>(new
				 * GenericResponse(HttpStatus.ACCEPTED, lt_dup), HttpStatus.ACCEPTED);
				 */
				return lt_dup;

			}

		} else
			/*
			 * return new ResponseEntity<GenericResponse>( new
			 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR,
			 * "Not valid location inventory"), HttpStatus.INTERNAL_SERVER_ERROR);
			 */
			throw new illegalArgument("Invalid Arguments");

		/*
		 * return new ResponseEntity<GenericResponse>( new
		 * GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Attributtes"),
		 * HttpStatus.INTERNAL_SERVER_ERROR);
		 */
		throw new illegalArgument("Invalid Update");

	}

	public LocationInventory changebyId(int id, ChangeLocationInventoryAttributes inv) throws illegalArgument {
		// TODO Auto-generated method stub
		/* Optional< */LocationInventory list22 = getAllInvbyId(id);
		// LocationInventory list22 = op1.get();

		// String desc =
		// list22.getInventory().getItemAttributes().getItems().getItemDesc();

		list22.getInventory().setInventoryStatus(inv.getToInventory().getInventoryStatus());
		list22.getInventory().getItemAttributes().getItems().setProductClass(inv.getToInventory().getProductClass());

		if (!inv.getToInventory().getShipByDate().isEmpty())
			list22.getInventory().setShipByDate(inv.getToInventory().getShipByDate());
		if (!inv.getToInventory().getBatchNo().isEmpty()) {
			if (list22.getInventory().getItemAttributes().getItems().getItemDesc().equals("BATCH"))
				list22.getInventory().getItemAttributes().getItemTags().setBatchNo(inv.getToInventory().getBatchNo());
		}

		LocationInventory lt_dup = list22;
		dao.save(lt_dup);

		// return new ResponseEntity<GenericResponse>(new GenericResponse(HttpStatus.OK,
		// lt_dup), HttpStatus.OK);
		return lt_dup;

	}

	

}
