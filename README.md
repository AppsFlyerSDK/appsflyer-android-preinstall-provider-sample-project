# PreInstall Provider Sample Project

## Introduction

This repository contains an API reference and a sample project for the client side of the Preinstall Campaigns measurement.
This part of the integration is intended for Preinstall Providers wishing to use AppsFlyer's Preinstall Campaign measurement. 


**Important Note**:  This is a reference implementation of the Preinstall Provider code; It should not be used as is. 
Please review code and adjust it according to your project's needs.

## Implementation 


Please use this  [sample project](/oemsdk/src/main/java/com/appsflyer/oem/PreInstallContentProvider.kt) for the integration. 

The sample project consists of 2 main modules:
* `PreInstallClient` - Fetch a transaction_id from Appsflyer's backend
* `PreInstallContentProvider` - Expose the transaction_id with a ContentProvider, which can then be accessed by the AppsFlyer SDK to obtain the transaction_id and send it to the the attribution server.

#### Content Provider - Data Structure

Column|  Type | Description
---   |   ---  | ----  
`0`   | String |  transaction_id

#### Important Note
- The ContentProvider should check for the `callingPackage` (i.e the app_id), and return only the data associated
with that app_id.  See the `PreInstallContentProvider` for further reference.



### API Reference

```kotlin  
class PreInstallClient(application: Application, private val mediaSource: String)  
```  

- `mediaSource` - The partner unique identifier

```kotlin  
@Throws(IOException::class)  
suspend fun registerAppInstall(info: PreInstallInfo): PreInstallId  
```  

- `info` - the object that contains attribution parameters. 


```kotlin  
class PreInstallId
```
This class represents the object that returns from registering a PreInstallation.

Name                | Example
---                 | ----  
`app_id`            | "com.appsflyer.game"
`transaction_id`    | "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4"

Usage example can be found in the [Integration Test](/oemsdk/src/androidTest/java/PreInstallClientTest.kt)