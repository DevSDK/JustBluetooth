package BTLib;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BT {
    final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Activity m_activity = null;
    private boolean IsOutputBufferFill = false;
    private boolean IsInputBufferFill = false;

    private byte []out_buffer;
    private byte []in_buffer;
    private int m_Interval;

    private char Flag_IO = 0;

    public static char INPUT    = 0x01;
    public static char OUTPUT   = 0x02;

    private InputStream     m_InputStream;
    private OutputStream    m_OutputStream;

    BluetoothAdapter m_bluetoothAdapter;

    OnReadMethod read_method;

    boolean IsConnected = false;

    public BluetoothSocket m_socket = null;

    /// Activity = Current Activity or Target Activity, Interval = On Read Interval Time (ms),
     /// RWBufferSize = I/O Buffer Size, read = OnRead Method
    public BT(Activity activity, int Interval, int RWBufferSize, OnReadMethod read)
    {
        m_Interval = Interval;
        read_method = read;
        m_activity = activity;
        m_bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        m_bluetoothAdapter.startDiscovery();
        out_buffer = new byte[RWBufferSize];
        in_buffer = new byte[RWBufferSize];

    }
    public Set<BluetoothDevice> GetDeviceList()
    {
        return m_bluetoothAdapter.getBondedDevices();
    }

    public boolean IsFillnBuffer()
    {
        return IsInputBufferFill;
    }


    ///device = target device for connection, _flagIO = bitflag (INPUT, OUTPUT) for Open Stream
    public synchronized void Connect(BluetoothDevice device, char _FlagIO)
    {
        Flag_IO = _FlagIO;
        try {
            m_socket = device.createInsecureRfcommSocketToServiceRecord(UUID_SPP);
            IsConnected = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        m_socket.connect();
                        if((Flag_IO & INPUT) == INPUT)
                            m_InputStream = m_socket.getInputStream();
                        if((Flag_IO & OUTPUT ) == OUTPUT)
                            m_OutputStream = m_socket.getOutputStream();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                while(IsConnected) {
                                    if((Flag_IO & INPUT) == INPUT)
                                    {
                                            if ((m_InputStream.available()) > 0) {
                                                m_InputStream.read(in_buffer);
                                                for (int i = 0; i < in_buffer.length; i++) {
                                                    in_buffer[i] = 0;
                                                }
                                                read_method.OnRead(in_buffer);
                                            }
                                            else
                                                SystemClock.sleep(m_Interval);
                                    }

                                    if((Flag_IO & OUTPUT) == OUTPUT)
                                    {
                                        if(IsOutputBufferFill)
                                        {
                                            m_OutputStream.write(out_buffer);
                                            IsOutputBufferFill = false;
                                        }
                                    }
                                }
                                    if((Flag_IO & OUTPUT) == OUTPUT)
                                        m_OutputStream.close();
                                    if((Flag_IO & INPUT) == INPUT)
                                        m_InputStream.close();
                                    m_socket.close();
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void Disconnect()
    {
        IsConnected = false;
    }

    public synchronized void Write(byte[] data)
    {
        out_buffer = data;
        IsOutputBufferFill = true;
    }

    public void CheckWithEnableBluetooth()
    {
        if(!m_bluetoothAdapter.isEnabled())
        {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            m_activity.startActivityForResult(i, 2);
        }
    }

    public interface OnReadMethod
    {
        void OnRead(byte[] data);
    }


};