package com.adunik.adunik_connectingdevices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class BTClass
{
    private BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private OutputStream outStream;
    private ConnectedThread mConnectedThread;

    private BluetoothSocket createBTSocket(BluetoothDevice device) throws IOException
    {
        if(Build.VERSION.SDK_INT >= 10)
        {
            try
            {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            }
            catch (Exception e) {}
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public int btConnect(String address)
    {
        if(btSocket != null)
        {
            try
            {
                btSocket.close();
            }
            catch(IOException e) {}
        }

        BluetoothDevice device = BA.getRemoteDevice(address);
        try
        {
            btSocket = createBTSocket(device);
            mConnectedThread = new ConnectedThread(btSocket);
        }
        catch(IOException e){}

        try
        {
            btSocket.connect();
            return 0;
        }
        catch(IOException e)
        {
            try
            {
                btSocket.close();
            }
            catch(IOException e2){}
            return 1;
        }
    }

    public void getData(String msg)
    {
        mConnectedThread.write(msg);
    }

    private class ConnectedThread extends Thread
    {
        public ConnectedThread(BluetoothSocket socket)
        {
            OutputStream tmpOut = null;
            try
            {
                tmpOut = socket.getOutputStream();
            }
            catch (IOException e) { }
            outStream = tmpOut;
        }

        public void write(String message)
        {
            byte[] msgBuffer = message.getBytes();
            try
            {
                outStream.write(msgBuffer);
            }
            catch (IOException e) {}
        }
    }
}
