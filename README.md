# mMoneyConverter Web Service

_EXPERIMENTAL!_


Basic web frontend for the mMoneyConverter

## Build

`> lein ring uberjar`


## Deployment

1) Provide configuration (`config.edn`) with referencing account-mapping in resources/config

2) Build Docker image:
   
   `> docker build --tag mmoney-converter .`
   
   or including a version tag:
   
   `> docker build --tag mmoney-converter:1.1 .`
   
3) Create and start container, e.g.:

   `> docker run -p3000:3000 -it mmoney-converter`
   
