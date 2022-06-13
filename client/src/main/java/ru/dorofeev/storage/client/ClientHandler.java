package ru.dorofeev.storage.client;

import common.file.FileSynchronizer;
import common.data.FileInfoSetMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final String clientId;
    private final FileSynchronizer fileSynchronizer;

    public ClientHandler(String clientId, FileSynchronizer fileSynchronizer) {
        this.clientId = clientId;
        this.fileSynchronizer = fileSynchronizer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {

        FileInfoSetMessage fileInfoSetMessage = new FileInfoSetMessage(clientId, fileSynchronizer.getFilesForSync());
        ctx.writeAndFlush(fileInfoSetMessage);

        System.out.println("channelActive=" + fileInfoSetMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {

        System.out.println("channelRead=" + msg);
        FileInfoSetMessage request = (FileInfoSetMessage) msg;

        if(!request.isSync()) {
            System.out.println("Files need sync...");
            var response = new FileInfoSetMessage(clientId, fileSynchronizer.getFilesForSync(request.getFileInfoSet()));
            ctx.writeAndFlush(response);
        }else
        {
            System.out.println("All files sync");
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
