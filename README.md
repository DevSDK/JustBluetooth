# JustBluetooth
Not more, Bluetooth Communication

1:1 Translate Data

Not 1:N


## How To Use?

1. permission check

2. Initialize BT Class

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
                    public void OnRead(byte[] data) {
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
  
getDeviceList : return Set < BluetoothDevice >

CheckWithEnableBluetooth : Activate Bluetooth 

Disconnect : Disconnect Connection


### OnRead Method Interface Definition

```Java
    public interface OnReadMethod
    {
        void OnRead(byte[] data);
    }
``` 

### Issue

---

---

1. 전송측과 수신측의 전송 속도가 같아야 데이터가 온전함(데이터가 누락되지 않음) -> 추후 개선 가능함

2. 오가는 데이터가 클수록 전송과 수신측의 Interval이 길수록 안정적임. (1번의 이유 때문에) -> 개선 가능함
