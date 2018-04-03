package com.osps.client.entity;

import com.osps.client.entity.model.Class33;
import com.osps.client.entity.model.Model;
import com.osps.client.link.NodeSub;

public class Animable extends NodeSub {
	
	public int modelHeight;
	public Class33 aClass33Array1425[];

	public void method443(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2) {
		Model model = getRotatedModel();
		if(model != null) {
			modelHeight = model.modelHeight;
			model.method443(i, j, k, l, i1, j1, k1, l1, i2);
		}
	}

	public Model getRotatedModel() {
		return null;
	}

	public Animable() {
		modelHeight = 1000;
	}
}