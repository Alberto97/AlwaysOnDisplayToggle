# Always on Display Toggle

An Android quick setting that turns Always on Display on or off.

## Permissions

The app requires the ```WRITE_SECURE_SETTINGS``` permission to be able to toggle AoD on and off.\
You can grant it with adb:

```bash
adb shell pm grant org.alberto97.aodtoggle android.permission.WRITE_SECURE_SETTINGS
```
Please note that this app has only been tested on Google Pixels.\
Different manufacturers may have implemented AoD their own way and therefore the app might not work.
