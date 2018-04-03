package com.osps.client.cache.def;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.osps.client.Client;
import com.osps.client.Stream;
import com.osps.client.VarBit;
import com.osps.client.cache.StreamLoader;
import com.osps.client.cache.anim.Class36;
import com.osps.client.entity.model.Model;
import com.osps.client.link.MRUNodes;
import com.osps.client.net.OnDemandFetcher;

public final class ObjectDef {
	

	public void setTexture(int modelID, Model m) {
		if (modelID == 28260){
			m.setTexture(26);
		}
	}
	
	public static ObjectDef forID(int i) {
		if (i > streamIndices.length)
			i = streamIndices.length - 1;
		for (int j = 0; j < 20; j++)
			if (cache[j].type == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 20;
		ObjectDef objectDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[i];
		objectDef.type = i;
		objectDef.setDefaults();
		objectDef.readValues(stream);
		
//		final int[] OBJECTS = { 7123, 7158, 7160, 24051, 24051, 7111, 7108 };
//		for (int id : OBJECTS) {
//			if (i == id) {
//				for (int i4 = 0; i4 < objectDef.anIntArray773.length; i4++)
//				objectDef.anIntArray773[i4] = 0;
//				objectDef.aBoolean767 = false;
//				return objectDef;
//			}
//		}
		
		switch (i) {
		case 11338://bank booth
			objectDef.actions[2] = "Use-quickly";
			break;
		case 411:
			objectDef.name = "@cya@Altar";
			objectDef.actions[0] = "Restore @gre@Health";
			objectDef.actions[1] = "Restore @gre@Special Attack";
			break;
		case 6552:
			objectDef.actions[0] = "Switch Books";
			break;

		case 1990:
			objectDef.name = "Survival Crate";
			objectDef.actions[0] = "Unpack";
			break;

		case 19038:
			objectDef.actions = new String[] { null, null, null, null, null };
			objectDef.anInt744 = 3;
			objectDef.anInt761 = 3;
			objectDef.anInt740 = 340; // Width
			objectDef.anInt748 = 500; // Thickness
			objectDef.anInt772 = 400; // Height
			break;

		case 13641:
			objectDef.name = "Teleportation Device";
			objectDef.actions = new String[] { "Quick-Teleport", null, null, null, null };
			objectDef.anInt744 = 1;
			objectDef.anInt761 = 1;
			objectDef.anInt740 = 80; // Width
			objectDef.anInt748 = 80; // Thickness
			objectDef.anInt772 = 250; // Height
			break;

		case 11700:
			objectDef.anIntArray773 = new int[] { 4086 };
			objectDef.name = "Venom";
			objectDef.anInt744 = 3;
			objectDef.anInt761 = 3;
			objectDef.solidObject = false;
			objectDef.aBoolean762 = true;
			objectDef.anInt781 = 1261;
			objectDef.newModelColors = new int[] { 31636 };
			objectDef.originalModelColors = new int[] { 10543 };
			objectDef.anInt748 = 160;
			objectDef.anInt772 = 160;
			objectDef.anInt740 = 160;
			objectDef.actions = new String[5];
			objectDef.description = new String(
					"It's a cloud of venomous smoke that is extremely toxic.");
			break;

			
		/*case 7489:
			objectDef.originalTextureFaces = new int[] { 24 };
			objectDef.newTextureFaces = new int[] { 10508 };
			break;
		case 7494:
			objectDef.originalTextureFaces = new int[] { 24 };
			objectDef.newTextureFaces = new int[] { 31437 };
			break;
		case 7492:
			objectDef.originalTextureFaces = new int[] { 24 };
			objectDef.newTextureFaces = new int[] { 22239 };
			break;
		case 7455:
			objectDef.originalTextureFaces = new int[] { 24 };
			objectDef.newTextureFaces = new int[] { 2576 };
			break;
		case 7485:
			objectDef.originalTextureFaces = new int[] { 24 };
			objectDef.newTextureFaces = new int[] { 53 };
			break;
		case 7484:
			objectDef.originalTextureFaces = new int[] { 24 };
			objectDef.newTextureFaces = new int[] { 4645 };
			break;*/
		case 27785:
			objectDef.anIntArray773 = new int[] { 30553 };
			break;
		
		case 14193:
			objectDef.anIntArray773 = new int[] { 30552 };
		break;

		case 14194:
			objectDef.anIntArray773 = new int[] { 30554 };
		break;

		case 14195:
			objectDef.anIntArray773 = new int[] { 30555 };
		break;	
			
		/*case 7131:
			objectDef.originalTextureFaces = new int[] { 2 };
			objectDef.newTextureFaces = new int[] { 46 };
			break;*/
		}
		/*if (Client.debugModels) {
			
		if (objectDef.name == null || objectDef.name.equalsIgnoreCase("null"))
			objectDef.name = "test";
		
		objectDef.hasActions = true;
		}*/
		return objectDef;
	}
	
	public static int totalObjects;
	
	public static void unpackConfig(StreamLoader streamLoader) {
		stream = new Stream(streamLoader.getDataForName("loc.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("loc.idx"));
	    totalObjects = stream.readUnsignedWord();
		streamIndices = new int[totalObjects];
		int i = 2;
		for (int j = 0; j < totalObjects; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ObjectDef[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new ObjectDef();
	}
	
	public void readValues(Stream stream) {
		int flag = -1;
		do {
			int type = stream.readUnsignedByte();
			if (type == 0)
				break;
			if (type == 1) {
				int len = stream.readUnsignedByte();
				if (len > 0) {
					if (anIntArray773 == null || lowMem) {
						anIntArray776 = new int[len];
						anIntArray773 = new int[len];
						for (int k1 = 0; k1 < len; k1++) {
							anIntArray773[k1] = stream.readUnsignedWord();
							anIntArray776[k1] = stream.readUnsignedByte();
						}
					} else {
						stream.currentOffset += len * 3;
					}
				}
			} else if (type == 2)
				name = stream.readString();
			else if (type == 3)
				description = stream.readString();
			else if (type == 5) {
				int len = stream.readUnsignedByte();
				if (len > 0) {
					if (anIntArray773 == null || lowMem) {
						anIntArray776 = null;
						anIntArray773 = new int[len];
						for (int l1 = 0; l1 < len; l1++)
							anIntArray773[l1] = stream.readUnsignedWord();
					} else {
						stream.currentOffset += len * 2;
					}
				}
			} else if (type == 14)
				anInt744 = stream.readUnsignedByte();
			else if (type == 15)
				anInt761 = stream.readUnsignedByte();
			else if (type == 17)
				aBoolean767 = false;
			else if (type == 18)
				aBoolean757 = false;
			else if (type == 19)
				hasActions = (stream.readUnsignedByte() == 1);
			else if (type == 21)
				aBoolean762 = true;
			else if (type == 22)
				aBoolean769 = false;
			else if (type == 23)
				aBoolean764 = true;
			else if (type == 24) {
				anInt781 = stream.readUnsignedWord();
				if (anInt781 == 65535)
					anInt781 = -1;
			} else if (type == 28)
				anInt775 = stream.readUnsignedByte();
			else if (type == 29)
				aByte737 = stream.readSignedByte();
			else if (type == 39)
				aByte742 = stream.readSignedByte();
			else if (type >= 30 && type < 39) {
				if (actions == null)
					actions = new String[5];
				actions[type - 30] = stream.readString();
				if (actions[type - 30].equalsIgnoreCase("hidden"))
					actions[type - 30] = null;
			} else if (type == 40) {
				int i1 = stream.readUnsignedByte();
				modifiedModelColors = new int[i1];
				originalModelColors = new int[i1];
				for (int i2 = 0; i2 < i1; i2++) {
					modifiedModelColors[i2] = stream.readUnsignedWord();
					originalModelColors[i2] = stream.readUnsignedWord();
				}
			} else if (type == 41) {
				int j2 = stream.readUnsignedByte();
				modifiedTexture = new short[j2];
				originalTexture = new short[j2];
				for (int k = 0; k < j2; k++) {
					modifiedTexture[k] = (short) stream.readUnsignedWord();
					originalTexture[k] = (short) stream.readUnsignedWord();
				}

			} else if (type == 60)
				anInt746 = stream.readUnsignedWord();
			else if (type == 62)
				aBoolean751 = true;
			else if (type == 64)
				aBoolean779 = false;
			else if (type == 65)
				anInt748 = stream.readUnsignedWord();
			else if (type == 66)
				anInt772 = stream.readUnsignedWord();
			else if (type == 67)
				anInt740 = stream.readUnsignedWord();
			else if (type == 68)
				anInt758 = stream.readUnsignedWord();
			else if (type == 69)
				anInt768 = stream.readUnsignedByte();
			else if (type == 70)
				anInt738 = stream.readSignedWord();
			else if (type == 71)
				anInt745 = stream.readSignedWord();
			else if (type == 72)
				anInt783 = stream.readSignedWord();
			else if (type == 73)
				aBoolean736 = true;
			else if (type == 74)
				aBoolean766 = true;
			else if (type == 75)
				anInt760 = stream.readUnsignedByte();
			else if (type == 77) {
				anInt774 = stream.readUnsignedWord();
				if (anInt774 == 65535)
					anInt774 = -1;
				anInt749 = stream.readUnsignedWord();
				if (anInt749 == 65535)
					anInt749 = -1;
				int j1 = stream.readUnsignedByte();
				childrenIDs = new int[j1 + 1];
				for (int j2 = 0; j2 <= j1; j2++) {
					childrenIDs[j2] = stream.readUnsignedWord();
					if (childrenIDs[j2] == 65535)
						childrenIDs[j2] = -1;
				}
			}
		} while (true);
		if (flag == -1  && name != "null" && name != null) {
			hasActions = anIntArray773 != null
			&& (anIntArray776 == null || anIntArray776[0] == 10);
			if (actions != null)
				hasActions = true;
		}
		if (aBoolean766) {
			aBoolean767 = false;
			aBoolean757 = false;
		}
		if (anInt760 == -1)
			anInt760 = aBoolean767 ? 1 : 0;
	}

	
	private void readValuesOld(Stream buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.readUnsignedByte();
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int modelLength = buffer.readUnsignedByte();
					anIntArray773 = new int[modelLength];
					anIntArray776 = new int[modelLength];
					for(int i2 = 0; i2 < modelLength; i2++) {
						anIntArray773[i2] = buffer.readUnsignedWord();
						anIntArray776[i2] = buffer.readUnsignedByte();
					}
				} else if (opcode == 2)
					name = buffer.readString();
				else if (opcode == 3)
					description = buffer.readString();
				else if (opcode == 5) {
					int length = buffer.readUnsignedByte();
					anIntArray776 = null;
					anIntArray773 = new int[length];
					for (int l1 = 0; l1 < length; l1++)
						anIntArray773[l1] = buffer.readUnsignedWord();
				} else if (opcode == 14)
					anInt744 = buffer.readUnsignedByte();
				else if (opcode == 15)
					anInt761 = buffer.readUnsignedByte();
				else if (opcode == 17)
					solidObject = false;
				else if (opcode == 18)
					aBoolean757 = false;
				else if (opcode == 19) {
					i = buffer.readUnsignedByte();
					if (i == 1)
						hasActions = true;
				} else if (opcode == 21)
					aBoolean762 = true;
				else if (opcode == 22)
					aBoolean769 = true;
				else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					anInt781 = buffer.readUnsignedWord();
					if (anInt781 == 65535)
						anInt781 = -1;
				} else if (opcode == 28)
					anInt775 = buffer.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = buffer.readSignedByte();
				else if (opcode == 39)
					aByte742 = buffer.readSignedByte();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = buffer.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.readUnsignedByte();
					originalModelColors = new int[i1];
					newModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						originalModelColors[i2] = buffer.readUnsignedWord();
						newModelColors[i2] = buffer.readUnsignedWord();
					}
				} else if (opcode == 41) {
					int length = buffer.readUnsignedByte();
					originalTextureFaces = new int[length];
					newTextureFaces = new int[length];
					for (int textureId = 0; textureId < length; textureId++) {
						originalTextureFaces[textureId] = buffer.readUnsignedWord();
						newTextureFaces[textureId] = buffer.readUnsignedWord();
					}
				} else if (opcode == 60)
					anInt746 = buffer.readUnsignedWord();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.readUnsignedWord();
				else if (opcode == 66)
					anInt772 = buffer.readUnsignedWord();
				else if (opcode == 67)
					anInt740 = buffer.readUnsignedWord();
				else if (opcode == 68)
					anInt758 = buffer.readUnsignedWord();
				else if (opcode == 69)
					anInt768 = buffer.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = buffer.readSignedWord();
				else if (opcode == 71)
					anInt745 = buffer.readSignedWord();
				else if (opcode == 72)
					anInt783 = buffer.readSignedWord();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.readUnsignedByte();
				}
				continue label0;
			} while (opcode != 77);
				anInt774 = buffer.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
				anInt749 = buffer.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			hasActions = anIntArray773 != null && (anIntArray776 == null || anIntArray776[0] == 10);
			if (actions != null)
				hasActions = true;
		}
		if (aBoolean766) {
			solidObject = false;
			aBoolean757 = false;
		}
		if (anInt760 == -1)
			anInt760 = solidObject ? 1 : 0;
    }
	
	public void method574(OnDemandFetcher class42_sub1) {
		if (anIntArray773 == null)
			return;
		for (int j = 0; j < anIntArray773.length; j++)
			class42_sub1.method560(anIntArray773[j] & 0xffff, 0);
	}

	public boolean method577(int i) {
		if (anIntArray776 == null) {
			if (anIntArray773 == null)
				return true;
			if (i != 10)
				return true;
			boolean flag1 = true;
			for (int k = 0; k < anIntArray773.length; k++)
				flag1 &= Model.method463(anIntArray773[k] & 0xffff);

			return flag1;
		}
		for (int j = 0; j < anIntArray776.length; j++)
			if (anIntArray776[j] == i)
				return Model.method463(anIntArray773[j] & 0xffff);

		return true;
	}

	public Model method578(int i, int j, int k, int l, int i1, int j1, int k1) {
		Model model = method581(i, k1, j);
		if (model == null)
			return null;
		if (aBoolean762 || aBoolean769)
			model = new Model(aBoolean762, aBoolean769, model);
		if (aBoolean762) {
			int l1 = (k + l + i1 + j1) / 4;
			for (int i2 = 0; i2 < model.anInt1626; i2++) {
				int j2 = model.anIntArray1627[i2];
				int k2 = model.anIntArray1629[i2];
				int l2 = k + ((l - k) * (j2 + 64)) / 128;
				int i3 = j1 + ((i1 - j1) * (j2 + 64)) / 128;
				int j3 = l2 + ((i3 - l2) * (k2 + 64)) / 128;
				model.anIntArray1628[i2] += j3 - l1;
			}

			model.method467();
		}
		return model;
	}

	public boolean method579() {
		if (anIntArray773 == null)
			return true;
		boolean flag1 = true;
		for (int i = 0; i < anIntArray773.length; i++)
			flag1 &= Model.method463(anIntArray773[i] & 0xffff);
		return flag1;
	}

	public ObjectDef method580() {
		int i = -1;
		if (anInt774 != -1) {
			VarBit varBit = VarBit.cache[anInt774];
			int j = varBit.anInt648;
			int k = varBit.anInt649;
			int l = varBit.anInt650;
			int i1 = Client.anIntArray1232[l - k];
			i = clientInstance.variousSettings[j] >> k & i1;
		} else if (anInt749 != -1)
			i = clientInstance.variousSettings[anInt749];
		if (i < 0 || i >= childrenIDs.length || childrenIDs[i] == -1)
			return null;
		else
			return forID(childrenIDs[i]);
	}

	private Model method581(int j, int k, int l) {
		Model model = null;
		long l1;
		if (anIntArray776 == null) {
			if (j != 10)
				return null;
			l1 = (type << 6) + l + ((long) (k + 1) << 32);
			Model model_1 = (Model) mruNodes2.insertFromCache(l1);
			if (model_1 != null)
				return model_1;
			if (anIntArray773 == null)
				return null;
			boolean flag1 = aBoolean751 ^ (l > 3);
			int k1 = anIntArray773.length;
			for (int i2 = 0; i2 < k1; i2++) {
				int l2 = anIntArray773[i2];
				if (flag1)
					l2 += 0x10000;
				model = (Model) mruNodes1.insertFromCache(l2);
				if (model == null) {
					model = Model.method462(l2 & 0xffff);
					if (model == null)
						return null;
					if (flag1)
						model.method477();
					mruNodes1.removeFromCache(model, l2);
				}
				if (k1 > 1)
					aModelArray741s[i2] = model;
				setTexture(l2, aModelArray741s[i2]);
			}

			if (k1 > 1)
				model = new Model(k1, aModelArray741s);
		} else {
			int i1 = -1;
			for (int j1 = 0; j1 < anIntArray776.length; j1++) {
				if (anIntArray776[j1] != j)
					continue;
				i1 = j1;
				break;
			}

			if (i1 == -1)
				return null;
			l1 = (type << 8) + (i1 << 3) + l + ((long) (k + 1) << 32);
			Model model_2 = (Model) mruNodes2.insertFromCache(l1);
			if (model_2 != null)
				return model_2;
			int j2 = anIntArray773[i1];
			boolean flag3 = aBoolean751 ^ (l > 3);
			if (flag3)
				j2 += 0x10000;
			model = (Model) mruNodes1.insertFromCache(j2);
			if (model == null) {
				model = Model.method462(j2 & 0xffff);
				if (model == null)
					return null;
				if (flag3)
					model.method477();
				mruNodes1.removeFromCache(model, j2);
			}
			setTexture(j2, model);
		}
		boolean flag;
		flag = anInt748 != 128 || anInt772 != 128 || anInt740 != 128;
		boolean flag2;
		flag2 = anInt738 != 0 || anInt745 != 0 || anInt783 != 0;
		//Model model_3 = new Model(originalModelColors == null || originalTextureFaces == null, Class36.method532(k), l == 0 && k == -1 && !flag && !flag2, model);
		Model model_3 = new Model(modifiedModelColors == null
				&& modifiedTexture == null, Class36.method532(k), l == 0
				&& k == -1 && !flag && !flag2, model);
		if (k != -1) {
			model_3.method469();
			model_3.method470(k);
			model_3.anIntArrayArray1658 = null;
			model_3.anIntArrayArray1657 = null;
		}
		while (l-- > 0)
			model_3.method473();
		if (modifiedModelColors != null) {
			for (int k2 = 0; k2 < modifiedModelColors.length; k2++)
				model_3.method476(modifiedModelColors[k2],
						originalModelColors[k2], ItemDef.getTexture(-1));

		}
		if (modifiedTexture != null) {
			for (int k2 = 0; k2 < modifiedTexture.length; k2++)
				model_3.method476(modifiedTexture[k2], originalTexture[k2],
						ItemDef.getTexture(-1));

		}
		if (flag)
			model_3.method478(anInt748, anInt740, anInt772);
		if (flag2)
			model_3.method475(anInt738, anInt745, anInt783);
		model_3.method479(aByte737 + 64, 768 + aByte742, -50, -10, -50, !aBoolean769);
		if (anInt760 == 1)
			model_3.anInt1654 = model_3.modelHeight;
		mruNodes2.removeFromCache(model_3, l1);
		return model_3;
	}
	
	private void setDefaults() {
		anIntArray773 = null;
		anIntArray776 = null;
		name = null;
		description = null;
		//originalModelColors = null;
		newModelColors = null;
		
		modifiedModelColors = null;
		originalModelColors = null;
		
		originalTextureFaces = null;
		newTextureFaces = null;
		aBoolean767 = true;
		aBoolean757 = true;
		anInt744 = 1;
		anInt761 = 1;
		solidObject = true;
		hasActions = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}
	
	public static void nullLoader() {
		mruNodes1 = null;
		mruNodes2 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	private ObjectDef() {
		type = -1;
	}

	public boolean aBoolean736;
	public byte aByte737;
	public int anInt738;
	public String name;
	public int anInt740;
	public static final Model[] aModelArray741s = new Model[4];
	public byte aByte742;
	public int anInt744;
	public int anInt745;
	public int anInt746;
	public int[] newModelColors;
	public int anInt748;
	public int anInt749;
	public boolean aBoolean751;
	public static boolean lowMem;
	public static Stream stream;
	public int type;
	public static int[] streamIndices;
	public boolean aBoolean757;
	public int anInt758;
	public int childrenIDs[];
	public int anInt760;
	public int anInt761;
	public boolean aBoolean762;
	public boolean aBoolean764;
	public static Client clientInstance;
	public boolean aBoolean766;
	public boolean aBoolean767;
	private short[] modifiedTextureFace;
	private short[] originalTextureFace;
	private int[] modifiedModelColors;
	private short[] originalTexture;
	private short[] modifiedTexture;
	/**
	 * aBoolean767
	 */
	public boolean solidObject;
	public int anInt768;
	public boolean aBoolean769;
	public static int cacheIndex;
	public int anInt772;
	public int[] anIntArray773;
	public int anInt774;
	public int anInt775;
	public int[] anIntArray776;
	public String description;
	public boolean hasActions;
	public boolean aBoolean779;
	public static MRUNodes mruNodes2 = new MRUNodes(30);
	public int anInt781;
	public static ObjectDef[] cache;
	public int anInt783;
	public int[] originalModelColors;
	public int[] originalTextureFaces, newTextureFaces;
	public static MRUNodes mruNodes1 = new MRUNodes(500);
	public String actions[];

}