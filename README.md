# react-native-open-intent

## Getting started

`$ npm install react-native-open-intent --save`

### Mostly automatic installation

`$ react-native link react-native-open-intent`

## Usage
```javascript
import OpenIntent from 'react-native-open-intent';

// TODO: What to do with the module?
const openIntentHandler = async (url) => {

    let openIntentData = null;
        try {
            openIntentData = await OpenIntent.openLink(url);
            console.log('openIntentData: ', openIntentData);

        } catch (error) {
            console.log('error: ', error);
        }
}
```
