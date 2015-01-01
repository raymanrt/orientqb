
orientqb
========
orientqb is a [builder](http://en.wikipedia.org/wiki/Builder_pattern) for OSQL query language written in Java.

It provides a fluent interface to build objects which represent a Query and which can be parsed by
[OrientDB](http://www.orientechnologies.com/orientdb/). It may be considered as an equivalent of [jOOQ](http://www.jooq.org/)
for the OrientDB query language.

orientqb has been thought to help developers in writing dynamically complex queries and aims to be simple but powerful.
In the past years it helped me a lot to write OSQL without typos and without dealing to much with messy String concatenations.
Then I decided to share it with the community.

For now it only supports SELECT statements, as defined in the last
[documentation](http://www.orientechnologies.com/docs/last/).

It has a very small footprint (only Guava dependency) and it's independent from OrientDB libraries.

Almost every function has a Unit Test, which you can use to learn how to use the library.

Install and First Usage
-----------------------

You can install orientqb from source with Maven:

```
mvn clean install
```

Write your first query is as simple as can be imagined:

```java
Query q = new Query();
assertEquals("SELECT FROM V", q.toString());
```

You can one or more `Projection` to your `Query` object as you want (order is preserved):

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

Done
----
* [projection] items (functions, methods)
* [projection] record attributes
* [projection] variables (only those in SELECT: $parent, $current)
* [projection] work with fields: http://www.orientechnologies.com/docs/1.7.8/orientdb.wiki/Document-Field-Part.html
* [clause] logical operators (NOT)
* nested queries
* [clause] logical operators (IN, NOT IN, CONTAINS, ...)
* [projection, clause] math operators (methods)
* [PARALLEL]
* [group by] ???
* skip/limit (check if it's ok)
* order by (ascending/descending)
* [clause] conditional operators
* [TIMEOUT] <Timeout> [<STRATEGY>]
* [LOCK default|record]
* [projection] $variabile
* [projection] ? e :variable
* [FETCHPLAN <FetchPlan>]
* [from] custom targets

To be completed after 0.1.0 release
===================================

* DELETE
* UPDATE
* TRAVERSE
* other commands

Nice to have (but won't be required)
====================================

* pretty print
* validation
* orientdb versions