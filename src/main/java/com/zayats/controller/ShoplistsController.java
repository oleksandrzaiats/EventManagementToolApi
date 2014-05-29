package com.zayats.controller;

import com.zayats.dal.EventRepository;
import com.zayats.dal.ShoplistRepository;
import com.zayats.model.Event;
import com.zayats.model.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home/shoplists")
public class ShoplistsController {

	@Autowired
	ShoplistRepository listRepository;

	@Autowired
    EventRepository eventRepository;

	private static final Logger logger = Logger
			.getLogger(ShoplistsController.class);

	@RequestMapping(value = "/{username}")
	public @ResponseBody
	List<Event> getFamilies(@PathVariable String username, Model model,
			Principal principal) {
		List<Event> list = null;
		try {
			logger.info("Get families for user.");
			list = eventRepository.getAllEventsForUser(username);
		} catch (EmptyResultDataAccessException e) {
			logger.info("Can't get families for user. Please, try later.s");
		}

		return list;
	}

	/*@RequestMapping(value = "/get/{familyId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Shoplist> getShoplists(@PathVariable int familyId) {

		List<Shoplist> shoplists = listRepository
				.getShoplistsForFamily(familyId);

		return shoplists;
	}*/

	@RequestMapping(value = "/create/{name}/{familyId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> createShoplist(@PathVariable int familyId,
			@PathVariable String name) {

		List<Boolean> created = new ArrayList<Boolean>();

		created.add(listRepository.createShoplist(name, familyId));

		return created;
	}

	@RequestMapping(value = "/shopitems/{shoplistId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Task> getShopitems(@PathVariable int shoplistId) {

		List<Task> tasks = listRepository
				.getItemsForShoplist(shoplistId);

		return tasks;
	}

	@RequestMapping(value = "/delete/{shoplistId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> deleteShoplist(@PathVariable int shoplistId) {
		List<Boolean> result = new ArrayList<Boolean>();

		result.add(listRepository.deleteShoplist(shoplistId));

		return result;
	}

	@RequestMapping(value = "/addShopitem/{name}/{quantity}/{shoplistId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> addShopitem(@PathVariable int shoplistId,
			@PathVariable String quantity, @PathVariable String name) {
		List<Boolean> result = new ArrayList<Boolean>();

		result.add(listRepository.addShopitem(name, quantity, shoplistId));

		return result;
	}

	@RequestMapping(value = "/deleteShopitem/{shopitemId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> deleteShopitem(@PathVariable int shopitemId) {
		List<Boolean> result = new ArrayList<Boolean>();

		result.add(listRepository.deleteShopitem(shopitemId));

		return result;
	}

	@RequestMapping(value = "/buyShopitem/{shopitemId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> buyShopitem(@PathVariable int shopitemId) {
		List<Boolean> result = new ArrayList<Boolean>();

		result.add(listRepository.buyShopitem(shopitemId));

		return result;
	}

}
