<img src="https://www.appsflyer.com/wp-content/uploads/2016/11/logo-1.svg"  width="450">

# WIP: appsflyer-android-preload-client

AppsFlyer Preload (OEM) Client will store app information into Content Provider using the following path:

```
content://com.appsflyer.oemclient/<HASHED_APP_ID>/preload_id
```

Where `<HASHED_APP_ID>` is a package name hashed by `SHA256`.

For example, if the app_id is `"com.my.app"`, the path would be:
```
content://com.appsflyer.oemclient/252a30a2adf5ab9a340796ef38341e9a424a199a13a6f5dd2149c6503480c92f/preload_id
```
#### Content Provider data structure

Column|   Type | Description
---   |   ---  | ----
`0`   | String |  preload id


### API

```java
public class com.appsflyer.oem.AppsFlyerOEMClient
```
A main class used to create an interface between OEM and AppsFlyer OEM Client. It is a singleton.

#### Methods:

```java
public static AppsFlyerOEMClient getInstance()
```
Returns an instance of AppsFlyerOEMClient.

```java
public void setPartnerUniqueIdentifier(String pid)
```
The partner unique identifier

```java
public void addPackage(String packageName, long installTimeStamp, org.json.JSONObject data)
```
- `packageName` - the app identifier. For example, com.my.app
- `installTimeStamp` -  Timestamp representing the install time  (Epoch in milliseconds)
- `data` - the object contains attribution parameters. 



| Field Name| Description| Type| Mandatory |
| ---| --- | ------------------------------------- | --------- |
| pid                                            | Partner unique identifier                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | String                                | Yes       |
| install\_time                                  | Timestamp representing the install time                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | Epoch timestamp - long (milliseconds) | Yes       |
| app\_id                                        | Application unique identifier                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | String                                | Yes       |
| c                                              | Campaign name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | String                                | No        |
| af\_c\_id                                      | Campaign Id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | String                                | No        |
| af\_adset                                      | Adset name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | String                                | No        |
| af\_adset\_id                                  | Adset Id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | String                                | No        |
| af\_ad                                         | Ad Name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | String                                | No        |
| af\_ad\_id                                     | Ad Id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | String                                | No        |
| af\_prt                                        | Agency Account Name                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | String                                | No        |
| network\_tran\_id                              | Ad network unique transaction identifier                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | String                                | No        |
| af\_ad\_type                                   | Ad type: <br> **text**: an ad unit containing only text, e.g. a search result<br> **banner**: a basic format that appears at the top or bottom of the device screen<br> **interstitial**: a full-page ad that appears during breaks in the current experience<be> **video**: a standard video, i.e. non-rewarded<br> **rewarded\_video**: an ad unit offering in-app rewards in exchange for watching a video<br>**playable**: an ad unit containing an interactive preview of the app experience<br>**sponsored\_content**: a link included in a piece of sponsored content, like an advertorial article<br>**audio**: an audio ad | String                                | No        |
| af\_channel      | The media source channel through which the ads are distributed, e.g., UAC\_Search, UAC\_Display, Instagram, Facebook Audience Network etc.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | String                                | No        |
| af\_sub\[n\]<br>(n=1-5) example: af\_sub1 | Optional custom parameter defined by the advertiser.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | String                                | No        |



```java
public void registerPreload(com.appsflyer.oem.PreloadStateListener)
```
This method will trigger the AppsFlyer OEM Client main flow.

```java
public interface com.appsflyer.oem.PreloadStateListener
```
An interface that returns the information about processing preload.

```java
public void onPreloadSetupFinished(int responseCode)
```
Response Codes:

Name |   Code | Description
---   |   ---  | ----
`OK`   | 0 |  Success
`DEVELOPER_ERROR`   | 1 |  General errors caused by incorrect usage
`SERVICE_ERROR`   | 2 |  The error returned from AppsFlyer servers

Java Example:

```java
JSONObject dataOne = new JSONObject();
dataOne.put("af_c_id", "campaign_id_1");
dataOne.put("campaign", "campaign_name_1");

JSONObject dataTwo = new JSONObject();
// ...

AppsFlyerOEMClient.getInstance().
   setPartnerUniqueIdentifier("partner_int").
   addPackage("com.my.app1",  1606657869005, dataOne).
   addPackage("com.my.app2",  1606657869005, dataTwo).
   registerPreload(new PreloadStateListener() {
      @Override
      public void onPreloadSetupFinished(int responseCode) {
        // handle the response code
      }
}); 
```



