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
package org.jboss.netty.channel;

import java.util.Map;


/**
 *
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev$, $Date$
 *
 * @apiviz.landmark
 * @apiviz.composedOf org.jboss.netty.channel.ChannelHandlerContext
 * @apiviz.owns       org.jboss.netty.channel.ChannelHandler
 * @apiviz.uses       org.jboss.netty.channel.ChannelSink - - sends events downstream
 */
public interface ChannelPipeline {
    void addFirst (String name, ChannelHandler handler);
    void addLast  (String name, ChannelHandler handler);
    void addBefore(String baseName, String name, ChannelHandler handler);
    void addAfter (String baseName, String name, ChannelHandler handler);

    void remove(ChannelHandler handler);
    ChannelHandler remove(String name);
    <T extends ChannelHandler> T remove(Class<T> handlerType);
    ChannelHandler removeFirst();
    ChannelHandler removeLast();

    void replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler);
    ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler);
    <T extends ChannelHandler> T replace(Class<T> oldHandlerType, String newName, ChannelHandler newHandler);

    ChannelHandler getFirst();
    ChannelHandler getLast();

    ChannelHandler get(String name);
    <T extends ChannelHandler> T get(Class<T> handlerType);

    ChannelHandlerContext getContext(ChannelHandler handler);
    ChannelHandlerContext getContext(String name);
    ChannelHandlerContext getContext(Class<? extends ChannelHandler> handlerType);

    void sendUpstream(ChannelEvent e);
    void sendDownstream(ChannelEvent e);

    Channel getChannel();
    ChannelSink getSink();
    void attach(Channel channel, ChannelSink sink);

    Map<String, ChannelHandler> toMap();
}
