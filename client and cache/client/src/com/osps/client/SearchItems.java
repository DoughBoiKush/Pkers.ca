package com.osps.client;

import com.osps.client.cache.def.ItemDef;

public class SearchItems {
	
	public static SearchItems[] list = new SearchItems[22600];
	
	public static void loadList() {
		for (int i = 0; i < list.length; i++) {
			ItemDef def = ItemDef.forID(i);
			if (def != null && def.name != null) {
				if (def.certTemplateID == -1) {
					list[i] = new SearchItems(i, def.name);
				}
			}
		}
	}
	
	public int id;
	public String name;
	public String lname;
	
	public SearchItems(int id, String name) {
		this.id = id;
		this.name = name;
		this.lname = name.toLowerCase();
	}

}