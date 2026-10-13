# API evidence (captured 13 Oct 2026)

## Banana - JSON, no base64

Request:

```
GET https://marcconrad.com/uob/banana/api.php?out=json&base64=no
```

Response:

```json
{ "question" : "https://www.sanfoh.com/uob/banana/data/t1539acf2dc756aae70594758e7n97.png" , "solution" : 7 }
```

App uses `out=csv&base64=yes` instead, so image + solution come in
one reply with no second fetch. Format: `<base64>,<solution>`.
Parsed in `BananaFetch.java` by splitting on the last comma.

Failure test: airplane mode -> `readUrl` returns null -> bundled
`banana1.png` (answer 4) with (offline) tag.

## Weather - JSON

Request:

```
GET https://api.open-meteo.com/v1/forecast?latitude=51.8787&longitude=-0.42&current=temperature_2m&timezone=Europe%2FLondon
```

Response (trimmed):

```json
{"latitude":51.877346,"timezone":"Europe/London","current":{"time":"2026-10-13T03:30","temperature_2m":15.5}}
```

Parsed by hand in `WeatherServer.java` (`lastIndexOf` the key,
first hit is the units string). Failure test: bad network ->
canned temps with (offline) tag.
