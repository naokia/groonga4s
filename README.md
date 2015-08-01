# groonga4s
Groonga client library for Scala

## Installation

Add a following dependency into your build.sbt at first.

``` scala
libraryDependencies += "com.naokia" %% "groonga4s" % "0.1.5"

resolvers += "naokia github repository (snapshots)" at "http://naokia.github.io/repositories/snapshots"
```

## Usage

### Instantiate

``` scala
val client = new GroongaClient("http://localhost:10041")
````

### Select command

#### with filter

``` scala
// select from "Person" table
val res = client.select(SelectParameters("Person", filter = Some( """_key=="alice""""))).recover({
  case e:GroongaException => throw e
}).get
res.items map { item =>
  item("_key") // alice
}
```
#### with query

``` scala
val res = client.select(SelectParameters("Person",
query = Some("golf"),
matchColumns = Seq("profile")
))
```

#### output drilldowns

``` scala
val drillDownParameters = DrillDownParameters("city")

val res = client.select(SelectParameters("Person", drillDowns = Seq(drillDownParameters))).recover({
  case e:GroongaException => throw e
}).get
val populationOfTokyo = res.drillDownGroups("city")("tokyo").nsubrecs // Some(13350000)
))
```

