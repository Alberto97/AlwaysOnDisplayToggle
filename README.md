# Always on Display Toggle

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80"
     align="right">](https://f-droid.org/packages/org.alberto97.aodtoggle/)

[![GitHub release](https://img.shields.io/github/release/Alberto97/AlwaysOnDisplayToggle.svg?logo=github)](https://github.com/Alberto97/AlwaysOnDisplayToggle/releases/latest)
[![F-Droid](https://img.shields.io/f-droid/v/org.alberto97.aodtoggle.svg?logo=F-Droid)](https://f-droid.org/en/packages/org.alberto97.aodtoggle/)
[![GitHub license](https://img.shields.io/github/license/Alberto97/AlwaysOnDisplayToggle)](https://github.com/Alberto97/AlwaysOnDisplayToggle/blob/master/LICENSE)

An Android quick setting that turns Always on Display on or off.

## Permissions

The app requires the ```WRITE_SECURE_SETTINGS``` permission to be able to toggle AoD on and off.\
You can grant it with adb:

```bash
adb shell pm grant org.alberto97.aodtoggle android.permission.WRITE_SECURE_SETTINGS
```
or in case of multiple users (`<userId>` is listed as first number in output of `adb shell pm list users`):
```bash
adb shell pm grant --user <userId> org.alberto97.aodtoggle android.permission.WRITE_SECURE_SETTINGS
```

Please note that this app has only been tested on Google Pixels.\
Different manufacturers may have implemented AoD their own way and therefore the app might not work.
