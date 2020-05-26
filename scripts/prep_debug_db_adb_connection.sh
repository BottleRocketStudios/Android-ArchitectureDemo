#!/bin/bash
# The purpose of this script is to allow an Android Studio Run Configuration to be created to execute the following command to prepare the Android DebugDb connection. See BEST_PRACTICES.md for more info.

adb forward tcp:8080 tcp:8080