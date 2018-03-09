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
package net.gleamynode.netty.handler.codec.serialization;

import java.io.StreamCorruptedException;

import net.gleamynode.netty.array.ByteArray;
import net.gleamynode.netty.array.ByteArrayBuffer;
import net.gleamynode.netty.array.ByteArrayInputStream;
import net.gleamynode.netty.channel.Channel;
import net.gleamynode.netty.channel.ChannelEvent;
import net.gleamynode.netty.handler.codec.replay.ReplayingDecoder;
import net.gleamynode.netty.pipeline.PipeContext;

/**
 * @author The Netty Project (netty@googlegroups.com)
 * @author Trustin Lee (trustin@gmail.com)
 *
 * @version $Rev: 346 $, $Date: 2008-06-18 16:50:41 +0900 (Wed, 18 Jun 2008) $
 *
 */
public class ObjectDecoder extends ReplayingDecoder {

    private final int maxObjectSize;

    public ObjectDecoder() {
        this(1048576);
    }

    public ObjectDecoder(int maxObjectSize) {
        this.maxObjectSize = maxObjectSize;
    }

    @Override
    protected Object decode(
            PipeContext<ChannelEvent> ctx, Channel channel, ByteArrayBuffer buffer) throws Exception {
        int dataLen = buffer.readBE32();
        if (dataLen <= 0) {
            throw new StreamCorruptedException("invalid data length: " + dataLen);
        }
        if (dataLen > maxObjectSize) {
            throw new StreamCorruptedException(
                    "data length too big: " + dataLen + " (max: " + maxObjectSize + ')');
        }
        ByteArray data = buffer.read(dataLen);
        return new CompactObjectInputStream(new ByteArrayInputStream(data)).readObject();
    }
}