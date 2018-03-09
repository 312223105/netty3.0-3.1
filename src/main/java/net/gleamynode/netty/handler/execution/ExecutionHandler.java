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
package net.gleamynode.netty.handler.execution;

import java.util.concurrent.Executor;

import net.gleamynode.netty.channel.ChannelEvent;
import net.gleamynode.netty.pipeline.PipeContext;
import net.gleamynode.netty.pipeline.PipelineCoverage;
import net.gleamynode.netty.pipeline.UpstreamHandler;

@PipelineCoverage("all")
public class ExecutionHandler implements UpstreamHandler<ChannelEvent> {

    private final Executor executor;

    public ExecutionHandler(Executor executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        this.executor = executor;
    }

    public final Executor getExecutor() {
        return executor;
    }

    public void handleUpstream(
            PipeContext<ChannelEvent> context, ChannelEvent element) throws Exception {
        executor.execute(new ChannelEventRunnable(context, element));
    }
}