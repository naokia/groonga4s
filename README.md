# groonga4s - Groonga Scala Client

## Supported command

- select
- load

## Installation

Add a following dependency into your build.sbt at first.

``` scala
libraryDependencies += "com.naokia" %% "groonga4s" % "0.3.0"

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
case class Person(_key: String, city: String, profile: String, hobby: String)

val res = client.select(SelectParameters("Person", classOf[Person], filter = Some( """_key=="alice""""))).recover({
  case e:GroongaException => throw e
}).get
res.items map { item => // Person
  item._key // alice
}
```
#### with query

``` scala
val query = QueryParameters("golf", Seq("profile", "hobby"))  // query= golf , match_columns=profile,hobby
val res = client.select(SelectParameters("Person", classOf[Person], query = Some(query)))
```

#### output drilldowns

``` scala
val drillDownParameters = DrillDownParameters("city")

val res = client.select(SelectParameters("Person", classOf[Person], drillDowns = Seq(drillDownParameters))).recover({
  case e:GroongaException => throw e
}).get
val populationOfTokyo = res.drillDownGroups("city")("tokyo").nsubrecs // Some(13350000)
))
```

### Load command

``` scala
case class Person(_key: String, mailAddress: String)
val people = List(Person("taro", "taro@example.com"))
client.load(LoadParameters("Person", classOf[Person], people))
```
