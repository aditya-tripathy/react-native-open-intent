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
### You can use this for react native android UPI payment. like below

```javascript
export const parseResponse = (response) => {
    let responseData = null;
    if (response) {
        let array = response.split('&');
        if (array && array.length > 0) {
            responseData = {};
            array.forEach((item) => {
                let element = item.split('=');
                if (element[0] === 'Status') {
                    responseData['status'] = element[1] === 'Failed' || element[1] === 'FAILURE' ? 'FAILURE' : element[1];
                } else {
                    responseData[element[0]] = element[1];
                }
            });
        }
    } else {
        responseData = {
            status: 'FAILURE',
            message: 'No action taken',
        };
    }
    return responseData;
};

//ex: upi://pay?pa=example@yesbank&pn=Example%20Merchant&mc=&tr=3156431614262946822&tn=Paying%20to%20ShopX&am=1000.0&mam=0&cu=INR&url=

const paymentHandler = async (UpiUrl) => {
    let response = null;
    try {
        response = parseResponse(await OpenIntent.openLink(UpiUrl));
    } catch (err) {
        response = parseResponse(null);
    }
};
```

### To check using package name

```javascript

OpenIntent.isPackageInstalled('com.whatsapp', (isInstalled) => {
    console.log('isInstalled: ', isInstalled);
    // isInstalled is true if the app is installed or false if not
});

```