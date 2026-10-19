
## 1. What is GraphQL?

GraphQL is a query language and API execution model that lets clients
request exactly the data they need. Instead of exposing many
resource-specific endpoints like REST, a GraphQL API usually exposes one
endpoint backed by a strongly typed schema. Clients send queries
describing the fields and relationships they want, and the server
resolves those fields and returns a response with a matching structure.

------------------------------------------------------------------------

## 2. What Problems Is GraphQL Trying to Solve?
GraphQL mainly addresses over-fetching and under-fetching. REST
endpoints often return more data than a client needs, or require several
requests to collect related data. GraphQL allows the client to request
exactly the fields and nested relationships it needs in a single query.
It also makes it easier to support different clients, such as web and
mobile applications, without creating many specialized endpoints.

------------------------------------------------------------------------

## 3. How to Use GraphQL?

To use GraphQL, the server first defines a strongly typed schema
containing types, queries, and mutations. The client sends a GraphQL
operation specifying the fields it wants. The server validates that
operation against the schema and executes resolver functions, which
retrieve or modify data from databases or downstream services. The
result is returned as JSON in roughly the same shape as the client's
query.

------------------------------------------------------------------------

## 4. What Are the Differences Between REST and GraphQL?

  -----------------------------------------------------------------------
  Feature                 REST                    GraphQL
  ----------------------- ----------------------- -----------------------
  API structure           Usually multiple        Usually a single
                          endpoints               `/graphql` endpoint

  Data returned           Server determines       Client specifies
                          response shape          requested fields

  Over-fetching           More common             Reduced by field
                                                  selection

  Under-fetching          May require multiple    Related data can often
                          requests                be fetched in one query

  Schema                  May use OpenAPI or      Strongly typed schema
                          other API contracts     is central to GraphQL

  Operations              HTTP methods such as    Queries, mutations, and
                          GET, POST, PUT, PATCH,  optionally
                          DELETE                  subscriptions

  Versioning              Often uses endpoint or  Schema can often evolve
                          header versioning       by adding fields and
                                                  deprecating old ones

  HTTP caching            Fits standard HTTP/CDN  Often requires more
                          caching naturally       application-aware
                                                  caching

  Complexity              Straightforward for     More server/query
                          simple resource APIs    complexity

  Client flexibility      Response is mostly      High client control
                          server-controlled       over response shape
  -----------------------------------------------------------------------

GraphQL is a strongly typed query language and runtime for APIs.
Compared with REST, where the server exposes resource-oriented endpoints
with predefined response structures, GraphQL lets the client specify
exactly which fields and relationships it needs. This helps reduce
over-fetching and under-fetching and is useful when web, mobile, and
other clients have different data requirements. A GraphQL server defines
a schema, clients send queries or mutations, and resolvers fetch data
from databases or downstream services. The trade-off is that GraphQL can
introduce additional server-side complexity around query performance,
authorization, caching, and problems such as N+1 queries, so I would
choose between REST and GraphQL based on the application's access
patterns rather than assuming one is always better.
