package com.zayats.dal;

import com.zayats.model.Task;

import java.util.List;

public interface ShoplistRepository {

	public boolean createShoplist(String name, int familyId);

	public boolean deleteShoplist(int listId);

//	public List<Shoplist> getShoplistsForFamily(int familyId);

	public List<Task> getItemsForShoplist(int listId);

	public boolean addShopitem(String name, String quantity, int shoplistId);

	public boolean deleteShopitem(int shopitemId);

	public boolean buyShopitem(int shopitemId);
}
