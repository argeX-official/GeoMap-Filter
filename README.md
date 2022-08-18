# GeoMap-Filter
GPS location filtering application for better GeoFence Notification.
Written By argeX team ©2022

# Problem

The devices which use GPS sensors for GPS tracking inside a building usually have GPS jump issues due to low signal reception.

When the GPS location jumps occur, the user receives GeoFence violation notifications.

<img src="screenshots\ss3.png" alt="drawing" height="350"/>
<img src="screenshots\ss4.png" alt="drawing" height="350"/>

As seen from the above two images watch #12 and watch #30 have a lot of GPS jumps when the two watches are inside the building. Hence, results in unwanted GeoFence violation notifications. When compared to 50 watches, the user receives hundreds of false GeoFence violation notifications through out the day.

# Solution

We have developed a sample android application to address this problem. We developed a GeoHash filtering application based on Dmitry Romanov's study. The application filters unwanted jumps which can be seen as red colored lines. The filtered locations are colored as blue lines.

<img src="screenshots\ss1.png" alt="drawing" height="350"/>
<img src="screenshots\ss2.png" alt="drawing" height="350"/>

As seen from the above images, usage of GeoHash filtering improves GPS location accuracy inside the building.

The number in the bottom middle of the screen indicated as 8, represents the hash length. The user can increase or decrease the hash length in order to change filtering accuracy.

Raw means the total mount of GPS location data taken from the GPS sensor. Filter represents the amount of filtered GPS location data.

# Conclusions and Recommendations

We strongly believe that when GeoHash filtering applied to TCL Kids Smartwatches, the filter will improve locational accuracy inside the buildings significantly.

# How to Install and Test

* Obtain a Google API key for Google Maps.
* Install the app ang give the required location permissions.

# Referance

https://github.com/drfonfon/android-geohash
