# mobile-automation-framework

## Requirements

#### Installing Java15 (OpenJDK)
- Using [Home brew](https://brew.sh/) to install Java15 [AdoptOpenJDK Java Development Kit](https://formulae.brew.sh/cask/adoptopenjdk#default)
brew install adoptopenjdk15 --cask
jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home
jenv global 15


#### Manually setup Java 15 (OpenJDK) on Mac
 - Set `$JAVA_HOME` environment variable by finding out if you run bash or zsh by running this command `echo $SHELL` should return `/bin/zsh` or `/bin/bash`
 - On Mac OS X 10.5 or later, we can use `/usr/libexec/java_home` to return the location of the default JDK, or find all installed JDKs by running this command `/usr/libexec/java_home -V`
 - On macOS 10.5 Leopard open terminal and run this command `~/.bash_profile` and then add `export JAVA_HOME=$(/usr/libexec/java_home)`
 - On macOS 10.15 Catalina open terminal and run this command `~/.zshenv` and then add `export JAVA_HOME=$(/usr/libexec/java_home)`
 - Source the file and print the `$JAVA_HOME`

#### Install Android Studio
 - Home brew installation using `brew install android-sdk`


#### Manually Install Android Studio on Mac
 - [Download](https://developer.android.com/studio/index.html) and Install Android Studio
 - Set `$ANDROID_HOME` environment variable
 - Open `~/.bash_profile` or `~/.zshrc`
 - Add following to the profile

```
export ANDROID_HOME=/Users/$USER/Library/Android/sdk
export PATH=$ANDROID_HOME/platform-tools:$PATH
export PATH=$ANDROID_HOME/platform-tools/bin:$PATH
export PATH=$ANDROID_HOME/tools:$PATH
export PATH=$ANDROID_HOME/tools/bin:$PATH
export PATH=$ANDROID_HOME/build-tools/30.0.2:$PATH
export PATH=$ANDROID_HOME/build-tools/30.0.2/bin:$PATH
export PATH=$ANDROID_HOME/emulator:$PATH
```


#### Install XCode
 - [Download XCode](https://developer.apple.com/download/) and install XCode
 - Install Xcode Command line tools by executing `$ xcode-select –install` command


#### Install NodeJS
- Install [Node.js](https://nodejs.org/en/download/) manually or via Home brew `$ brew install node`
- To check node version run this command `$ node -v`


#### Install WebDriver
- `$ npm install wd`


#### Install Appium
- [Download](https://appium.io) manually, via `npm install -g appium` or `brew install --cask appium`
- To check Appium version run this command `$ appium -v`


#### Install IDE: IntelliJ
 - [Download](https://devqa.io/brew-install-intellij/) or `$ brew install IntelliJ`


#### Install Appium desktop inspector
 - [Download](https://github.com/appium/appium-inspector) manually or `brew install --cask appium-inspector`
 - [Download](https://github.com/appium/appium-desktop/releases/latest) latest release


#### Install set-simulator-location
 - [Download](https://github.com/MobileNativeFoundation/set-simulator-location) manually or `brew install lyft/formulae/set-simulator-location`
 - [Download](https://github.com/MobileNativeFoundation/set-simulator-location/releases/latest) latest release

Used for iOS tests that change location


## Setup Appium inspector
 - In the `Remote Path` text field add `/wd/hub`
 - Set Desired Capabilities (see examples below) and start the session

## Setup chromedriver (Test WebView on Android)
 - Download required [chromedriver](https://sites.google.com/chromium.org/driver/downloads?authuser=0). Be aware each version of ChromeDriver supports Chrome with matching major, minor, and build version numbers. For example, ChromeDriver 73.0.3683.20 supports all Chrome versions that start with 73.0.3683.
 - Move chromedriver to resource folder in project src/main/resources/chromedriver 
 - In resource folder right-click on chromedriver and press Open. We make this step to provide access for chromedriver file. 

## In order to run tests you have to pass next environment variables:

```
-Dmobile.os={ios/android}
-Dudid={device UDID}
-Ddevice.name={device name}
-Dapp.path="{path to *.apk/*.app}"
-Dplatform.version={platform version only for iOS tests}
```

## Run Automation Tests

#### Running a test Class or individual test
1. Start adb-server with connected device or emulator
2. In IntelliJ right-click on any class or test -> Modify Run Config
3. Set VM Options Example: 
```
-Dmobile.os=ios
-Dudid="A8306C2C-1DFA-4860-A155-8B7020FF6C48"
-Dapp.path={path to *.apk/*.app}"
-Dplatform.version="15.4"
```
4. Choose new Run Config and press run

#### Command line (Gradle)

- app.path - path to .app/.apk file  (File names should contain no spaces, bad file name example `TuneIn Radio.app`)
- suite - path to .xml test suite
- mobile.os - testing platform ios/android (lowercase)
- platform.version - version of OS (required for iOS)
- udid - device unique id (simulator / real device) / emulator)

```cmd
gradle clean runTest 
-Dmobile.os=ios 
-Dudid="0DF38FE4-59F8-4911-8B06-4F4AA1A4AE89" 
-Dsuite=smoke 
-Dplatform.version="15.4" 
-Dapp.path=
 ```


## Run Stream Test Automation

#### Available Configurations

| Config Key | Description                                                                                                                                                      | Example Values                                                                            |
|-------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| `appium.stream.test.enabled` | Enable stream test                                                                                                                                               | `true`, `false`                                                                           | 
| `device.name="iPhone 11"` | Headspin device name                                                                                                                                             | `iPhone 11`, `Pixel 5`                                                                    |
| `appium.stream.headspinStreams.token` | Headsin API token                                                                                                                                                | `f2c3d0a24c54135af26ad2a4e66398cc`                                                        |
| `appium.stream.file.name`  | `.csv` document name with the stream details such as name, station id, and media type                                                                            | `player_test_source_stream`                                                               |
| `udid`  | Headspin device's UDID number                                                                                                                                    | `"00008030-00097864146A802E"` or `"00008110-0001743402A2401E,00008101-000344412282001E"`  |
| `platform.version`  | Headspin device's [Operating System](https://ui.headspinStreams.io/docs/supported-devices#operating-systems) version                                             | `15.5` `13.1`                                                                             |
| `appium.stream.monitor.duration`  | Stream test duration in seconds                                                                                                                                  | `3800` `120`                                                                              |
| `appium.stream.write.to.file`  | Writes to debug notes to a `player_test_source_streams_results.csv` file                                                                                         | `true` `false`                                                                            |
| `appium.stream.test.uninstall.tunein.app`  | Uninstalls TuneIn app after the test is complete                                                                                                                 | `true` `false`                                                                            |
| `appium.stream.test.install.tunein.app`  | Installs previously uploaded TuneIn App from Headspin platform by using headspinStreams app id that should be provided in `appium.stream.headspinStreams.app.id` | `true` `false`                                                                            |
| `appium.stream.headspinStreams.app.id`  | Headspin app id of the uploaded TuneIn App [manage-apps-by-app-id ](https://ui.headspinStreams.io/docs/app-management#manage-apps-by-app-id)                     | `dd76b46d-e9e7-4c3b-93cf-d7bac0c2430f`                                                    |
| `headspinStreams.app.path` | App path for the AD_HOC .ipa build to be uploaded to Headspin                                                                                                    | `"./Downloads/TuneIn_Radio.ipa"`                                                          |
| `appium.stream.headspinStreams.capture.video` | Enable video capture                                                                                                                                             | `true` `false`   |
| `appium.stream.headspinStreams.capture.network` | Enable network capture                                                                                                                                           | `true` `false`   |
| `appium.master.file.name` | Rename `player_test_master.csv` file name                                                                                                                        | `"player_test_master.csv"`                                                                |
| `appium.master.file.path` | Output path for the master csv file                                                                                                                              | `"/Users/serviceaccount/Downloads/Headspin_Files/master_csv/"`                            |
| `appium.report.file.name` | Test report csv file                                                                                                                                             | `"player_test_source_streams_results.csv"`                                                |
| `appium.report.file.path` | Output path for the detailed test report csv file                                                                                                                | `"/Users/serviceaccount/Downloads/Headspin_Files/"`                                       |
| `appium.premium.user.test` | Number of premium users to run in parallel. Default value is `0`                                                                                                 | `"1"` `"2"` or `"3"`                                                                      |
| `appium.stream.station.name` | Content profile name | `MSNBC`| 
| `appium.stream.station.id` | Station guide id that starts with prefix `s` or `t` | `283042253` | 
| `appium.stream.internet.speed.test` | Enables [ internet speed test ](https://tunein.atlassian.net/wiki/spaces/PRODENG/pages/3167748123/Automation+Flow+Internet+Speed+Test+on+iOS) | `true` `false` | 
| `appium.stream.get.serial.id` | Captures `Serial id` from device before test | `true` `false` | 
| `appium.stream.local.json.file.path` | Read data from local `Streams.json` file | `"/Users/username/Documents/Streams.json"` | 

 This test will trigger the Headspin device and starts the stream test session based on the config `appium.stream.monitor.duration`

### Examples

#### Single Device Example

```cmd
-Dmobile.os=ios
-Dappium.stream.test.enabled="true"
-Dappium.stream.monitor.duration="3800"
-Dappium.stream.headspinStreams.token="{$headspin_token}"
-Dappium.stream.file.name="player_test_source_stream"
-Ddevice.name="iPhone 11"
-Dplatform.version="15.5"
-Dudid="00008030-00097864146A802E"
-Dappium.stream.write.to.file="true"
-Dappium.stream.headspinStreams.capture.network="false"
-Dappium.stream.test.uninstall.tunein.app="false"
-Dappium.stream.test.install.tunein.app="false"
-Dappium.stream.headspinStreams.app.id="dd76b46d-e9e7-4c3b-93cf-d7bac0c2430f"
```

#### Multiple Devices Example (Parallel Testing)

```cmd
-Dmobile.os=ios
-Dappium.stream.test.enabled="true"
-Dappium.stream.monitor.duration="7000"
-Dappium.stream.file.name="player_test_source_stream"
-Dappium.stream.write.to.file="true"
-Dappium.stream.headspinStreams.token="${headspin_token}"
-Ddevice.name="iPhone 12,iPhone 13,iPhone 14"
-Dheadspin.app.path="/Users/tsaakyan/Downloads/TuneIn_Radio.ipa"
-Dudid="00008101-001660343663003A,00008110-00046C600EB8801E,00008110-0004546A3485401E"
-Dappium.stream.test.uninstall.tunein.app="false"
-Dappium.stream.test.install.tunein.app="false"
-Dappium.stream.headspinStreams.capture.network="false"
```

#### Upload and Install Build to Hedspin Before Test

```cmd
-Dmobile.os=ios
-Dappium.stream.test.enabled="true"
-Dappium.stream.monitor.duration="7000"
-Dappium.stream.headspinStreams.token="${headspin_token}"
-Dappium.stream.file.name="player_test_source_stream"
-Ddevice.name="iPhone 12"
-Dplatform.version="15.5"
-Dudid="00008101-001660343663003A"
-Dheadspin.app.path="${path_to_ipa}/TuneIn_Radio.ipa"
-Dappium.stream.test.uninstall.tunein.app="true"
-Dappium.stream.test.install.tunein.app="true"
```

#### When number of devices = 2 and number of premium users = 1

```cmd
-Dmobile.os=ios
-Dappium.stream.test.enabled="true"
-Dappium.stream.monitor.duration="7000"
-Dappium.stream.headspinStreams.token="${headspin_token}"
-Dappium.stream.file.name="player_test_source_stream"
-Ddevice.name="iPhone 12"
-Dplatform.version="15.5"
-Dudid="00008101-001660343663003A,00008110-00046C600EB8801E"
-Dheadspin.app.path="${path_to_ipa}/TuneIn_Radio.ipa"
-Dappium.stream.test.uninstall.tunein.app="true"
-Dappium.stream.test.install.tunein.app="true"
-Dappium.premium.user.test="1"
```

### Network Configuration (Headspin API )

[Network Config API](https://ui.headspin.io/docs/network-config-api) allows us to configure network connectivity to simulate network types such as LTE, 3G, EDGE or very bad network.
 
Set network configuration to simulate 3G network
```json
[
  {
    "00008101-001660343663003A": {
      "deviceName": "iPhone 12",
      "stationName": "The Rick & Bubba Show",
      "stationId": "208566",
      "streamType": "AAC",
      "networkType": "3G"
    }
  }
]
```

|  key          | Default value | Supported values                        |
|----------------------------|---------------|----------------------------|
| `networkType` | `WiFi`        | `LTE`, `3G`, `EDGE`, `VERY BAD NETWORK` |
