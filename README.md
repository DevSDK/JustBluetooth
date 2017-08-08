# JustBluetooth
Not more, Bluetooth Communication

1:1 Translate Data

Not 1:N


## How To Use?

1. permission check

2. Initalize BT Class

#### Constructor Definition

```Java
    /// Activity = Current Activity or Target Activity, Interval = On Read Interval Time (ms),
    /// RWBufferSize = I/O Buffer Size, read = OnRead Method
    public BT(Activity activity, int Interval, int RWBufferSize, OnReadMethod read)
```

for example

```Java
BT  bt = new BT(MainActivity.this, 500, 1024,new BT.OnReadMethod() {
                    @Override
                    public void OnRead(String data) {
                      ///Do Something
                    }
                });
``` 

#### Connect Method Definition

```Java
    ///device = target device for connection, _flagIO = bitflag (INPUT, OUTPUT) for Open Stream
    public synchronized void Connect(BluetoothDevice device, char _FlagIO)
```
for example

```Java
   //For Input
   bt.Connect(device, BT.INPUT);
   
   //For Output(
   bt.Connect(device, BT.OUTPUT);
   
   //For Input/Output
   bt.Connect(device, BT.OUTPUT | BT.INPUT);
```
### Write Method Definition

```Java
    public synchronized void Write(byte[] data)
```


### Other Method
  
getDeviceList : return Set<BluetoothDevice>

CheckWithEnableBluetooth : Activate Bluetooth 

Disconnect : Disconnect Connection


### OnRead Method Interface Definition

```Java
    public interface OnReadMethod
    {
        void OnRead(byte[] data);
    }
``` 
