# code-sharing-platform
Share your code with everyone else. And - if you are a hacker - you may want to consider more restricted options as setting the view count limits and / or time. On that occassion, you'll have to save the UUID of your entry.

## Endpoints
| API Endpoints            	| Description                                                                                                                                         	|
|--------------------------	|-----------------------------------------------------------------------------------------------------------------------------------------------------	|
| **GET** /api/code/{uuid} 	| Returns a code snippet                                                                                                                              	|
| **GET** /api/code/latest 	| Returns a list of last 10 non-restricted (i.e. no time & views limit) code snippets                                                                 	|
| **POST** /api/code/new   	| Send a request body containing a to-be-shared code snippet and do you must also specify time & view count limits (set to '0' to leave unrestricted) 	|

| Web Endpoints        	| Description                                                                     	|
|----------------------	|---------------------------------------------------------------------------------	|
| **GET** /code/{uuid} 	| Page with a given code snippet                                                  	|
| **GET** /code/latest 	| Page with the last 10 non-restricted (i.e. no time & views limit) code snippets 	|
| **GET** /code/new    	| Page where you can add new code snippets                                        	|
