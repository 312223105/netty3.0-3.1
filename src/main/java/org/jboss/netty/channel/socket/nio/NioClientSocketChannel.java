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
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelSink;
import org.jboss.netty.channel.Channels;

class NioClientSocketChannel extends NioSocketChannel {

    private static final Logger logger = Logger.getLogger(
            NioClientSocketChannel.class.getName());

    private static SocketChannel newSocket() {
        SocketChannel socket;
        try {
            socket = SocketChannel.open();
        } catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }

        boolean success = false;
        try {
            socket.configureBlocking(false);
            success = true;
        } catch (IOException e) {
            throw new ChannelException("Failed to enter non-blocking mode.", e);
        } finally {
            if (!success) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.log(
                            Level.WARNING,
                            "Failed to close a partially initialized socket.",
                            e);
                }
            }
        }

        return socket;
    }

    volatile NioWorker worker;
    volatile ChannelFuture connectFuture;
    volatile boolean boundManually;

    NioClientSocketChannel(
            ChannelFactory factory, ChannelPipeline pipeline, ChannelSink sink) {

        super(null, factory, pipeline, sink, newSocket());
        Channels.fireChannelOpen(this);
    }

    @Override
    NioWorker getWorker() {
        return worker;
    }

    @Override
    void setWorker(NioWorker worker) {
        if (this.worker == null) {
            this.worker = worker;
        } else if (this.worker != worker) {
            // worker never changes.
            throw new IllegalStateException("Should not reach here.");
        }
    }
}
