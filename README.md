# rn-signal-strength

React native plugin to get native android cell signal strength.

## Installation

```sh
npm install rn-signal-strength
```

## Usage

```js
import { getCurrentSignalStrength } from 'rn-android-signal-strength';

// ...

const result = await getCurrentSignalStrength();
```

Expected result is between 0 to 4


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
