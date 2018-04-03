package org.osps.net.login;

import org.osps.Config;
import org.osps.Connection;
import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.PlayerSave;
import org.osps.net.Packet;
import org.osps.net.PacketBuilder;
import org.osps.net.captcha.Captcha;
import org.osps.net.captcha.CaptchaManager;
import org.osps.net.captcha.ImageCaptcha;
import org.osps.net.captcha.SimpleCaptcha;
import org.osps.util.ISAACCipher;
import org.osps.util.Misc;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;

public class RS2LoginProtocol extends FrameDecoder {


	private static final BigInteger RSA_MODULUS = new BigInteger("94844922743570990739616980795536373564135244684332994396699394806304313983677199911247392205689519619817906590636675030851529228074823129939457129065320758893699561245741792154477449509004242259639569854789625150251242667721503547465731153022255722712756303409501787563518146574209684395176746831231357297687");

	private static final BigInteger RSA_EXPONENT = new BigInteger("14224495256825293620087817633387659120220414727593710452494901377102478022270827134712461937374647730948780137622531987705260856962440095582265348148115378250449635702230112836874382865708162601396936593908233381574443851910737517389024991231183675384448438623608271776110834975341567896989272543709001932553");
	
	private static final int CONNECTED = 0;
    private static final int LOGGING_IN = 1;
    private static final int  CAPTCHA = 2;
    private int state = CONNECTED;
    Captcha captcha;

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
            if(!channel.isConnected()) {
                    return null;
            }

            switch (state) {
            case CONNECTED:
                    if (buffer.readableBytes() < 2)
                            return null;
                    int request = buffer.readUnsignedByte();
                    if (request != 14) {
                            System.out.println("Invalid login request: " + request);
                            channel.close();
                            return null;
                    }
                    buffer.readUnsignedByte();                        
                    if(Config.CAPTCHA_REQUIRED && CaptchaManager.getSingleton().requiresCaptcha(((InetSocketAddress) channel
            				.getRemoteAddress()).getAddress().getHostAddress().toString())) {
                        state = CAPTCHA;

                    	if(Config.IMAGE_CAPTCHA) {
                			channel.write(new PacketBuilder().put((byte) 1).toPacket());
	                        captcha = new ImageCaptcha();
	                        byte[] captchaImg = (byte[])captcha.getKey(0);
	                        channel.write(new PacketBuilder().putInt(captchaImg.length).put(captchaImg).toPacket());
                    	} else {	   
                    		captcha = new SimpleCaptcha();
                    		channel.write(new PacketBuilder().put((byte) 2).toPacket());
                    		Packet p = new PacketBuilder().put((byte[])captcha.getKey(((Integer)channel.hashCode()))).toPacket();
                    		channel.write(p);
                    	}
            		} else {
            			channel.write(new PacketBuilder().put((byte) 0).toPacket());
                        state = LOGGING_IN;
            		}
                    
                    channel.write(new PacketBuilder().put((byte) 0).putLong(new SecureRandom().nextLong()).toPacket());
                    return null;
                    
            case CAPTCHA:
            	if(captcha != null && captcha.getExpectedLength() <= buffer.capacity()) {
            		
                	if(Config.IMAGE_CAPTCHA) {
                		String s = Misc.getRS2String(buffer);
                		if(!s.equals((String)captcha.getAnswer())) {
							channel.close();
							return false;
                		} else {
                			CaptchaManager.getSingleton().updateEntry(((InetSocketAddress) channel
                					.getRemoteAddress()).getAddress().getHostAddress().toString());
                            state = LOGGING_IN;
                		}
                	} else {	   
                		int answer = buffer.readByte();
                		if(answer != (int)captcha.getAnswer()) {
							channel.close();
							return false;
                		} else {
                		 CaptchaManager.getSingleton().updateEntry(((InetSocketAddress) channel
                					.getRemoteAddress()).getAddress().getHostAddress().toString());
                            state = LOGGING_IN;
                		}
                	}
            		
            	}
            	return null;

            case LOGGING_IN:					                    
					int loginType = -1, loginPacketSize = -1, loginEncryptPacketSize = -1;
					if(2 <= buffer.capacity()) {
						loginType = buffer.readByte() & 0xff; //should be 16 or 18
						
						if (loginType != 22 && loginType != 24) {
//							System.out.println("Invalid login type.");
							channel.close();
							return false;
						}
						loginPacketSize = buffer.readByte() & 0xff;
						loginEncryptPacketSize = loginPacketSize-(1+1+2);
						if(loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
							System.out.println("Zero or negative login size.");
							channel.close();
							return false;
						}
					}
					
					/**
					 * Read the magic id.
					 */
					if(loginPacketSize <= buffer.capacity()) {
						int magic = buffer.readByte() & 0xff;
						int version = buffer.readUnsignedShort();
						if(magic != 255) {
							System.out.println("Wrong magic id.");
							channel.close();
							return false;
						}
						if(version != 1) {
							//Dont Add Anything
						}
						@SuppressWarnings("unused")
						int lowMem = buffer.readByte() & 0xff;
						
						/**
						 * Pass the CRC keys.
						 */
						for(int i = 0; i < 9; i++) {
							buffer.readInt();
						}
						loginEncryptPacketSize--;
						if(loginEncryptPacketSize != (buffer.readByte() & 0xff)) {
							System.out.println("Encrypted size mismatch.");
							channel.close();
							return false;
						}
						
						ChannelBuffer rsaBuffer = buffer.readBytes(loginEncryptPacketSize);
						BigInteger bigInteger = new BigInteger(rsaBuffer.array());
						bigInteger = bigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
						rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
						if((rsaBuffer.readByte() & 0xff) != 15) {
							System.out.println("Encrypted id != 10.");
							sendReturnCode(channel, 23);
							channel.close();
							return false;
						}
                     final long clientHalf = rsaBuffer.readLong();
                     final long serverHalf = rsaBuffer.readLong();
                     
						
                     final String name = Misc.formatPlayerName(Misc.getRS2String(rsaBuffer));
                     final String pass = Misc.getRS2String(rsaBuffer);
                     long uid = rsaBuffer.readLong();
                     final String macAddress = Misc.getRS2String(rsaBuffer);
                     final int clientVersion = rsaBuffer.readInt();
                     final int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
                     final ISAACCipher inCipher = new ISAACCipher(isaacSeed);
                     for (int i = 0; i < isaacSeed.length; i++)
                             isaacSeed[i] += 50;
                     final ISAACCipher outCipher = new ISAACCipher(isaacSeed);
                     //final int version = buffer.readInt();
                     channel.getPipeline().replace("decoder", "decoder", new RS2Decoder(inCipher));
                     return login(channel, inCipher, outCipher, version, name, pass, macAddress, clientVersion, uid);
             }
             }
             return null;
             
     }

	private static Player login(Channel channel, ISAACCipher inCipher,
			ISAACCipher outCipher, int version, String name, String pass, String macAddress, int clientVersion, long uid) {
		int returnCode = 2;
		if (Connection.isIpBanned(((InetSocketAddress) channel
				.getRemoteAddress()).getAddress().getHostAddress().toString())) {
			returnCode = 4;
		}
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		}
		if (name.length() > 12) {
			returnCode = 8;
		}
		if (pass.length() < 4 || pass.length() == 0) {
			returnCode = 24;
		}
		Player cl = new Player(channel, -1);
		cl.playerName = name;
		cl.playerName2 = cl.playerName;
		cl.playerPass = pass;
		cl.setNameAsLong(Misc.playerNameToInt64(cl.playerName));
		cl.outStream.packetEncryption = outCipher;
		cl.saveCharacter = false;
		cl.isActive = true;
		cl.setMacAddress(macAddress);
		cl.setClientVersion(clientVersion);
		cl.setUniqueIdentifier(uid);
		cl.setUsernameHash(Misc.playerNameToInt64(name));
		
		if (Connection.isUidBanned(name, uid)) {
			returnCode = 26;
		}
		
		if (!cl.hasValidMac()) {
			returnCode = 4;
		}
		
		if (Connection.isNamedBanned(cl.playerName)) {
			returnCode = 4;
		}
		if (Connection.isMacBanned(macAddress)) {
			returnCode = 4;
		}
		if (Connection.isLocked(cl.playerName)) {
			returnCode = 25;
		}
		if (cl.getClientVersion() != Config.VERSION_OF_CLIENT) {
			returnCode = 6;
		}
		if(cl.playerName.endsWith(" ")){
			returnCode = 4;
		}
		if(cl.playerName.startsWith(" ")){
			returnCode = 4;
		}
		if (cl.playerName.contains("  ")) {
			returnCode = 4;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			returnCode = 5;
		}
		if (PlayerHandler.getPlayerCount() >= Config.MAX_PLAYERS) {
			returnCode = 7;
		}
		if (Server.UpdateServer) {
			returnCode = 14;
		}

		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
			if (load == 3) {
				returnCode = 3;
				cl.saveFile = false;
			} else {
				for (int i = 0; i < cl.playerEquipment.length; i++) {
					if (cl.playerEquipment[i] == 0) {
						cl.playerEquipment[i] = -1;
						cl.playerEquipmentN[i] = 0;
					}
				}
				if (!Server.playerHandler.newPlayerClient(cl)) {
					returnCode = 7;
					cl.saveFile = false;
				} else {
					cl.saveFile = true;
				}
			}
		}
		if (returnCode == 2) {
			cl.saveCharacter = true;
			final PacketBuilder bldr = new PacketBuilder();
			bldr.put((byte) 2);
			if (cl.getRights().isOwner()) {
				bldr.put((byte) 2);
			} else {
				bldr.put((byte) cl.getRights().getValue());
			}
			bldr.put((byte) 0);
			channel.write(bldr.toPacket());
		} else {
			System.out.println("returncode:" + returnCode);
			sendReturnCode(channel, returnCode);
			return null;
		}
		synchronized (PlayerHandler.lock) {
			cl.initialize();
			cl.initialized = true;
		}
		return cl;
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket())
				.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(final ChannelFuture arg0)
							throws Exception {
						arg0.getChannel().close();
					}
				});
	}

}