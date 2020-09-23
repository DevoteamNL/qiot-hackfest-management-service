# qiot-hackfest-management-service project

## Why this service?

This service is used to manage the other services present on the Weather Station.

It allows a few operations:
* user-info
* serial
* health
* upgrade-services

## Functionality
All these functionalities are exposed at `/admin/{name}`, where name is the functionalities name.

### user-info
This endpoint prints your username or email, depending if you're authenticated with a devoteam.com account.


### serial
This endpoint prints the serial number of the raspberry pi, or throws an UnsupportedOperationException if the file does not exist or cannot be access.

### health
This endpoint aggregates the health endpoint of the other services. 
This means that it queries the internal components, and checks their health endpoints. 

### upgrade-services - NOT IMPLEMENTED YET.
This endpoint is quite dangerous, and as such can only be operated by a `devoteam.com` account.
We would not want you breaking our stuff, would we. ;-)
After being triggered, this endpoint will terminate the internal components of the pi.
It will update the containers by pulling down the latest, and start the corresponding services.
