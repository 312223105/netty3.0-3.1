package me.xuzhe;

import net.gleamynode.netty.bootstrap.ServerBootstrap;
import net.gleamynode.netty.channel.ChannelFactory;
import net.gleamynode.netty.channel.ChannelPipeline;
import net.gleamynode.netty.channel.socket.nio.NioServerSocketChannelFactory;
import net.gleamynode.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()
        );

        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        ChannelPipeline pipeline =  bootstrap.getPipeline();

        pipeline.addLast("handler", new StringDecoder());

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(8080));

    }
}
