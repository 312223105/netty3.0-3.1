/*
 * Copyright (C) 2008  Trustin Heuiseung Lee
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, 5th Floor, Boston, MA 02110-1301 USA
 */
package net.gleamynode.netty.handler.codec.string;

import java.nio.charset.Charset;

import net.gleamynode.netty.array.ByteArray;
import net.gleamynode.netty.channel.ChannelEvent;
import net.gleamynode.netty.channel.ChannelHandlerContext;
import net.gleamynode.netty.channel.ChannelPipelineCoverage;
import net.gleamynode.netty.channel.ChannelUpstreamHandler;
import net.gleamynode.netty.channel.ChannelUtil;
import net.gleamynode.netty.channel.MessageEvent;

/**
 * @author The Netty Project (netty@googlegroups.com)
 * @author Trustin Lee (trustin@gmail.com)
 *
 * @version $Rev$, $Date$
 *
 */
@ChannelPipelineCoverage("all")
public class StringDecoder implements ChannelUpstreamHandler {

    private final String charsetName;

    public StringDecoder() {
        this(Charset.defaultCharset());
    }

    public StringDecoder(String charsetName) {
        this(Charset.forName(charsetName));
    }

    public StringDecoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        charsetName = charset.name();
    }

    public void handleUpstream(
            ChannelHandlerContext context, ChannelEvent element) throws Exception {
        if (!(element instanceof MessageEvent)) {
            context.sendUpstream(element);
            return;
        }

        MessageEvent e = (MessageEvent) element;
        if (!(e.getMessage() instanceof ByteArray)) {
            context.sendUpstream(element);
            return;
        }

        ByteArray src = (ByteArray) e.getMessage();
        byte[] dst = new byte[src.length()];
        src.get(src.firstIndex(), dst);
        ChannelUtil.fireMessageReceived(context, e.getChannel(), new String(dst, charsetName));
    }
}
