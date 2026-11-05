# Homework 4 - API Design

## 1. What is GraphQL?

GraphQL is a query language for APIs and a runtime that executes those queries. The server defines a typed schema, and the client chooses which fields to request from that schema.

For example, a client can ask for a user's name without also downloading the user's email and other details. It can also request related data, such as the user's posts, in the same query.

GraphQL is not a database or a replacement for SQL. The server's resolvers can get data from a database, another API, or another service. A resolver is the function that supplies the value of a field.

The three operation types are:

- **Query:** read data.
- **Mutation:** change data, such as creating or updating a user.
- **Subscription:** receive a stream of updates when events happen.

See the official [GraphQL introduction](https://graphql.org/learn/) and [execution guide](https://graphql.org/learn/execution/).

## 2. What problems is GraphQL trying to solve?

GraphQL mainly helps with flexible data fetching:

- **Over-fetching:** an endpoint returns more data than the client needs. A profile card may only need a name, but receive a whole user record.
- **Under-fetching:** one response does not contain everything needed. A screen may need separate requests for a user and the user's posts.
- **Different client needs:** a mobile screen and a web dashboard may need different fields. They can use different queries against the same schema instead of needing a new endpoint for each screen.

This can reduce unnecessary response data and client-server round trips. Clients can only request fields that the server already exposes and permits them to access. A new field still needs backend work if it is not in the schema.

GraphQL does not automatically make the database faster. One API request can still cause many database queries if the resolvers are poorly implemented.

These are the main use cases discussed in the assigned [Hello Interview API Design reading](https://www.hellointerview.com/learn/system-design/core-concepts/api-design#graphql).

## 3. How do you use GraphQL?

First, define a schema on the server. This describes the available data and operations. For example:

```graphql
type User {
  id: ID!
  name: String!
  email: String
}

type Query {
  user(id: ID!): User
}

type Mutation {
  updateUserName(id: ID!, name: String!): User
}
```

The `!` means the value cannot be null. Here, a user must have an ID and a name, but email is optional. The `user` query can return null if no matching user exists. See [Schemas and Types](https://graphql.org/learn/schema/).

Next, connect the schema to resolvers. The `user` resolver looks up a user by ID. The `updateUserName` resolver checks permission, updates the name, and returns the updated user. Defining the schema alone does not implement these actions.

The client can send this query in a GraphQL client or API testing tool:

```graphql
query GetUser($id: ID!) {
  user(id: $id) {
    id
    name
  }
}
```

Variables:

```json
{
  "id": "1"
}
```

For HTTP, the server commonly exposes `/graphql`. Send a POST request with `Content-Type: application/json` and this JSON body:

```json
{
  "query": "query GetUser($id: ID!) { user(id: $id) { id name } }",
  "variables": { "id": "1" }
}
```

The server validates the query against the schema, executes the resolvers, and returns the result. Assuming user 1 exists, a successful response could be:

```json
{
  "data": {
    "user": {
      "id": "1",
      "name": "Amy"
    }
  }
}
```

Email is not included because it was not requested. Variables let the client change the ID without rebuilding the query text. See [Queries](https://graphql.org/learn/queries/) and [Serving over HTTP](https://graphql.org/learn/serving-over-http/).

To change the name, use a mutation:

```graphql
mutation {
  updateUserName(id: "1", name: "Anna") {
    id
    name
  }
}
```

This calls the update resolver and asks for the updated ID and name in the response. These examples describe an API contract; they require an implemented GraphQL server to run.

## 4. What are the differences between REST and GraphQL?

REST is an architectural style built around resources. GraphQL is a query language and execution system built around a schema. Both can be used over HTTP.

| Topic | REST | GraphQL |
| --- | --- | --- |
| API structure | Usually resource URLs such as `/users/1` and `/users/1/posts`. | Commonly one endpoint, such as `/graphql`, with operations in the request. |
| Response fields | The server normally chooses the response structure. Some APIs support field selection. | The client chooses fields from the server's schema. |
| Operations | Uses HTTP methods such as GET, POST, PUT, PATCH, and DELETE. | Uses query, mutation, and subscription operations. |
| Related data | May require multiple requests, though REST can also embed related data. | Can request related fields together if the schema supports them. |
| Contract | Can be documented and typed using OpenAPI or other tools. | A typed schema is built into GraphQL. |
| Caching | GET responses fit standard HTTP caching well when configured correctly. | Caching is possible, but query-specific responses often need more client/server cache logic. |
| Errors | Commonly uses HTTP status codes and an error body. | Execution results can contain both `data` and `errors`; HTTP status still matters for transport and request failures. |

For a simple CRUD application, I would usually start with REST. I would consider GraphQL when several clients need different combinations of related data.

The tradeoff is extra server complexity. For example, loading a list and then fetching related data separately for each item creates an N+1 query problem. Batching can reduce those database calls. The server also needs authorization checks and limits on expensive queries. REST can have N+1 problems too; they are not unique to GraphQL.

References: [assigned REST and GraphQL reading](https://www.hellointerview.com/learn/system-design/core-concepts/api-design), [GraphQL caching](https://graphql.org/learn/caching/), [GraphQL response format](https://graphql.org/learn/response/), and [GraphQL performance](https://graphql.org/learn/performance/).
