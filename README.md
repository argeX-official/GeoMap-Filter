# GeoMap-Filter
GPS location filtering application for better GeoFence Notification.
Written By argeX team ©2022

# Problem

The devices that locate themselves via GPS and other sensors inside a building have GPS location jump issues due to low signal reception.

When the GPS location jumps occur, the user receives GeoFence violation notifications.

<img src="screenshots\ss3.png" alt="drawing" height="350"/>
<img src="screenshots\ss4.png" alt="drawing" height="350"/>

As seen from the above two images watch #12 and watch #30 have a lot of GPS jumps when the two watches are in the building. Hence, results in unwanted GeoFence violation notifications. When compared to 50 watches, the user receives hundreds of false GeoFence violation notifications.

# Solution

We have developed a sample android application to address this problem. We developed a GeoHash filtering application based on Dmitry Romanov's study. The application filters unwanted jumps which can be seen colored as red lines. The filtered locations are colored as blue lines.

<img src="screenshots\ss1.png" alt="drawing" height="350"/>
<img src="screenshots\ss2.png" alt="drawing" height="350"/>

As seen from the above images, usage of GeoHash filtering improves GPS location accuracy inside the buildings.

The number in the bottom middle of the screen indicated as 8, represents the hash length. The user can increase or decrease the hash length in order to change filtering accuracy.

Raw means the total mount of GPS location data points taken from the GPS sensor. Filter means the amount of filtered GPS location data points.

# Conclusions and Recommendations

We strongly believe that GeoHash filtering applied to TCL Kids Smartwatches such as the locational accuracy inside the building will be improved significantly.

# How to Install and Test

* Obtain a Google API key for Google Maps.
* Install the app ang give the required location permissions.

# Referance

https://github.com/drfonfon/android-geohash
