# RocketChat SpringBoot SDK 

RocketChat Java SDK based on SpringBoot and Reactor (https://projectreactor.io).
The SDK is mostly suitable for building chatbots and other integrations with RocketChat 
using SpringBoot stack.

## Getting Started
The SDK contains SpringBoot auto-configuration class that by default creates all available 
clients. 

The following properties must be set:
* **rocketchat.base-url**  - the base URL for the RocketChat server (ex. http://localhost:3000)
* **rocketchat.user**  - the bot user name
* **rocketchat.password**  - the bot user password

### Auto-Configured Beans
* **RealtimeClient** - the websocket-based Realtime-API client
* **ReactiveRocketChatClient** - reactive REST API client
* **RocketChatClient** - blocking REST API client
 

## Realtime API
https://rocket.chat/docs/developer-guides/realtime-api/
The Realtime API based on Websocket and offers bi-directional exchange of messages between
client and the server. 

### Logging
In order to get websocket protocol logs set the following property in application.properties file
```
logging.level.rocketchat.spring.ws.socket.ReactiveWebSocket=debug
```

## REST API
https://rocket.chat/docs/developer-guides/rest-api/


# TODO
* Better websocket reconnect implementation
* Integration tests suite
* Add examples
* dedicated debug logger for rest api network layer