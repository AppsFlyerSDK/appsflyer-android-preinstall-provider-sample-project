<img src="https://massets.appsflyer.com/wp-content/uploads/2019/05/21152710/logo.png"  width="450">  

# PreInstall Provider sample project

### WARNING!!! This is a reference implementation of OEM code, it must not be used as is! Please review code and adjust it according to your project needs.

SDK is reference implementation of OEM part of Preload Campaigns measurement.

Current SDK sample cares the following responsibilities:

* fetches transactions_ids from appsflyer S2S backend
* saves them in local database alongside with app_ids
* provided ContentProvider, which then be accessed by AppsFlyer Android SDK to fetch transactionId
  for running app

### Content Provider details

See [sample implementation](/oemsdk/src/main/java/com/appsflyer/oem/PreInstallContentProvider.kt)

**IMPORTANT!** ContentProvider must check for the `callingPackage` and return data only associated
with calling app_id. This is required for anti-fraud reasons.

#### Data structure

Column|   Type | Description
---   |   ---  | ----  
`0`   | String |  transaction_id

### API

```kotlin  
class PreInstallClient  
```  

A main class used to create an interface between OEM and AppsFlyer OEM Client.  
OEM partner must use it to record app download/install events.

```kotlin  
class PreInstallClient(application: Application, private val mediaSource: String)  
```  

- `mediaSource` - The partner unique identifier

```kotlin  
@Throws(IOException::class)  
suspend fun registerAppInstall(info: PreInstallInfo): PreInstallId  
```  

- `info` - the object contains attribution parameters.

| Field Name| Description| Type| Mandatory |  
| ---| --- | ------------------------------------- | --------- |  
| af\_engagement\_type                           | Type of the preinstall flow (`preload` or `click_to_download`)
| af_media_source                                | Partner unique identifier                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | String                                | Yes       |  
| install\_time                                  | Timestamp representing the install time                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | Epoch timestamp - long (milliseconds) | Yes       |  
| app\_id                                        | Application unique identifier                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | String                                | Yes       |  
| af_campaign                                    | Campaign name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | String                                | No        |  
| af_campaign_id                                 | Campaign Id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | String                                | No        |  
| af\_adset                                      | Adset name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | String                                | No        |  
| af\_adset\_id                                  | Adset Id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | String                                | No        |  
| af\_ad                                         | Ad Name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | String                                | No        |  
| af\_ad\_id                                     | Ad Id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | String                                | No        |  
| af\_prt                                        | Agency Account Name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | String                                | No        |  
| af_click_id                                    | Ad network unique transaction identifier                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | String                                | No        |  
| af\_ad\_type                                   | Ad type: <br> **
text**: an ad unit containing only text, e.g. a search result<br> **
banner**: a basic format that appears at the top or bottom of the device screen<br> **
interstitial**: a full-page ad that appears during breaks in the current experience<be> **
video**: a standard video, i.e. non-rewarded<br> **
rewarded\_video**: an ad unit offering in-app rewards in exchange for watching a video<br>**
playable**: an ad unit containing an interactive preview of the app experience<br>**
sponsored\_content**: a link included in a piece of sponsored content, like an advertorial article<br>**
audio**: an audio ad | String                                | No        |  
| af\_channel      | The media source channel through which the ads are distributed, e.g., UAC\_Search, UAC\_Display, Instagram, Facebook Audience Network etc.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | String                                | No        |  
| af\_custom\[n\]<br>(n=1-5) example: af\_custom1 | Optional custom parameter defined by the advertiser.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | String                                | No        |  

An class that returns the information about processing preload.

```kotlin  
class PreInstallId  
```  
Response:

Name                | Description
---                 | ----  
`app_id`            | "com.appsflyer.game"
`transaction_id`    | "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4"

Example usage can be found in
the [Integration Test](/oemsdk/src/androidTest/java/PreInstallClientTest.kt)