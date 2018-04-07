package org.osps.model.players.packets;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;
import org.osps.database.Poker;
import org.osps.model.content.help.HelpDatabase;
import org.osps.model.content.help.HelpRequest;
import org.osps.model.content.presets.Preset;
import org.osps.model.items.bank.BankPin;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.net.Packet;

public class InputField implements PacketType {

	@Override
	public void processPacket(Player player, Packet packet) {
		int id = packet.getInt();
		String text = packet.getRS2String();
		/*if (player.getRights().isDeveloper()) {
			player.sendMessage("Component; "+id+", input; " + text);
		}*/
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
                
                    if (id == 0) {
                            player.shopSellAmount = Integer.parseInt(text);
                        } else if (id == 1){
                            player.shopSellPrice = Integer.parseInt(text);
                        }
                
                
		switch (id) {
		
		case 60002:
			if (!player.hasAccountPin && player.isSettingAccountPin) {
				if (text.length() < 4 || text.length() > 6) {
					player.sendMessage("Your pin needs to be between 4 and 6 digits.");
					return;
				}
				player.setAccountPin(text);
				player.sendMessage("@red@Your account pin is now: " + player.getAccountPin());
				player.hasAccountPin = true;
				player.isSettingAccountPin = false;
				player.setAccountPinMac(player.getMacAddress());
				player.getPA().closeAllWindows();
				return;
			}
			player.sendMessage("@red@Please stop trying to cheat the system.");
			player.getPA().closeAllWindows();
			break;
			
		case 64002:
			if (player.needsAccountPin && player.hasAccountPin) {
				if (text.length() < 4 || text.length() > 6) {
					player.sendMessage("@red@Your entry needs to be between 4 and 6 digits.");
					return;
				}
				if (!text.equalsIgnoreCase(player.getAccountPin())) {
					player.sendMessage("@red@You have entered an incorrect pin.");
					return;
				}
				if (text.equalsIgnoreCase(player.getAccountPin())) {
					player.canUsePackets = true;
					player.getPA().closeAllWindows();
					player.sendMessage("@red@You have entered the correct pin and unlocked your account!");
					player.setAccountPinMac(player.getMacAddress());
					return;
				}
			}
			player.sendMessage("@red@Please stop trying to cheat the system.");
			break;
			
		case 63002:
			if (text.length() < 4 || text.length() > 6) {
				player.sendMessage("@red@Your entry needs to be between 4 and 6 digits.");
				return;
			}
			if (text.equalsIgnoreCase(player.getAccountPin())) {
				player.canUsePackets = true;
				player.getPA().closeAllWindows();
				player.sendMessage("@red@You have entered the correct pin and reset your account pin!");
				player.hasAccountPin = false;
				return;
			}
			break;
		
		case 33205:
			player.getPunishmentPanel().setReason(text);
			break;
			
		case 33211:
			player.getPunishmentPanel().setDuration(Integer.parseInt(text));
			break;
		
			case 59527:
				if (text.length() < 25) {
					player.sendMessage("Your help request must contain 25 characters for the description.");
					return;
				}
				List<Player> staff = PlayerHandler.getPlayerList().stream().filter(Objects::nonNull)
						.filter(p -> p.getRights().isBetween(1, 3) || p.getRights().isHelper()).collect(Collectors.toList());
				if (HelpDatabase.getDatabase().requestable(player)) {
					HelpDatabase.getDatabase().add(new HelpRequest(player.playerName, player.connectedFrom, text));
					if (staff.size() > 0) {
						PlayerHandler.sendMessage("[@blu@HelpDB@bla@] " + WordUtils.capitalize(player.playerName) + ""
								+ " is requesting help, type ::helpdb to view their request.", staff);
						player.sendMessage("You request has been sent, please wait as a staff member gets back to you.");
					} else {
						player.sendMessage("There are no staff online to help you at this time, please be patient.");
					}
				}
				player.getPA().removeAllWindows();
				break;
				
			case 62502:
				if(player.ironman == true){
					player.sendMessage("Iron man cannot participate in poker.");
					return;
				}
				if (player.getItems().playerHasItem(995, Integer.valueOf(text))) {
					player.getItems().deleteItem2(995, Integer.valueOf(text));
					Poker.deposit(player,text);
					player.sendMessage("Your have deposited "+text+" Coins into your poker account!");
				} else {
					player.sendMessage("You do not have that much coins!");
				}
				player.getPA().removeAllWindows();
			break;
			
			case 32002:
				Preset preset = player.getPresets().getCurrent();
				if (preset == null) {
					player.sendMessage("You must select a preset before changing the name.");
					return;
				}
				preset.setAlias(text);
				player.getPresets().refreshMenus(preset.getMenuSlot(), preset.getMenuSlot() + 1);
				break;
	
			case 58063:
				if (player.getPA().viewingOtherBank) {
					player.getPA().resetOtherBank();
					return;
				}
				if (player.isBanking) {
					player.getBank().getBankSearch().setText(text);
					player.getBank().setLastSearch(System.currentTimeMillis());
					if (text.length() > 2) {
						player.getBank().getBankSearch().updateItems();
						player.getBank().setCurrentBankTab(player.getBank().getBankSearch().getTab());
						player.getItems().resetBank();
						player.getBank().getBankSearch().setSearching(true);
					} else {
						if (player.getBank().getBankSearch().isSearching())
							player.getBank().getBankSearch().reset();
						player.getBank().getBankSearch().setSearching(false);
					}
				}
				break;
	
			case 59507:
				if (player.getBankPin().getPinState() == BankPin.PinState.CREATE_NEW)
					player.getBankPin().create(text);
				else if (player.getBankPin().getPinState() == BankPin.PinState.UNLOCK)
					player.getBankPin().unlock(text);
				else if (player.getBankPin().getPinState() == BankPin.PinState.CANCEL_PIN)
					player.getBankPin().cancel(text);
				else if (player.getBankPin().getPinState() == BankPin.PinState.CANCEL_REQUEST)
					player.getBankPin().cancel(text);
				break;
	
			default:
				break;
		}
	}

}
