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
package org.jboss.netty.channel.socket.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.Socket;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelSink;
import org.jboss.netty.channel.*;

class OioAcceptedSocketChannel extends OioSocketChannel {

    private final PushbackInputStream in;
    private final OutputStream out;

    OioAcceptedSocketChannel(
            Channel parent,
            ChannelFactory factory,
            ChannelPipeline pipeline,
            ChannelSink sink,
            Socket socket) {

        super(parent, factory, pipeline, sink, socket);

        try {
            in = new PushbackInputStream(socket.getInputStream(), 1);
        } catch (IOException e) {
            throw new ChannelException("Failed to obtain an InputStream.", e);
        }
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            throw new ChannelException("Failed to obtain an OutputStream.", e);
        }

        Channels.fireChannelOpen(this);
        Channels.fireChannelBound(this, getLocalAddress());
        Channels.fireChannelConnected(this, getRemoteAddress());
    }

    @Override
    PushbackInputStream getInputStream() {
        return in;
    }

    @Override
    OutputStream getOutputStream() {
        return out;
    }

    @Override
    void setInputStream(PushbackInputStream in) {
        if (this.in != in) {
            throw new IllegalStateException("Should not reach here.");
        }
    }

    @Override
    void setOutputStream(OutputStream out) {
        if (this.out != out) {
            throw new IllegalStateException("Should not reach here.");
        }
    }
}
