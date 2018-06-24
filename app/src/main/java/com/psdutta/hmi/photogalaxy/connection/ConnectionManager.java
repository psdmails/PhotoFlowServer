package com.psdutta.hmi.photogalaxy.connection;

import android.util.Log;

import com.psdutta.hmi.photogalaxy.queue.PacketQueueManager;

public class ConnectionManager implements IClientConnection{
    private static final int CLIENT_LISTENING_PORT = 2323;
    private static final int SERVER_LISTENING_PORT = 1313;
    private static final String TAG = ConnectionManager.class.getSimpleName();
    private PacketQueueManager mPacketQueueManager;
    private Thread mServerConnectionThread;
    private Thread mClientConnectionThread;
    private ServerConnection mServerConnection;
    private ClientConnection mClientConnection;
    private PacketWriter mPacketWriter;

    ConnectionManager(PacketQueueManager manager) {
        mPacketQueueManager = manager;
        mServerConnection = new ServerConnection(CLIENT_LISTENING_PORT);
        mServerConnectionThread = new Thread(mServerConnection);
        mServerConnectionThread.start();
    }

    public void close() {
        if(mServerConnection != null) {
            mServerConnection.serverSocketConnection.close();
        }
        if(mClientConnection != null) {
            mClientConnection.clientSocketConnection.close();
        }
    }

    public void sendPacket(byte[] packet){
        if(mPacketWriter != null){
            mPacketWriter.addDataIntoQueue(packet);
        }
    }

    @Override
    public void onClientConnected(String ipAddress) {
        Log.i(TAG," On client connected :"+ipAddress);
        mClientConnection = new ClientConnection(ipAddress, SERVER_LISTENING_PORT);
        mClientConnectionThread = new Thread(mClientConnection);
        mClientConnectionThread.start();
    }

    private class ClientConnection implements Runnable {
        private final String ipAddress;
        private final int port;
        final ClientSocketConnection clientSocketConnection;
        private final Thread writerThread;

        ClientConnection(final String ip, final int portNumber) {
            ipAddress = ip;
            port = portNumber;
            clientSocketConnection = new ClientSocketConnection();
            mPacketWriter = new PacketWriter(clientSocketConnection);
            writerThread = new Thread(mPacketWriter);
        }

        @Override
        public void run() {
            writerThread.start();
            try {
                clientSocketConnection.connect(ipAddress, port);
                while (!Thread.currentThread().isInterrupted()) {
                    if (!clientSocketConnection.isConnected()) {
                        break;
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                clientSocketConnection.close();
                writerThread.interrupt();
                writerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerConnection implements Runnable {
        private final int port;
        final ServerSocketConnection serverSocketConnection;
        private final Thread readerThread;

        ServerConnection(final int portNumber) {
            port = portNumber;
            serverSocketConnection = new ServerSocketConnection();
            readerThread = new Thread(new PacketReader(serverSocketConnection, mPacketQueueManager));
        }

        @Override
        public void run() {
            readerThread.start();
            try {
                serverSocketConnection.connect(port);
                while (!Thread.currentThread().isInterrupted()) {
                    if (!serverSocketConnection.isConnected()) {
                        Log.i(TAG, "run() connection closed calledeeee");
                        break;
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Log.i(TAG, "run() connection closed called");
                serverSocketConnection.close();
                readerThread.interrupt();
                readerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
