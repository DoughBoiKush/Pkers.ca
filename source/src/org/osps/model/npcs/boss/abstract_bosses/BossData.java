package org.osps.model.npcs.boss.abstract_bosses;

public class BossData extends DefaultBossEvent {
	
	private String NAME_BOSS = "Glough";
	
	public BossData() {
		super();
	}
	
	@Override
	protected int[] npcIds() {
		final int[] data = {
				7101
		};
		return data;
	}
	
	@Override
	protected int[][] locations() {
		final int[][] data = {
				{3265, 3880}
		};
		return data;
	}

	@Override
	protected String[] announcements() {
		final String[] data = {
				"@blu@"+NAME_BOSS+" has spawned at ::wb in a multi combat wilderness zone!"
				//"@blu@Kamil has spawned to the north-east of the castle in a multi combat zone!"
		};
		return data;
	}
	
}