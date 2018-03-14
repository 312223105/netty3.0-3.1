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
package org.jboss.netty.channel.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.AbstractServerChannel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelSink;
import org.jboss.netty.channel.socket.DefaultServerSocketChannelConfig;
import org.jboss.netty.channel.socket.ServerSocketChannelConfig;
import org.jboss.netty.channel.Channels;

class NioServerSocketChannel extends AbstractServerChannel
                             implements org.jboss.netty.channel.socket.ServerSocketChannel {

    private static final Logger logger = Logger.getLogger(NioServerSocketChannel.class.getName());

    final ServerSocketChannel socket;
    private final ServerSocketChannelConfig config;

    NioServerSocketChannel(
            ChannelFactory factory,
            ChannelPipeline pipeline,
            ChannelSink sink) {

        super(factory, pipeline, sink);

        try {
            socket = ServerSocketChannel.open();
        } catch (IOException e) {
            throw new ChannelException(
                    "Failed to open a server socket.", e);
        }

        try {
            socket.socket().setSoTimeout(1000);
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e2) {
                logger.log(
                        Level.WARNING,
                        "Failed to close a partially initialized socket.", e2);
            }
            throw new ChannelException(
                    "Failed to set the server socket timeout.", e);
        }

        config = new DefaultServerSocketChannelConfig(socket.socket());

        Channels.fireChannelOpen(this);
    }

    public ServerSocketChannelConfig getConfig() {
        return config;
    }

    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) socket.socket().getLocalSocketAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    public boolean isBound() {
        return isOpen() && socket.socket().isBound();
    }

    public boolean isConnected() {
        return false;
    }

    @Override
    protected boolean setClosed() {
        return super.setClosed();
    }

    @Override
    protected ChannelFuture getSucceededFuture() {
        return super.getSucceededFuture();
    }
}
