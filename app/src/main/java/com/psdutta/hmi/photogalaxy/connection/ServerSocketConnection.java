package com.psdutta.hmi.photogalaxy.connection;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

final class ServerSocketConnection {
    private static final String TAG = ServerSocketConnection.class.getSimpleName();
    private ServerSocket mServerSocket;
    private Socket mSocket;
    private InputStream mInputStream;

    ServerSocketConnection() {
        mServerSocket = null;
        mInputStream = null;
    }

    void connect(final int port) {
        try {
            close();
            mServerSocket = new ServerSocket(port);
            Log.i(TAG, "Start accepting packet from client ......");
            mSocket = mServerSocket.accept();
            Log.i(TAG, "Started listening packet from client.....");
            mInputStream = mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getIpAddress() {
        if (mSocket != null) {
            return mSocket.getInetAddress().toString().replace("/", "");
        }
        return null;
    }

    boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    void close() {
        try {
            if (mServerSocket != null) {
                mServerSocket.close();
            }
            if (mInputStream != null) {
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte[] read() throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }

        InputStreamReader reader = new InputStreamReader(mInputStream);
        BufferedReader inputFromClient = new BufferedReader(reader);
        String inputStringFromClient = inputFromClient.readLine();
        Log.d("TTTT", "Input is: "+ inputStringFromClient);
        return inputStringFromClient.getBytes();
/*
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        Log.d("TTTT", "Reading data");

        while ((nRead = mInputStream.read(data, 0, data.length)) != -1) {
            String string = new String(data);
            if ("**".equals(string)) {
                Log.d("TTTT", "Pranoy has send **");
                break;
            }
            Log.d("TTTT", "Reading few bytes");
            buffer.write(data, 0, nRead);
            Log.d("TTTT", "Read buffer completed");
        }
        Log.d("TTTT", "Flushing buffer and sending response");
        buffer.flush();
        return buffer.toByteArray();

        *//*
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int available = mInputStream.available();
        byte[] buf = new byte[available];
        Log.d("TTTT", "Available: " + available);
        int n = mInputStream.read(buf);
        byteArrayOutputStream.write(buf, 0, n);
        byte[] result = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return result;*/
    }
}
