package ru.dorofeev.storage.server;

import common.file.FileSynchronizer;
import common.data.FileInfoSetMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Paths;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final String workDirectory;

    public ServerHandler(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {

        System.out.println("channelRead=" + msg);
        FileInfoSetMessage request = (FileInfoSetMessage) msg;

        FileSynchronizer fileSynchronizer = new FileSynchronizer(Paths.get(workDirectory, request.getClientId()).toString());
        var response = new FileInfoSetMessage(request.getClientId(),fileSynchronizer.syncFiles(request.getFileInfoSet()));
        ctx.writeAndFlush(response);
        if (response.isSync()) {
            System.out.println("All files sync");
            ctx.close();
        }else
        {
            System.out.println("Files need sync...");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

