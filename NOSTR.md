# nostr - messages

## Client request (first request)

```json
[
 "REQ",
 "929676923932772",
 {
  "kinds":[0,1,2,7],
  "since":1671348685,
  "limit":450
 }
]
```

## Server response

```json
[
 "EVENT",
 "929676923932772",
 {
  "id":"484197e1627fb531fc157c779b047ba4896a36ab1a9f0df1accc3398141fa1a6",
  "kind":0,
  "pubkey":"b4cde574ef03f44e2c0ef01d691c0b331cc105aa7a6f1416b7492ca748b45274",
  "created_at":1671435077,
  "content":"{\"name\":\"Hansbria\",\"about\":\"!\"}",
  "tags":[],
  "sig":"96d86fca787c4a12457d8820edeba4f364d662cbf19a6805e6524d7c03a2f25c2e8c6596faa9ee3da4db1b73fb997dec838873fd5032ef6123052b4e8c07b44e"
 }
]
```

```json
[
 "EVENT",
 "929676923932772",
 {
  "id":"dc2ec2c2297607a9b38238ce6fb5e38fe2422d4b511e09218b540a6ab6d6825d",
  "kind":2,
  "pubkey":"b6d47259229adf90f6b16acbbc3e566a34cf3acb5dad69595f8017f8ff636baa",
  "created_at":1671435063,
  "content":"wss://nostr.rocks",
  "tags":[],
  "sig":"b92c8bc8a7ef984aa4960304d13cb9fd828ff7f2d34e9f128d33c686eee426da15b0a2bcf332d44f8decfc45a32c4f2080fffdd551012de3ba5213fd698cea7c"
 }
]
```

## Client request (send message)

```json
[
 "EVENT",
 {
  "kind":1,
  "pubkey":"1bfac4a10ec18f77245db9edaeaeb54c26256b0a866c1061278ee7984e865bdb",
  "content":"test",
  "tags":[],
  "created_at":1671454891,
  "sig":"0844d3ac08a8a256799c663e8d0308cbf25c8d072b4b94aa208f31a6a6219191516deeefd8395acf4b09de480fbbec1830792844c3f8b70edf177276c9511058",
  "id":"9938b2d8489d21109e6aef6d6d889277c69618a00f364e45e365a35fdc70d6a4"
 }
]
```

## Client klickt auf anderen Teilnehmer

```json
[
 "REQ",
 "6575805217776569",
 {
  "authors":["04c915daefee38317fa734444acee390a8269fe5810b2241e5e6dd343dfbecc9"],
  "kinds":[0],
  "limit":1
 }
]
```

```json
[
 "REQ",
 "07552604843823452",
 {
  "authors":["04c915daefee38317fa734444acee390a8269fe5810b2241e5e6dd343dfbecc9"],
  "kinds":[1],
  "limit":150
 }
]
```
