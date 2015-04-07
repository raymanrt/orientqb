
orientqb
========

*orientqb* is a [builder](http://en.wikipedia.org/wiki/Builder_pattern) for OSQL query language written in Java.

It provides a fluent interface to build objects which represent a Query whose String representation can be parsed by
[OrientDB](http://www.orientechnologies.com/orientdb/).
It may be considered as an equivalent of [jOOQ](http://www.jooq.org/) for the OrientDB query language.

*orientqb* has been thought to help developers in writing complex queries dynamically and aims to be simple but powerful.
In the past years it helped me a lot in writing OSQL without typos and without dealing to much with ugly String concatenations.
Then I decided to share it with the community.

For now it only supports SELECT statements, as defined in the last
[documentation](http://www.orientechnologies.com/docs/last/).

It has a very small footprint (the only external dependency is [guava](https://code.google.com/p/guava-libraries/) 18.0) and
it's independent from OrientDB libraries.

Almost every function has a **Unit Test**, which you can use to learn how to use the library.

*orientqb* is thought to build queries incrementally, therefore it's easy adding parts to a given query
but typically it is not possible to remove them.

**[WARNING]** *orientqb* doesn't have any knowledge of the schema of your data, hence it can build only syntactically valid queries.
Writing semantically valid queries still depends on you.

Install and First Usage
-----------------------

You can include *orientqb* into your Maven project as a dependency:
```
<dependency>
  <groupId>com.github.raymanrt</groupId>
  <artifactId>orientqb</artifactId>
  <version>0.1.2</version>
</dependency>
```

Or you can build it from source with Maven:

```
mvn clean install
```

Write your first query is as simple as you can imagine:

```java
Query q = new Query();
assertEquals("SELECT FROM V", q.toString());
```

You can declare one or more `Projection` to your `Query` object as you want (order is preserved):

```java
Query q = new Query()
        .select("hello")
        .select("world");
assertEquals("SELECT hello, world FROM V", q.toString());
```

You can change default `Target` of your `Query` (V) with `from` method:

```java
Query q = new Query()
        .select("field")
        .from("Target");
assertEquals("SELECT field FROM Target", q.toString());
```

Projections
-----------

The best way to build projections which aren't simple fields is to work with `Projection` objects. The first reason you
should use a `Projection` object is to assign an alias:


```java
// static import is strongly suggested to improve your code readability
// import static Projection.projection;
// [...]

Query q = new Query()
        .select(projection("field").as("f"))
        .from("Target");
assertEquals("SELECT field as f FROM Target", q.toString());
```

Starting with a `Projection` object you can apply
[methods](http://www.orientechnologies.com/docs/1.7.8/orientdb.wiki/SQL-Methods.html#bundled-methods) or
[functions](http://www.orientechnologies.com/docs/1.7.8/orientdb.wiki/SQL-Functions.html#bundled-functions)
to build more complex projections:

```java
// static import is strongly suggested to improve your code readability
// import static Projection.projection;
// [...]

Query q = new Query()
        .select(projection("field").normalize().as("fNormalized"))
        .from("Target");
assertEquals("SELECT field.normalize() as fNormalized FROM Target", q.toString());
```

```java
// static import is strongly suggested to improve your code readability
// import static Projection.projection;
// import static ProjectionFunction.max;
// [...]

Query q = new Query()
        .select(max(projection("field")).as("fMax"))
        .from("Target");
assertEquals("SELECT max(field) as fMax FROM Target", q.toString());
```

All the methods and functions available in OrientDB have their counter part in *orientqb* with some exceptions for those
methods which can't have a valid Java identifier.

**Base expressions**: `dot`, `index`, `indexRange`, `field` methods:

```java
// static import is strongly suggested to improve your code readability
// import static Projection.projection;
// [...]
Query q = new Query()
        .select(
                projection("field")
                .dot(projection("subField"))
                .index(0)
        )
        .from("Class");
assertEquals("SELECT field.subField[0] FROM Class", q.toString());
```

**Mathematical operators**: `plus`, `minus`, `times`, `divide`, `mod` methods:

```java
// static import is strongly suggested to improve your code readability
// import static Projection.projection;
// [...]
Query q = new Query()
        .select(
            projection("field").times(2).plus(1)
        )
        .from("Class");
assertEquals("SELECT field * 2 + 1 FROM Class", q.toString());
```

*orientqb* features for `Projection` (SELECT part):
* all [functions](http://www.orientechnologies.com/docs/last/orientdb.wiki/SQL-Functions.html)
* all [methods](http://www.orientechnologies.com/docs/last/orientdb.wiki/SQL-Methods.html)
* all [record attributes](http://www.orientechnologies.com/docs/last/orientdb.wiki/sql-where.html#record-attributes)
* all [variables](http://www.orientechnologies.com/docs/last/orientdb.wiki/SQL-Where.html#variables) (only those available in SELECT: $parent, $current)
* named variables ($var)
* work with [fields](http://www.orientechnologies.com/docs/1.7.8/orientdb.wiki/Document-Field-Part.html)
* nested queries

Targets
-------

There is also a `Target` object, which provides some utility to some particular targets available in OrientDB:

```java
Query q = new Query()
        .select("field")
        .from(Target.target("#1:1", "#1:2"));
assertEquals("SELECT field FROM [#1:1, #1:2]", q.toString());
```

Target can also be a nested query:

```java
Query q = new Query();

Query nested = new Query()
        .select("city")
        .select(sum(projection("salary")).as("salary"))
        .from("Employee")
        .groupBy(projection("city"))
        ;

Target t = Target.nested(nested);

q.from(t)
.where(projection("salary").gt(1000));
assertEquals("SELECT FROM (SELECT city, sum(salary) as salary FROM Employee GROUP BY city) WHERE salary > 1000", q.toString());
```



Clauses
-------

To filter your results is typically required to populate the WHERE part of your Query. To help you in this task
*orientqb* defines a specific object, called `Clause` which can be build with methods accessible from `Projection` object:

```java
Query q = new Query()
        .select(Projection.ALL)
        .from("Class")
        .where(projection("f2").eq(5));
assertEquals("SELECT * FROM Class WHERE f2 = 5", q.toString());
```

By default clauses are joined with AND boolean operator:

```java
Query q = new Query()
        .select(Projection.ALL)
        .from("Class")
        .where(projection("f2").eq(5))
        .where(projection("f3").lt(0));
assertEquals("SELECT * FROM Class WHERE f2 = 5 AND f3 < 0", q.toString());
```

You can join two or more clauses with specific functions (`and`, `or`, `not`) accessible from `Clause` object:

```java
// static import is strongly suggested to improve your code readability
// import static Clause.not;
// import static Clause.or;
// [...]
Query q = new Query()
        .select(Projection.ALL)
        .from("Class")
        .where(or(
                projection("f2").eq(5),
                not(projection("f3").lt(0))
        ));
assertEquals("SELECT * FROM Class WHERE f2 = 5 OR NOT(f3 < 0)", q.toString());
```

You can also declare parameters or named parameters:

```java
Query q = new Query()
        .select(Projection.ALL)
        .from("Class")
        .where(projection("f").eq(Parameter.PARAMETER));
assertEquals("SELECT * FROM Class WHERE f = ?", q.toString());
```

```java
Query q = new Query()
        .select(Projection.ALL)
        .from("Class")
        .where(projection("f").eq(Parameter.parameter("fParameter")));
assertEquals("SELECT * FROM Class WHERE f = :fParameter", q.toString());
```

All the [conditional operators](http://www.orientechnologies.com/docs/1.7.8/orientdb.wiki/SQL-Where.html#conditional-operators)
are supported. Please **always** remember that *orientqb* hasn't any knowledge of the schema of your data and you should
care of using valid operators for your field.

Other Query methods
-------------------

*orientqb* implements `Query` methods for:
* GROUP BY
* ORDER BY (ASC or DESC)
* SKIP / LIMIT
* FETCHPLAN (and fetching strategies)
* TIMEOUT (and timeout strategies)
* LOCK (and locking strategies)
* PARALLEL

To be completed after 0.1.0 release
-----------------------------------

In my spare time I'd like to add the following construct:

* DELETE and UPDATE
* TRAVERSE
* other OSQL commands

Obviously some of my time will be spent in bug fixing, code cleaning and API optimizations, since the aim of the project
will always be to make life easier in writing OSQL queries. **Any contribution (pull requests) in this direction will
always be appreciated** but new features will be accepted only if tested.

Nice to have (if a version 1.0.0 will be released)
--------------------------------------------------

There are a bunch of features which are not considered as a priority by now, but should be considered as valid future works:

* pretty print
* query validation (wrt to the schema? declaring field types during query building?)
* orientdb versions (alignment of *orientqb* to orientdb versions)
