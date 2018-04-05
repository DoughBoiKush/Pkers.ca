package org.osps.model.content.dialogue.teleport;

import java.util.function.Consumer;

import org.osps.model.content.dialogue.OptionDialogue;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;

public class TeleportDialogue extends OptionDialogue {

	private int page;

	private final Teleport[] teleports;

	public TeleportDialogue(Teleport[] teleports) {
		this.teleports = teleports;
	}

	public void setLines() {
		if (!hasPages()) {
			String[] lines = new String[teleports.length];
			for (int i = 0; i < teleports.length; i++) {
				lines[i] = teleports[i].getName();
			}
			setLines(lines);
			return;
		}

		int limit = getLimit();
		int offset = getOffset();
		int count = limit - offset;

		String[] lines = new String[5];
		for (int i = 0; i < lines.length; i++) {
			if (i < count) {
				lines[i] = teleports[offset + i].getName();
				continue;
			}
			lines[i] = "";
		}

		lines[4] += "@blu@Next (" + (page + 1) + "/" + getPages() + ")";

		setLines(lines);
		setNext(0);
	}

	@Override
	public void execute() {
		if (getNext() == 0) {
			setLines();
			super.execute();
			return;
		}

		if (getNext() == 5 && hasPages()) {
			if (onLastPage()) {
				page = 0;
			} else {
				page++;
			}
			setLines();
			super.execute();
			return;
		}

		int index = getOffset() + getNext() - 1;

		if (index > teleports.length) {
			getPlayer().getPA().removeAllWindows();
			return;
		}

		Consumer<Player> consumer = teleports[index].getConsumer();
		if (consumer != null) {
			consumer.accept(getPlayer());
		} else {
			getPlayer().setDialogue(null);
			getPlayer().getPA().closeAllWindows();

			if (teleports[index].isDangerous()) {
				player.start(new OptionDialogue("Proceed with dangerous teleport", p -> {
					TeleportExecutor.teleport(getPlayer(), teleports[index], true);
					getPlayer().setDialogue(null);
					getPlayer().getPA().closeAllWindows();
				}, "Cancel", p -> {
					getPlayer().setDialogue(null);
					getPlayer().getPA().closeAllWindows();
				}));
			} else {
				TeleportExecutor.teleport(getPlayer(), teleports[index], true);
			}
		}

	}

	private int getOffset() {
		return page * 4;
	}

	private int getLimit() {
		int limit = getOffset() + 4;
		return limit > teleports.length ? teleports.length : limit;
	}

	private boolean hasPages() {
		return teleports.length > 5;
	}

	private boolean onLastPage() {
		return getLimit() == teleports.length;
	}

	private int getPages() {
		return (int) Math.ceil(teleports.length / 4D);
	}

}