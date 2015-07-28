# groonga4s
Groonga client library for Scala

## Installation

Add a following dependency into your build.sbt at first.

``` scala
libraryDependencies += "com.naokia" %% "groonga4s" % "0.1.2"

resolvers += "naokia github repository (snapshots)" at "http://naokia.github.io/repositories/snapshots"
```

## Usage

### Instantiate

``` scala
val client = new GroongaClient("http://localhost:10041")
````

### Select command

#### with filter

ex: from "Person" table.

``` scala
val res = client.select(SelectParameters("Person", filter = Some( """_key=="alice""""))).recover({
  case e:GroongaException => throw e
}).get
res.items map { item =>
  item("key") // alice
}
````
#### with query

``` scala
val res = client.select(SelectParameters("Person",
query = Some("john"),
matchColumns = Seq("name")
))
````
