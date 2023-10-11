# cinema-room-rest-service
A simplified backend service responsible for the management of seats & tickets in the cinema.

## API
| Endpoints          	| Description                                                                                      	|
|--------------------	|-------------------------------------------------------------------------------------------------	|
| **GET** /seats     	| Returns a list of available seats                                                               	|
| **POST** /purchase 	| Returns a ticket-representing token on specified row & column in the request body               	|
| **POST** /return   	| Refund a ticket by passing a token in the request body                                          	|
| **POST** /stats    	| Get statistics (e.g. number of purchased tickets); requires a password (hardcoded) as parameter 	|
