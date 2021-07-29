<img src="https://massets.appsflyer.com/wp-content/uploads/2019/05/21152710/logo.png"  width="450">

# Pre install SDK

SDK fetches ids from appsflyer backend for pre-install attribution

#### Content Provider data structure
Column|   Type | Description
---   |   ---  | ----
`0`   | String |  preload_id
### API
```kotlin
class PreInstall
```
A main class used to create an interface between OEM and AppsFlyer OEM Client.
```kotlin
class PreInstall(application: Application, private val mediaSource: String)
```
The partner unique identifier

```kotlin
@Throws(IOException::class)
suspend fun add(vararg info: PreInstallInfo): List<PreInstallId>
```
- `info` - the object contains attribution parameters. 



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

An class that returns the information about processing preload.

```kotlin
class PreInstallId
```
Response:

Name            | Description
---             | ----
`app_id`        | "com.appsflyer.game"
`preload_id`    | "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4"
`status`        | "success" or "failure"

[Example](/oemsdk/src/androidTest/java/PreInstallTest.kt)