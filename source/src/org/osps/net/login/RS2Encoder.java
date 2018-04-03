package org.osps.net.login;

import org.osps.net.Packet;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class RS2Encoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object object) throws Exception {
		return ((Packet) object).getPayload();
	}

}
