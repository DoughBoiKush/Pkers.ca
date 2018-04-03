package com.osps.client.cache.graphics;
import java.awt.Color;

import com.osps.client.Sprite;
import com.osps.client.Stream;
import com.osps.client.cache.StreamLoader;
import com.osps.client.draw.DrawingArea;
import com.osps.client.draw.rsDrawingArea;

public class RSFont extends DrawingArea {

	private boolean strikeThrough;
	public int baseCharacterHeight = 0;
	public int anInt4142;
	public int anInt4144;
	public int[] characterDrawYOffsets;
	public int[] characterHeights;
	public int[] characterDrawXOffsets;
	public int[] characterWidths;
	public int[] iconWidths;
	public byte[] aByteArray4151;
	public byte[][] fontPixels;
	public int[] characterScreenWidths;
	public static Sprite[] chatImages;
	public static Sprite[] clanImages;
	public static String aRSString_4135;
	public static String startTransparency;
	public static String startDefaultShadow;
	public static String endShadow = "/shad";
	public static String endEffect;
	public static String aRSString_4143;
	public static String endStrikethrough = "/str";
	public static String aRSString_4147;
	public static String startColor;
	public static String lineBreak;
	public static String startStrikethrough;
	public static String endColor;
	public static String startImage;
	public static String startClanImage;
	public static String endUnderline;
	public static String defaultStrikethrough;
	public static String startShadow;
	public static String startEffect;
	public static String aRSString_4162;
	public static String aRSString_4163;
	public static String endTransparency;
	public static String aRSString_4165;
	public static String startUnderline;
	public static String startDefaultUnderline;
	public static String aRSString_4169;
	public static String[] splitTextStrings;
	public static int defaultColor;
	public static int textShadowColor;
	public static int strikethroughColor;
	public static int defaultTransparency;
	public static int anInt4175;
	public static int underlineColor;
	public static int defaultShadow;
	public static int anInt4178;
	public static int transparency;
	public static int textColor;

	private void drawCharacter(byte charPixels[], int x, int y, int width, int height, int color) {
		int offset = x + y * DrawingArea.width;
		int k1 = DrawingArea.width - width;
		int l1 = 0;
		int pixel = 0;
		if (y < DrawingArea.topY) {
			int offsetY = DrawingArea.topY - y;
			height -= offsetY;
			y = DrawingArea.topY;
			pixel += offsetY * width;
			offset += offsetY * DrawingArea.width;
		}
		if (y + height >= DrawingArea.bottomY) {
			height -= ((y + height) - DrawingArea.bottomY);
		}
		if (x < DrawingArea.topX) {
			int offsetX = DrawingArea.topX - x;
			width -= offsetX;
			x = DrawingArea.topX;
			pixel += offsetX;
			offset += offsetX;
			l1 += offsetX;
			k1 += offsetX;
		}
		if (x + width >= DrawingArea.bottomX) {
			int l2 = ((x + width) - DrawingArea.bottomX);
			width -= l2;
			l1 += l2;
			k1 += l2;
		}
		if (!(width <= 0 || height <= 0)) {
			createPixels(DrawingArea.pixels, charPixels, color, pixel, offset, width, height, k1, l1);
		}
	}

	private void createPixels(int drawingAreaPixels[], byte charPixels[], int color, int pixel, int offset, int width, int height, int unknown1, int unknown2) {
		int l1 = -(width >> 2);
		width = -(width & 3);
		for (int i2 = -height; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
			}
			for (int k2 = width; k2 < 0; k2++) {
				if (charPixels[pixel++] != 0) {
					drawingAreaPixels[offset++] = color;
				} else {
					offset++;
				}
			}
			offset += unknown1;
			pixel += unknown2;
		}
	}

	public void drawShadowedString(String string, int x, int y, int color, boolean shadow) {
		strikeThrough = false;
		int lineX = x;
		if (string == null) {
			return;
		}
		y -= baseCharacterHeight;
		for (int index = 0; index < string.length(); index++) {
			if (string.charAt(index) == '@' && index + 4 < string.length() && string.charAt(index + 4) == '@') {
				int textColor = getColorByName(string.substring(index + 1, index + 4));
				if (textColor != -1) {
					color = textColor;
				}
				index += 4;
			} else {
				char c = string.charAt(index);
				if (c != ' ') {
					if (shadow) {
						drawCharacter(fontPixels[c], x + characterDrawXOffsets[c] + 1, y + characterDrawYOffsets[c] + 1, characterWidths[c], characterHeights[c], 0);
					}
					drawCharacter(fontPixels[c], x + characterDrawXOffsets[c], y + characterDrawYOffsets[c], characterWidths[c], characterHeights[c], color);
				}
				x += characterScreenWidths[c];
			}
		}
		if (strikeThrough) {
			DrawingArea.drawHorizontalLine(lineX, y + (int) ((double) baseCharacterHeight * 0.69999999999999996D), x - lineX, 0x800000);
		}
	}

	public RSFont(boolean TypeFont, String s, StreamLoader archive) {
		fontPixels = new byte[256][];
		characterWidths = new int[256];
		characterHeights = new int[256];
		characterDrawXOffsets = new int[256];
		characterDrawYOffsets = new int[256];
		characterScreenWidths = new int[256];
		strikeThrough = false;
		Stream stream = new Stream(archive.getDataForName(s + ".dat"));
		Stream stream_1 = new Stream(archive.getDataForName("index.dat"));
		stream_1.currentOffset = stream.readUnsignedWord() + 4;
		int k = stream_1.readUnsignedByte();
		if (k > 0) {
			stream_1.currentOffset += 3 * (k - 1);
		}
		for (int l = 0; l < 256; l++) {
			characterDrawXOffsets[l] = stream_1.readUnsignedByte();
			characterDrawYOffsets[l] = stream_1.readUnsignedByte();
			int i1 = characterWidths[l] = stream_1.readUnsignedWord();
			int j1 = characterHeights[l] = stream_1.readUnsignedWord();
			int k1 = stream_1.readUnsignedByte();
			int l1 = i1 * j1;
			fontPixels[l] = new byte[l1];
			if (k1 == 0) {
				for (int i2 = 0; i2 < l1; i2++) {
					fontPixels[l][i2] = stream.readSignedByte();
				}

			} else if (k1 == 1) {
				for (int j2 = 0; j2 < i1; j2++) {
					for (int l2 = 0; l2 < j1; l2++) {
						fontPixels[l][j2 + l2 * i1] = stream.readSignedByte();
					}

				}

			}
			if (j1 > baseCharacterHeight && l < 128) {
				baseCharacterHeight = j1;
			}
			characterDrawXOffsets[l] = 1;
			characterScreenWidths[l] = i1 + 2;
			int k2 = 0;
			for (int i3 = j1 / 7; i3 < j1; i3++) {
				k2 += fontPixels[l][i3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
				characterDrawXOffsets[l] = 0;
			}
			k2 = 0;
			for (int j3 = j1 / 7; j3 < j1; j3++) {
				k2 += fontPixels[l][(i1 - 1) + j3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
			}
		}

		if (TypeFont) {
			characterScreenWidths[32] = characterScreenWidths[73];
		} else {
			characterScreenWidths[32] = characterScreenWidths[105];
		}
	}

	public void drawStringMoveY(String string, int drawX, int drawY, int color, int shadow, int randomMod, int randomMod2) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			double d = 7.0 - (double) randomMod2 / 8.0;
			if (d < 0.0) {
				d = 0.0;
			}
			int[] yOffset = new int[string.length()];
			for (int index = 0; index < string.length(); index++) {
				yOffset[index] = (int) (Math.sin((double) index / 1.5 + (double) randomMod) * d);
			}
			drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, null, yOffset);
		}
	}

	public int getCharacterWidth(int i) {
		return characterScreenWidths[i & 0xff];
	}

	public void setTrans(int i, int j, int k) {
		textShadowColor = defaultShadow = i;
		textColor = defaultColor = j;
		transparency = defaultTransparency = k;
	}

	public void drawCenteredString(String s, int i, int j) {
		if (s != null)
			drawBasicString(s, i - getTextWidth(s) / 2, j);
	}

	private int getColorByName(String color) {
		if (color.equals("pt1")) {
			return 0x005eff;
		}
		if (color.equals("pt2")) {
			return 0x336600;
		}
		if (color.equals("pt3")) {
			return 0xA300CC;
		}
		if (color.equals("pt4")) {
			return 0xE6E600;
		}
		if (color.equals("pt5")) {
			return 0xB80000;
		}
		if (color.equals("pt6")) {
			return 0xCC5200;
		}
		if (color.equals("pt7")) {
			return 0x3D991F;
		}

		if (color.endsWith("inf")) {
			return 0xff9933;
		}

		if (color.equals("ceo")) {
			return 0x1F9100;
		}
		if (color.equals("dev")) {
			return 0xBB2AF5;
		}
		if (color.equals("war")) {
			return 0xF752CB;
		}
		if (color.equals("369")) {
			return 0x336699;
		}
		if (color.equals("def")) {
			return 0xB8934A;
		}
		if (color.equals("red")) {
			return 0xff0000;
		}
		if (color.equals("gre")) {
			return 65280;
		}
		if (color.equals("blu")) {
			return 255;
		}
		if (color.equals("yel")) {
			return 0xffff00;
		}
		if (color.equals("cya")) {
			return 65535;
		}
		if (color.equals("mag")) {
			return 0xff00ff;
		}
		if (color.equals("whi")) {
			return 0xffffff;
		}
		if (color.equals("gry")) {
			return 0x475154;
		}
		if (color.equals("bla")) {
			return 0;
		}
		if (color.equals("lre")) {
			return 0xff9040;
		}
		if (color.equals("dre")) {
			return 0x800000;
		}
		if (color.equals("dbl")) {
			return 128;
		}
		if (color.equals("or1")) {
			return 0xffb000;
		}
		if (color.equals("or2")) {
			return 0xff7000;
		}
		if (color.equals("or3")) {
			return 0xff3000;
		}
		if (color.equals("gr1")) {
			return 0xc0ff00;
		}
		if (color.equals("gr2")) {
			return 0x80ff00;
		}
		if (color.equals("gr3")) {
			return 0x40ff00;
		}
		if (color.equals("str")) {
			strikeThrough = true;
		}
		if (color.equals("end")) {
			strikeThrough = false;
		}
		
		
		
		if (color.equals("mbl")) {
			return 0x359BBD;
		}
		
		
		
		return -1;
	}

	public void setDefaultTextEffectValues(int color, int shadow, int trans) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = trans;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public static int method1014(byte[][] is, byte[][] is_27_, int[] is_28_, int[] is_29_, int[] is_30_, int i, int i_31_) {
		int i_32_ = is_28_[i];
		int i_33_ = i_32_ + is_30_[i];
		int i_34_ = is_28_[i_31_];
		int i_35_ = i_34_ + is_30_[i_31_];
		int i_36_ = i_32_;
		if (i_34_ > i_32_) {
			i_36_ = i_34_;
		}
		int i_37_ = i_33_;
		if (i_35_ < i_33_) {
			i_37_ = i_35_;
		}
		int i_38_ = is_29_[i];
		if (is_29_[i_31_] < i_38_) {
			i_38_ = is_29_[i_31_];
		}
		byte[] is_39_ = is_27_[i];
		byte[] is_40_ = is[i_31_];
		int i_41_ = i_36_ - i_32_;
		int i_42_ = i_36_ - i_34_;
		for (int i_43_ = i_36_; i_43_ < i_37_; i_43_++) {
			int i_44_ = is_39_[i_41_++] + is_40_[i_42_++];
			if (i_44_ < i_38_) {
				i_38_ = i_44_;
			}
		}
		return -i_38_;
	}

	public void drawCenteredStringMoveXY(String string, int drawX, int drawY, int color, int shadow, int randomMod) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			int[] xMods = new int[string.length()];
			int[] yMods = new int[string.length()];
			for (int index = 0; index < string.length(); index++) {
				xMods[index] = (int) (Math.sin((double) index / 5.0 + (double) randomMod / 5.0) * 5.0);
				yMods[index] = (int) (Math.sin((double) index / 3.0 + (double) randomMod / 5.0) * 5.0);
			}
			drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, xMods, yMods);
		}
	}

	public void drawCenteredStringMoveY(String class100, int drawX, int drawY, int color, int shadow, int i_54_) {
		if (class100 != null) {
			setColorAndShadow(color, shadow);
			int[] yOffset = new int[class100.length()];
			for (int index = 0; index < class100.length(); index++) {
				yOffset[index] = (int) (Math.sin((double) index / 2.0 + (double) i_54_ / 5.0) * 5.0);
			}
			drawBaseStringMoveXY(class100, drawX - getTextWidth(class100) / 2, drawY, null, yOffset);
		}
	}

	public static void unpackImages(Sprite[] icons, Sprite[] clan) {
		chatImages = icons;
		clanImages = clan;
	}

	public void drawBasicString(String string, int drawX, int drawY) {
		drawY -= baseCharacterHeight;
		int startIndex = -1;
		string = handleOldSyntax(string);
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}
			if (character == 60 && isSpecial(string, currentCharacter)) {
				startIndex = currentCharacter;
			} else {
				if (character == 62 && startIndex != -1 && isSpecial(string, startIndex + 1)) {
					String effectString = string.substring(startIndex + 1, currentCharacter);
					startIndex = -1;
					if (effectString.equals(startEffect)) {
						character = 60;
					} else if (effectString.equals(endEffect)) {
						character = 62;
					} else if (effectString.equals(aRSString_4135)) {
						character = 160;
					} else if (effectString.equals(aRSString_4162)) {
						character = 173;
					} else if (effectString.equals(aRSString_4165)) {
						character = 215;
					} else if (effectString.equals(aRSString_4147)) {
						character = 128;
					} else if (effectString.equals(aRSString_4163)) {
						character = 169;
					} else if (effectString.equals(aRSString_4169)) {
						character = 174;
					} else {
						if (effectString.startsWith(startImage)) {
							try {
								int imageId = Integer.valueOf(effectString.substring(4));
								Sprite icon = chatImages[imageId];
								icon.drawSprite(drawX, drawY);
								drawX += icon.myWidth + icon.drawOffsetX;
							} catch (Exception exception) {
								/* empty */
							}
						} else if (effectString.startsWith(startClanImage)) {
							try {
								int imageId = Integer.valueOf(effectString.substring(5));
								Sprite icon = clanImages[imageId];
								icon.drawSprite(drawX, drawY + 2);
								drawX += 11;
							} catch (Exception exception) {
								/* empty */
							}
						} else {
							setTextEffects(effectString);
						}
						continue;
					}
				}
				if (startIndex == -1) {
					int width = characterWidths[character];
					int height = characterHeights[character];
					if (character != 32) {
						if (transparency == 256) {
							if (textShadowColor != -1) {
								drawCharacter(character, drawX + characterDrawXOffsets[character] + 1, drawY + characterDrawYOffsets[character] + 1, width, height, textShadowColor, true);
							}
							drawCharacter(character, drawX + characterDrawXOffsets[character], drawY + characterDrawYOffsets[character], width, height, textColor, false);
						} else {
							if (textShadowColor != -1) {
								drawTransparentCharacter(character, drawX + characterDrawXOffsets[character] + 1, drawY + characterDrawYOffsets[character] + 1, width, height, textShadowColor, transparency, true);
							}
							drawTransparentCharacter(character, drawX + characterDrawXOffsets[character], drawY + characterDrawYOffsets[character], width, height, textColor, transparency, false);
						}
					} else if (anInt4178 > 0) {
						anInt4175 += anInt4178;
						drawX += anInt4175 >> 8;
						anInt4175 &= 0xff;
					}
					int lineWidth = characterScreenWidths[character];
					if (strikethroughColor != -1) {
						rsDrawingArea.drawHorizontalLine(drawX, drawY + (int) ((double) baseCharacterHeight * 0.69999999999999996D), lineWidth, strikethroughColor);
					}
					if (underlineColor != -1) {
						rsDrawingArea.drawHorizontalLine(drawX, drawY + baseCharacterHeight, lineWidth, underlineColor);
					}
					drawX += lineWidth;
				}
			}
		}
	}

	public void drawRAString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX - getTextWidth(string), drawY);
		}
	}
	
	private boolean isSpecial(String string, int currentCharacter) {
		String substring = string.substring(currentCharacter);
		if (substring.length() < 6) {
			return false;
		}
		if (substring.substring(0, 5).equals("<col=") || substring.substring(0, 5).equals("</col")) {
			return true;
		}
		if (substring.substring(0, 5).equals("<img=") || substring.substring(0, 5).equals("</img")) {
			return true;
		}
		if (substring.substring(0, 5).equals("<clan=") || substring.substring(0, 5).equals("</clan")) {
			return true;
		}
		if (substring.substring(0, 5).equals("<shad=") || substring.substring(0, 5).equals("</shad")) {
			return true;
		}
		if (substring.substring(0, 5).equals("<str=") || substring.substring(0, 5).equals("</str")) {
			return true;
		}
		if (substring.substring(0, 6).equals("<trans=") || substring.substring(0, 6).equals("</trans")) {
			return true;
		}
		return true;
	}
	
	public void drawBaseStringMoveXY(String string, int drawX, int drawY, int[] xModifier, int[] yModifier) {
		drawY -= baseCharacterHeight;
		int startIndex = -1;
		int modifierOffset = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character == 60 && isSpecial(string, currentCharacter)) {
				startIndex = currentCharacter;
			} else {
				if (character == 62 && startIndex != -1 && isSpecial(string, startIndex + 1)) {
					String effectString = string.substring(startIndex + 1, currentCharacter);
					startIndex = -1;
					if (effectString.equals(startEffect)) {
						character = 60;
					} else if (effectString.equals(endEffect)) {
						character = 62;
					} else if (effectString.equals(aRSString_4135)) {
						character = 160;
					} else if (effectString.equals(aRSString_4162)) {
						character = 173;
					} else if (effectString.equals(aRSString_4165)) {
						character = 215;
					} else if (effectString.equals(aRSString_4147)) {
						character = 128;
					} else if (effectString.equals(aRSString_4163)) {
						character = 169;
					} else if (effectString.equals(aRSString_4169)) {
						character = 174;
					} else {
						if (effectString.startsWith(startImage)) {
							try {
								int xModI;
								if (xModifier != null) {
									xModI = xModifier[modifierOffset];
								} else {
									xModI = 0;
								}
								int yMod;
								if (yModifier != null) {
									yMod = yModifier[modifierOffset];
								} else {
									yMod = 0;
								}
								modifierOffset++;
								int iconId = Integer.valueOf(effectString.substring(4));
								Sprite class92 = chatImages[iconId];
								int iconOffsetY = class92.anInt1445;
								if (transparency == 256) {
									class92.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod));
								} else {
									class92.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod), transparency);
								}
								drawX += class92.maxWidth;
							} catch (Exception exception) {
								/* empty */
							}
						} else {
							setTextEffects(effectString);
						}
						continue;
					}
				}
				if (startIndex == -1) {
					int width = characterWidths[character];
					int height = characterHeights[character];
					int xOff;
					if (xModifier != null) {
						xOff = xModifier[modifierOffset];
					} else {
						xOff = 0;
					}
					int yOff;
					if (yModifier != null) {
						yOff = yModifier[modifierOffset];
					} else {
						yOff = 0;
					}
					modifierOffset++;
					if (character != 32) {
						if (transparency == 256) {
							if (textShadowColor != -1) {
								drawCharacter(character, (drawX + characterDrawXOffsets[character] + 1 + xOff), (drawY + characterDrawYOffsets[character] + 1 + yOff), width, height, textShadowColor, true);
							}
							drawCharacter(character, drawX + characterDrawXOffsets[character] + xOff, drawY + characterDrawYOffsets[character] + yOff, width, height, textColor, false);
						} else {
							if (textShadowColor != -1) {
								drawTransparentCharacter(character, (drawX + characterDrawXOffsets[character] + 1 + xOff), (drawY + characterDrawYOffsets[character] + 1 + yOff), width, height, textShadowColor, transparency, true);
							}
							drawTransparentCharacter(character, drawX + characterDrawXOffsets[character] + xOff, drawY + characterDrawYOffsets[character] + yOff, width, height, textColor, transparency, false);
						}
					} else if (anInt4178 > 0) {
						anInt4175 += anInt4178;
						drawX += anInt4175 >> 8;
						anInt4175 &= 0xff;
					}
					int i_109_ = characterScreenWidths[character];
					if (strikethroughColor != -1) {
						rsDrawingArea.drawHorizontalLine(drawX, drawY + (int) ((double) baseCharacterHeight * 0.7), i_109_, strikethroughColor);
					}
					if (underlineColor != -1) {
						rsDrawingArea.drawHorizontalLine(drawX, drawY + baseCharacterHeight, i_109_, underlineColor);
					}
					drawX += i_109_;
				}
			}
		}
	}

	public void setTextEffects(String string) {
		do {
			try {
				if (string.startsWith(startColor)) {
					String color = string.substring(4);
					textColor = color.length() < 6 ? Color.decode(color).getRGB() : Integer.parseInt(color, 16);
				} else if (string.equals(endColor)) {
					textColor = defaultColor;
				} else if (string.startsWith(startTransparency)) {
					transparency = Integer.valueOf(string.substring(6));
				} else if (string.equals(endTransparency)) {
					transparency = defaultTransparency;
				} else if (string.startsWith(startStrikethrough)) {
					strikethroughColor = Integer.valueOf(string.substring(4));
				} else if (string.equals(defaultStrikethrough)) {
					strikethroughColor = 8388608;
				} else if (string.equals(endStrikethrough)) {
					strikethroughColor = -1;
				} else if (string.startsWith(startUnderline)) {
					underlineColor = Integer.valueOf(string.substring(2));
				} else if (string.equals(startDefaultUnderline)) {
					underlineColor = 0;
				} else if (string.equals(endUnderline)) {
					underlineColor = -1;
				} else if (string.startsWith(startShadow)) {
					textShadowColor = Integer.valueOf(string.substring(5));
				} else if (string.equals(startDefaultShadow)) {
					textShadowColor = 0;
				} else if (string.equals(endShadow)) {
					textShadowColor = defaultShadow;
				} else {
					if (!string.equals(lineBreak)) {
						break;
					}
					setDefaultTextEffectValues(defaultColor, defaultShadow, defaultTransparency);
				}
			} catch (Exception exception) {
				break;
			}
			break;
		} while (false);
	}

	public void setColorAndShadow(int color, int shadow) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = 256;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public int getRealTextWidth(String string) {
		if (string == null) {
			return 0;
		}
		return string.length();
	}
	
	public int getTextWidth(String string) {
		if (string == null) {
			return 0;
		}
		int startIndex = -1;
		int finalWidth = 0;
		string = handleOldSyntax(string);
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}
			if (character == 60 && isSpecial(string, currentCharacter)) {
				startIndex = currentCharacter;
			} else {
				if (character == 62 && startIndex != -1 && isSpecial(string, startIndex + 1)) {
					String effectString = string.substring(startIndex + 1, currentCharacter);
					startIndex = -1;
					if (effectString.equals(startEffect)) {
						character = 60;
					} else if (effectString.equals(endEffect)) {
						character = 62;
					} else if (effectString.equals(aRSString_4135)) {
						character = 160;
					} else if (effectString.equals(aRSString_4162)) {
						character = 173;
					} else if (effectString.equals(aRSString_4165)) {
						character = 215;
					} else if (effectString.equals(aRSString_4147)) {
						character = 128;
					} else if (effectString.equals(aRSString_4163)) {
						character = 169;
					} else if (effectString.equals(aRSString_4169)) {
						character = 174;
					} else {
						if (effectString.startsWith(startImage)) {
							try {
								int iconId = Integer.valueOf(effectString.substring(4));
								finalWidth += chatImages[iconId].maxWidth;
							} catch (Exception exception) {
								/* empty */
							}
						} else if (effectString.startsWith(startClanImage)) {
							try {
								int iconId = Integer.valueOf(effectString.substring(5));
								finalWidth += clanImages[iconId].maxWidth;
							} catch (Exception exception) {
								/* empty */
							}
						} else {
							setTextEffects(effectString);
						}
						continue;
					}
				}
				if (startIndex == -1) {
					finalWidth += characterScreenWidths[character];
				}
			}
		}
		return finalWidth;
	}

	public void drawBasicString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX, drawY);
		}
	}

	public static String handleOldSyntax(String text) {
		text = text.replaceAll("@gry@", "<col=475154>");
		text = text.replaceAll("@pt2@", "<col=336600>");
		text = text.replaceAll("@pt1@", "<col=005eff>");
		text = text.replaceAll("@pt7@", "<col=3D991F>");
		text = text.replaceAll("@pt6@", "<col=E68A00>");
		text = text.replaceAll("@pt3@", "<col=A300CC>");
		text = text.replaceAll("@pt4@", "<col=E6E600>");
		text = text.replaceAll("@pt5@", "<col=B80000>");
		text = text.replaceAll("@red@", "<col=ff0000>");
		text = text.replaceAll("@gre@", "<col=65280>");
		text = text.replaceAll("@blu@", "<col=255>");
		text = text.replaceAll("@yel@", "<col=ffff00>");
		text = text.replaceAll("@cya@", "<col=65535>");
		text = text.replaceAll("@mag@", "<col=ff00ff>");
		text = text.replaceAll("@whi@", "<col=ffffff>");
		text = text.replaceAll("@lre@", "<col=ff9040>");
		text = text.replaceAll("@dre@", "<col=800000>");
		text = text.replaceAll("@bla@", "<col=0>");
		text = text.replaceAll("@or1@", "<col=ffb000>");
		text = text.replaceAll("@or2@", "<col=ff7000>");
		text = text.replaceAll("@or3@", "<col=ff3000>");
		text = text.replaceAll("@gr1@", "<col=c0ff00>");
		text = text.replaceAll("@gr2@", "<col=80ff00>");
		text = text.replaceAll("@gr3@", "<col=40ff00>");
		text = text.replaceAll("@RED@", "<col=ffff00>");
		text = text.replaceAll("@GRE@", "<col=65280>");
		text = text.replaceAll("@BLU@", "<col=255>");
		text = text.replaceAll("@YEL@", "<col=ff0000>");
		text = text.replaceAll("@CYA@", "<col=65535>");
		text = text.replaceAll("@MAG@", "<col=ff00ff>");
		text = text.replaceAll("@WHI@", "<col=ffffff>");
		text = text.replaceAll("@LRE@", "<col=ff9040>");
		text = text.replaceAll("@DRE@", "<col=800000>");
		text = text.replaceAll("@BLA@", "<col=0>");
		text = text.replaceAll("@OR1@", "<col=ffb000>");
		text = text.replaceAll("@OR2@", "<col=ff7000>");
		text = text.replaceAll("@OR3@", "<col=ff3000>");
		text = text.replaceAll("@GR1@", "<col=c0ff00>");
		text = text.replaceAll("@GR2@", "<col=80ff00>");
		text = text.replaceAll("@GR3@", "<col=40ff00>");
		text = text.replaceAll("@ric@", "<col=ff8000>");
		text = text.replaceAll("@cr1@", "<img=0>");
		text = text.replaceAll("@cr2@", "<img=1>");
		text = text.replaceAll("@cr3@", "<img=2>");
		text = text.replaceAll("@cr4@", "<img=3>");
		text = text.replaceAll("@cr5@", "<img=4>");
		text = text.replaceAll("@cr6@", "<img=5>");
		text = text.replaceAll("@cr7@", "<img=6>");
		text = text.replaceAll("@cr8@", "<img=7>");
		text = text.replaceAll("@cr9@", "<img=8>");
		text = text.replaceAll("@cr10@", "<img=9>");
		text = text.replaceAll("@cr11@", "<img=10>");
		text = text.replaceAll("@cr12@", "<img=11>");
		text = text.replaceAll("@cr13@", "<img=12>");
		text = text.replaceAll("@cr14@", "<img=13>");
		text = text.replaceAll("@don@", "<img=3>");
		text = text.replaceAll("@mbl@", "<col=359BBD>");
		return text;
	}

	public void drawCenteredString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			string = handleOldSyntax(string);
			drawBasicString(string, drawX - getTextWidth(string) / 2, drawY);
		}
	}

	public static void nullLoader() {
		startEffect = null;
		endEffect = null;
		aRSString_4135 = null;
		aRSString_4162 = null;
		aRSString_4165 = null;
		aRSString_4147 = null;
		aRSString_4163 = null;
		aRSString_4169 = null;
		startImage = null;
		lineBreak = null;
		startColor = null;
		endColor = null;
		startTransparency = null;
		endTransparency = null;
		startUnderline = null;
		startDefaultUnderline = null;
		endUnderline = null;
		startShadow = null;
		startDefaultShadow = null;
		endShadow = null;
		startStrikethrough = null;
		defaultStrikethrough = null;
		endStrikethrough = null;
		aRSString_4143 = null;
		splitTextStrings = null;
	}

	public static void createTransparentCharacterPixels(int[] is, byte[] is_0_, int i, int i_1_, int i_2_, int i_3_, int i_4_, int i_5_, int i_6_, int i_7_) {
		i = ((i & 0xff00ff) * i_7_ & ~0xff00ff) + ((i & 0xff00) * i_7_ & 0xff0000) >> 8;
		i_7_ = 256 - i_7_;
		for (int i_8_ = -i_4_; i_8_ < 0; i_8_++) {
			for (int i_9_ = -i_3_; i_9_ < 0; i_9_++) {
				if (is_0_[i_1_++] != 0) {
					int i_10_ = is[i_2_];
					is[i_2_++] = ((((i_10_ & 0xff00ff) * i_7_ & ~0xff00ff) + ((i_10_ & 0xff00) * i_7_ & 0xff0000)) >> 8) + i;
				} else {
					i_2_++;
				}
			}
			i_2_ += i_5_;
			i_1_ += i_6_;
		}
	}

	public void drawTransparentCharacter(int i, int i_11_, int i_12_, int i_13_, int i_14_, int i_15_, int i_16_, boolean bool) {
		int i_17_ = i_11_ + i_12_ * DrawingArea.width;
		int i_18_ = DrawingArea.width - i_13_;
		int i_19_ = 0;
		int i_20_ = 0;
		if (i_12_ < DrawingArea.topY) {
			int i_21_ = DrawingArea.topY - i_12_;
			i_14_ -= i_21_;
			i_12_ = DrawingArea.topY;
			i_20_ += i_21_ * i_13_;
			i_17_ += i_21_ * DrawingArea.width;
		}
		if (i_12_ + i_14_ > DrawingArea.bottomY) {
			i_14_ -= i_12_ + i_14_ - DrawingArea.bottomY;
		}
		if (i_11_ < DrawingArea.topX) {
			int i_22_ = DrawingArea.topX - i_11_;
			i_13_ -= i_22_;
			i_11_ = DrawingArea.topX;
			i_20_ += i_22_;
			i_17_ += i_22_;
			i_19_ += i_22_;
			i_18_ += i_22_;
		}
		if (i_11_ + i_13_ > DrawingArea.bottomX) {
			int i_23_ = i_11_ + i_13_ - DrawingArea.bottomX;
			i_13_ -= i_23_;
			i_19_ += i_23_;
			i_18_ += i_23_;
		}
		if (i_13_ > 0 && i_14_ > 0) {
			createTransparentCharacterPixels(DrawingArea.pixels, fontPixels[i], i_15_, i_20_, i_17_, i_13_, i_14_, i_18_, i_19_, i_16_);
		}
	}

	public static void createCharacterPixels(int[] is, byte[] is_24_, int i, int i_25_, int i_26_, int i_27_, int i_28_, int i_29_, int i_30_) {
		int i_31_ = -(i_27_ >> 2);
		i_27_ = -(i_27_ & 0x3);
		for (int i_32_ = -i_28_; i_32_ < 0; i_32_++) {
			for (int i_33_ = i_31_; i_33_ < 0; i_33_++) {
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
			}
			for (int i_34_ = i_27_; i_34_ < 0; i_34_++) {
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
			}
			i_26_ += i_29_;
			i_25_ += i_30_;
		}
	}

	public void drawCharacter(int character, int i_35_, int i_36_, int i_37_, int i_38_, int i_39_, boolean bool) {
		int i_40_ = i_35_ + i_36_ * DrawingArea.width;
		int i_41_ = DrawingArea.width - i_37_;
		int i_42_ = 0;
		int i_43_ = 0;
		if (i_36_ < DrawingArea.topY) {
			int i_44_ = DrawingArea.topY - i_36_;
			i_38_ -= i_44_;
			i_36_ = DrawingArea.topY;
			i_43_ += i_44_ * i_37_;
			i_40_ += i_44_ * DrawingArea.width;
		}
		if (i_36_ + i_38_ > DrawingArea.bottomY) {
			i_38_ -= i_36_ + i_38_ - DrawingArea.bottomY;
		}
		if (i_35_ < DrawingArea.topX) {
			int i_45_ = DrawingArea.topX - i_35_;
			i_37_ -= i_45_;
			i_35_ = DrawingArea.topX;
			i_43_ += i_45_;
			i_40_ += i_45_;
			i_42_ += i_45_;
			i_41_ += i_45_;
		}
		if (i_35_ + i_37_ > DrawingArea.bottomX) {
			int i_46_ = i_35_ + i_37_ - DrawingArea.bottomX;
			i_37_ -= i_46_;
			i_42_ += i_46_;
			i_41_ += i_46_;
		}
		if (i_37_ > 0 && i_38_ > 0) {
			createCharacterPixels(DrawingArea.pixels, fontPixels[character], i_39_, i_43_, i_40_, i_37_, i_38_, i_41_, i_42_);

		}
	}

	static {
		startTransparency = "trans=";
		startStrikethrough = "str=";
		startDefaultShadow = "shad";
		startColor = "col=";
		lineBreak = "br";
		defaultStrikethrough = "str";
		endUnderline = "/u";
		startImage = "img=";
		startClanImage = "clan=";
		startShadow = "shad=";
		startUnderline = "u=";
		endColor = "/col";
		startDefaultUnderline = "u";
		endTransparency = "/trans";
		aRSString_4143 = Integer.toString(100);
		aRSString_4135 = "nbsp";
		aRSString_4169 = "reg";
		aRSString_4165 = "times";
		aRSString_4162 = "shy";
		aRSString_4163 = "copy";
		endEffect = "gt";
		aRSString_4147 = "euro";
		startEffect = "lt";
		defaultTransparency = 256;
		defaultShadow = -1;
		anInt4175 = 0;
		textShadowColor = -1;
		textColor = 0;
		defaultColor = 0;
		strikethroughColor = -1;
		splitTextStrings = new String[100];
		underlineColor = -1;
		anInt4178 = 0;
		transparency = 256;
	}
}