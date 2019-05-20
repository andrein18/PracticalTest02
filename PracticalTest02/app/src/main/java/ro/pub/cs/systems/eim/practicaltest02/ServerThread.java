package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ServerThread {

    private int port = 0;
    private ServerSocket serverSocket = null;
    private String firstNumber = null;
    private String secondNumber = null;
    private String operation = null;
    private Integer result = 0;

    private static final String TAG = "MyActivity";


    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e(TAG, "An exception has occurred: " + ioException.getMessage());
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void setData(String firstNumber, String secondNumber, String operation) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.operation = operation;
    }

    public synchronized String getFirstNumber() {
        return firstNumber;
    }

    public synchronized String getSecondNumber() {
        return secondNumber;
    }

    public synchronized String getOperation() {
        return operation;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (ClientProtocolException clientProtocolException) {
            Log.e(TAG, "[SERVER THREAD] An exception has occurred: " + clientProtocolException.getMessage());
        } catch (IOException ioException) {
            Log.e(TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
        }

        switch (operation) {

            case "add":
                result = Integer.parseInt(firstNumber) + Integer.parseInt(secondNumber);
                break;

            case "mul":
                Thread.sleep(2000);
                result = Integer.parseInt(firstNumber) + Integer.parseInt(secondNumber);
                break;
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }

    public boolean isAlive() {

    }

}

