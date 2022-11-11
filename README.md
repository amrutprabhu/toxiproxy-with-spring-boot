# ToxiProxy with spring boot 

This project is used to demonstrate the use of [Toxiproxy](https://github.com/Shopify/toxiproxy) to interrupt or add latency to the connection 
between two systems. 

Here we have database which is brought up using docker compose, and then we create a proxy to the database.
We then communicate with the database via the proxy. The proxy is used to introduce the issues like latency or timeout.


#### Starting a Toxiproxy server
```shell
docker run --rm \
--net host \
-p 8474:8474 \
ghcr.io/shopify/toxiproxy
```

#### Creating a proxy
```shell
toxiproxy-cli create -l localhost:13306 -u localhost:3306 db-proxy
```

#### Creating a latency of 1000 ms
```shell
toxiproxy-cli toxic add -t latency -a latency=1000 db-proxy
```

#### Remove latency
```shell
toxiproxy-cli toxic remove -n latency_downstream db-proxy
```

#### Alias to run command without installing the cli.
```shell
alias toxiproxy="docker run --rm \
--net host \
--entrypoint="/toxiproxy-cli" \
-it \
ghcr.io/shopify/toxiproxy"
```
