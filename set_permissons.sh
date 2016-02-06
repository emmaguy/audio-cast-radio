#!/bin/bash
#
# argument: package name 
# Set permission SET_ANIMATION_SCALE and WRITE_EXTERNAL_STORAGE for each device.

adb=$ANDROID_HOME/platform-tools/adb
package=$1

if [ "$#" = 0 ]; then
	echo "No parameters found, run with sh set_permissions.sh <package>"
	exit 0
fi

# get all the devices
devices=$($adb devices | grep -v 'List of devices' | cut -f1 | grep '.')

for device in $devices; do
	echo "Setting permissions to device" $device "for package" $package
	$adb -s $device shell pm grant $package android.permission.WRITE_EXTERNAL_STORAGE
	$adb -s $device shell pm grant $package android.permission.SET_ANIMATION_SCALE
done