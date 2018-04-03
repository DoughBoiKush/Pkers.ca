package org.osps.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.osps.model.items.ItemAssistant;
import org.osps.model.players.Player;

public class RemoveItems {

	private static final int EQUIP_SLOT = 0;
	private static final int INV_SLOT = 1;
	private static final int BANK_SLOT = 2;
	protected transient static int index;

	public static void main(String[] args) {
		File f = new File("./Data/characters/");
		for(File f2: f.listFiles()){
			delete(f2);
		}
	}
	
	public static int remove[] = {
	/*		13072, 13073, 11849, 2572, 2573, 4166, 4167, 4168, 4169, 4551, 4552, 4164, 4165, 12783, 10887,
			11794, 11795, 11796, 11797, 11798, 11799, 11800, 11801, 11818, 11819, 11820, 11821, 11822, 11823, 12692, 
			12691,

			9747, 9748, 9749, 9751, 9752, 9753, 9754, 9757, 9758, 9760, 9761, 9755, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795,
			9792, 9774, 9771, 9777, 9786, 9810, 9765, 9789, 9763, 9764, 9948, 13280, 13281, 13282, 13329, 13330,
			13331, 13332, 13333, 13334, 13335, 13336, 13337, 13338, 13342, 9766, 9767, 9768, 9769, 9770, 9772,
			9774, 9775, 9776, 9778, 9779, 9781, 9782, 9784, 9785, 9787, 9788, 9790, 9791, 9793, 9794, 9796, 9797,
			9799, 9800, 9802, 9803, 9805, 9806, 9808, 9809, 9811, 9812, 9813, 9814*/
			
			10330, 10331, 10332, 10333, 10334, 10335, 10336, 10337, 10338,
			10339, 10340, 10341, 10342, 10343, 10344, 10345, 10346, 10347,
			10348, 10349, 10350, 10351, 10352, 10353, 12422, 12423,
			12424, 12425, 12426, 12427, 12437, 12438, 12425, 12426, 12424,
			
			1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047,
			1048, 1049, 1050, 1051, 963, 11934, 11935, 11936, 11937, 11934, 11935
			
	
	};

	public static boolean removeItem(int itemId,int amount,int slotType){
		for (int i : remove) {
			if(itemId == i){
				return true;
			}
		}
		return false;
	}

	public static void delete(File f){
		boolean change = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			LinkedList<String> list = new LinkedList<String>();
			String line= "";
			while((line = br.readLine()) != null){
				int slotType = -1;
				if(line.startsWith("character-equip")){
					slotType = EQUIP_SLOT;
				}
				if(line.startsWith("character-item")){
					slotType = INV_SLOT;
				}
				if(line.startsWith("character-bank")){
					slotType = BANK_SLOT;
				}
				if(line.startsWith("character-equip") || line.startsWith("character-item") || line.startsWith("bank-tab")){
					String[] args = line.replaceAll("  ", " ").split(" ")[2].split("\t");
					int itemId = -1;
					int amount = -1;
					try {
						itemId = Integer.parseInt(args[1]);
						amount = Integer.parseInt(args[2]);
						if(line.startsWith("character-item") || line.startsWith("bank-tab"))
							itemId--;
					} catch(Exception e){
						System.out.println("error executing: "+line);
						System.out.println("arg1: "+args[1]);
					}
					
					if(removeItem(itemId,amount,slotType)){
						System.out.println("removing: "+line);
						change = true;
						continue;
					}
					
				}
				list.add(line);
			}
			br.close();
			if(change){
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				for(String s : list){
					bw.write(s);
					bw.newLine();
				}
				bw.flush();
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
