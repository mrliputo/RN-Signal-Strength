# info-signal-strength

React native plugin to get native android cell signal strength and speed internet.

## Installation

```sh
npm install info-signal-strength
```

## Usage

```js
import { getCurrentSignalStrength } from 'info-signal-strength';

// ...

const result = await getCurrentSignalStrength();
```

Signal Strength Expected result is between 0 to 4


```js
import { getTotalRxTxBytes } from 'info-signal-strength';

// ...

const result = await getTotalRxTxBytes();

//result example : {"rxBytes": 123891237, "txBytes": 41323222, "time": 1453432323}
```

to get internet speed, I have not found an effective and efficient way

but currently with rxBytes,txBytes and comparing the total initial data and the following data

rxBytes and txBytes are:
Return number of bytes transmitted since device boot.
the way I tried below. if anyone can provide the best solution other than this please let me know

```js
import { getCurrentSignalStrength, getTotalRxTxBytes } from 'info-signal-strength';

useEffect(async () => {
  const result = await getCurrentSignalStrength();
  console.log("result",result);
}, []);

useEffect(async () => {
let previousResult = await getTotalRxTxBytes();
let intvl = 5000;
let time = intvl;

    const calculateSpeed = async () => {
      const currentResult = await getTotalRxTxBytes();

      if (previousResult) {
       // const url = 'https://freetestdata.com/wp-content/uploads/2021/09/1-MB-DOC.doc'; //this is just to trigger data movement, delete this, it will crash
       // const response = await fetch(url); //this is just to trigger data movement, delete this, it will crash
        console.log("previousResult.time",previousResult.time);
        console.log("currentResult.time",currentResult.time);
        let timeDiff = currentResult.time-previousResult.time;
        let rxDiff = currentResult.rxBytes-previousResult.rxBytes;
        let txDiff  = currentResult.txBytes-previousResult.txBytes;

        let rxSpeed = (rxDiff * 1000) / timeDiff;
        let txSpeed = (txDiff * 1000) / timeDiff;


        let download = (rxSpeed * 8) / (1024*1024); // Byte to bit, then to megabit
        let upload = (txSpeed * 8) /  (1024*1024);  // Byte to bit, then to megabit

        console.log("download Mbps", download);
        console.log("upload Mbps", upload);
        console.log("time", time);
      }
      time = time + intvl;
    };

    // Interval to check speed every second
    const interval = setInterval(calculateSpeed, intvl);

    return () => clearInterval(interval); // Cleanup on component unmount
}, []);

```




## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
