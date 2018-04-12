# wikipedia-adapter

Middleware adapter for [Wikidata](https://query.wikidata.org/) and [DBPedia](http://dbpedia.org/sparql).


## Develop

```bash
lein run

lein ring server-headless
```


## Build
```bash
lein ring uberjar

$ java -jar middlepack-adapter-0.1.0-standalone.jar
```

## Dockerize
```bash
# build
docker build -t wikipedia-adapter:latest .

# run
docker run --name wikipedia-adapter -p 3030:3000 -d wikipedia-adapter

# login
docker exec -it wikipedia-adapter sh
```

## License

MIT
