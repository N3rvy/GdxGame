import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import packets.Packet
import java.lang.Exception

class Server(val port: Int, val app: Application) {

    private val bossGroup = NioEventLoopGroup()
    private val workerGroup = NioEventLoopGroup()
    private val bootstrap = ServerBootstrap().apply {
        group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(
                        ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())),
                        ObjectEncoder(),
                        PacketServerHandler(app),
                    )
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
    }
    private var connection: Channel? = null

    fun start(): ChannelFuture {
        val future = bootstrap.bind(port)
        connection = future.channel()
        return future
    }

    fun send(packet: Packet): ChannelFuture? {
        return connection?.write(packet)
    }

    fun close(): ChannelFuture {
        return connection?.disconnect() ?: throw Exception("Tried closing connection while not bound")
    }
}