# react-native-open-intent

This package covers intent based operations like UPI Payment in android, Open Deeplink of UPI apps, Other Intent based operations. It also support All Installed Apps. System apps and Non System apps. 

You can also check a paticular app is installed or not using package name.

## Getting started

`$ npm install react-native-open-intent --save`

### Mostly automatic installation

`$ react-native link react-native-open-intent` for rn 0.59 or  lower.

## Methods

#### 1 - openLink(url, packageName)
#### 2 - isPackageInstalled()
#### 3 - getApps()
#### 4 - getNonSystemApps()
#### 5 - getSystemApps()

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

let UpiUrl = 'upi://pay?pa=example@yesbank&pn=Example%20Merchant&mc=&tr=3156431614262946822&tn=Paying%20to%20adi&am=1000.0&mam=0&cu=INR&url=';

const paymentHandler = async (UpiUrl) => {
    let response = null;
    try {
        response = parseResponse(await OpenIntent.openLink(UpiUrl));
    } catch (err) {
        response = parseResponse(null);
    }
};
```


If You want to open a paticular apps send the package name in seccond param. Below example I have opened phonepe you can use any other apps. It supports all available upi app.

```javascript
const paymentHandler = async (UpiUrl, 'com.phonepe.app') => {
    let response = null;
    try {
        response = parseResponse(await OpenIntent.openLink(UpiUrl));
    } catch (err) {
        response = parseResponse(null);
    }
};
```

### To check a app is installed or not

```javascript

OpenIntent.isPackageInstalled('com.whatsapp', (isInstalled) => {
    console.log('isInstalled: ', isInstalled);
    // isInstalled is true if the app is installed or false if not
});

```

### Get All System apps

```javascript
let allInstalledApps = await OpenIntent.getApps();
console.log('allInstalledApps: ', allInstalledApps);

[
  {
    "apkDir": "/data/app/com.healthkart.healthkart-zDMq_ERadbJNekV7XGMDZQ==/base.apk",
    "appName": "HealthKart",
    "firstInstallTime": 1578846224181,
    "icon": "iVBORw0KGgoAAAANSUhEUgAAAKgAAACoCAYAAAB0S6W0AAAAAX",
    "lastUpdateTime": 1619112973398,
    "packageName": "com.healthkart.healthkart",
    "size": 15552800,
    "versionCode": 339,
    "versionName": "16.1.1"
  }
]
```

### Get All Installed system apps

```javascript
let apps = await OpenIntent.getSystemApps();
console.log('apps: ', apps);
```

### Get All Installed non system apps

```javascript
let apps = await OpenIntent.getNonSystemApps();
console.log('apps: ', apps);
```