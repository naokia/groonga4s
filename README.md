# groonga4s - Groonga Scala Client

## Supported command

- select
- load

## Installation

Add a following dependency into your build.sbt at first.

``` scala
libraryDependencies += "com.naokia" %% "groonga4s" % "0.8.1"

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
// select from "Site" table
case class Site(_key: String, genre: String, title: String)

val request = new SelectRequest.Builder("Site")
  .withOutputColumns(Seq("_key", "genre", "title"))
  .withFilter("""genre=="sns"""")
  .build
client.select(request).onComplete {
  case Success(result) => for (site <- result.as[Site]) println(site)
  case Failure(t) => println("Error: " + t.getMessage)
}
```

#### output drill downs

``` scala
val drillDown = DrillDown("genre")
val request = new SelectRequest.Builder("Site")
  .withOutputColumns(Seq("_key", "genre", "title"))
  .withDrillDowns(Seq(drillDown))
  .build
client.select(request).onComplete {
  case Success(result) => println(result.drillDowns("genre")("sns").nsubrecs)
  case Failure(t) => println("Error: " + t.getMessage)
}
```

### Load command

``` scala
val site = Site("http://example.com", "sns", "example site")
client.load(new LoadRequest("Site", classOf[Site], List(site))).onComplete{
  case Success(result) => println(result.affected)
  case Failure(t) => println("Error: " + t.getMessage)
}
```
