package ro.pub.cs.systems.eim.practicaltest02;

import android.provider.SyncStateContract;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread {
    private ServerThread serverThread;
    private Socket socket;

    private String firstNumber = null;
    private String secondNumber = null;
    private String operation = null;

    private static final String TAG = "MyActivity";


    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String city = bufferedReader.readLine();
            String informationType = bufferedReader.readLine();
            if (city == null || city.isEmpty() || informationType == null || informationType.isEmpty()) {
                Log.e(TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            String result = null;
            switch(operation) {
                case "add":
                    result = Integer.parseInt(firstNumber) + Integer.parseInt(secondNumber);
                    break;
                case "mul":
                case "mul":
                    Thread.sleep(2000);
                    result = Integer.parseInt(firstNumber) + Integer.parseInt(secondNumber);
                    break;
            }
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
        } catch (JSONException jsonException) {
            Log.e(TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());

                }
            }
        }
    }

}

