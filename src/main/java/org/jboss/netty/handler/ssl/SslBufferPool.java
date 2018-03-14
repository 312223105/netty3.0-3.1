/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @author tags. See the COPYRIGHT.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.netty.handler.ssl;

import java.nio.ByteBuffer;

/**
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev$, $Date$
 *
 */
public class SslBufferPool {

    // Add 1024 as a room for compressed data.
    private static final int MAX_PACKET_SIZE = 16665 + 1024;
    private static final int DEFAULT_POOL_SIZE = MAX_PACKET_SIZE * 1024;

    private final ByteBuffer[] pool;
    private final int maxBufferCount;
    private int index;

    public SslBufferPool() {
        this(DEFAULT_POOL_SIZE);
    }

    public SslBufferPool(int poolSize) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException("poolSize: " + poolSize);
        }

        int maxBufferCount = poolSize / MAX_PACKET_SIZE;
        if (poolSize % MAX_PACKET_SIZE != 0) {
            maxBufferCount ++;
        }
        poolSize = maxBufferCount * MAX_PACKET_SIZE;

        pool = new ByteBuffer[maxBufferCount];
        this.maxBufferCount = maxBufferCount;
    }

    public int getMaxPoolSize() {
        return maxBufferCount * MAX_PACKET_SIZE;
    }

    public synchronized int getUnacquiredPoolSize() {
        return index * MAX_PACKET_SIZE;
    }

    synchronized ByteBuffer acquire() {
        if (index == 0) {
            return ByteBuffer.allocate(MAX_PACKET_SIZE);
        } else {
            return (ByteBuffer) pool[-- index].clear();
        }
    }

    synchronized void release(ByteBuffer buffer) {
        if (index < maxBufferCount) {
            pool[index ++] = buffer;
        }
    }
}