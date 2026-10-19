# HW5 - API Design Homework

## 1. What is GraphQL?

GraphQL is a query language for APIs and a runtime for executing those queries. Unlike REST, where a client usually calls multiple resource-based endpoints, GraphQL normally exposes a single endpoint. The client describes exactly which fields it needs, and the server returns data in the same shape as the query.

A GraphQL API is defined by a schema. The schema describes the available types, fields, relationships, queries, and mutations.

A simple example:

```graphql
query {
  event(id: "123") {
    name
    date
    venue {
      name
      address
    }
  }
}
```

The client asks only for `name`, `date`, and selected `venue` fields, so the server does not need to return unrelated data.

---

## 2. What problems is GraphQL trying to solve?

GraphQL mainly tries to solve problems caused by fixed REST response structures when different clients need different data.

### Over-fetching

Over-fetching happens when an API returns more data than the client needs.

For example, a mobile application may only need a user's name and profile picture, but a REST endpoint may return the full user object. This wastes bandwidth and can be especially expensive for mobile clients.

With GraphQL, the client can request only the required fields.

### Under-fetching

Under-fetching happens when one API response does not contain enough information, so the client has to make additional requests.

For example, a page may need event information, venue information, and ticket information. In REST, this may require several endpoints. GraphQL can retrieve related data in one query.

### Too many endpoints

With REST, different clients or screens may require different combinations of data. Teams may create more and more specialized endpoints, which increases maintenance cost.

GraphQL provides a flexible schema and allows different clients to request different data from the same endpoint.

### Frontend and backend coupling

With REST, a frontend change may require the backend team to modify an endpoint or create a new one. With GraphQL, the frontend can request additional fields as long as those fields already exist in the schema.

In an interview, a good summary is:

> GraphQL is useful when different clients have different data requirements and we want to reduce over-fetching, under-fetching, and endpoint proliferation.

---

## 3. How to use GraphQL?

Using GraphQL usually involves the following steps.

### Step 1: Define a schema

The server defines the data types and relationships.

```graphql
type Event {
  id: ID!
  name: String!
  date: String!
  venue: Venue!
}

type Venue {
  id: ID!
  name: String!
  address: String!
}

type Query {
  event(id: ID!): Event
}
```

### Step 2: Implement resolvers

Resolvers contain the server-side logic used to fetch the requested fields from databases, services, or other data sources.

### Step 3: Send a query from the client

The client sends a query to the GraphQL endpoint.

```graphql
query {
  event(id: "123") {
    name
    date
    venue {
      name
    }
  }
}
```

### Step 4: Return only the requested data

The response follows the structure of the query.

```json
{
  "data": {
    "event": {
      "name": "Concert",
      "date": "2026-10-10",
      "venue": {
        "name": "Main Arena"
      }
    }
  }
}
```

For reading data, GraphQL normally uses queries. For changing data, it commonly uses mutations.

One important implementation concern is the N+1 query problem. If a GraphQL request loads many objects and then separately loads related data for every object, it can cause many database queries. Batching techniques such as DataLoader are commonly used to reduce this problem.

---

## 4. What are the differences between REST and GraphQL?

| REST | GraphQL |
|---|---|
| Usually has multiple resource-based endpoints | Usually uses a single endpoint |
| Server decides the response structure | Client specifies the fields it needs |
| Can cause over-fetching | Client can request only required fields |
| May require multiple requests for related resources | Related data can often be fetched in one query |
| Uses HTTP methods such as GET, POST, PUT, PATCH, DELETE | Primarily uses GraphQL queries and mutations |
| Resource-oriented | Schema and query-oriented |
| HTTP caching is generally straightforward | Caching can be more complicated |
| Simpler and widely understood | More flexible but adds implementation complexity |

REST is usually a good default for normal CRUD APIs because it is simple, well understood, and has excellent tooling.

GraphQL is a strong choice when:
- mobile and web clients need different fields;
- clients frequently need different combinations of related data;
- over-fetching or under-fetching is a major problem;
- frontend teams need flexible data fetching.

GraphQL also introduces additional complexity, such as schema management, query validation, field-level authorization, caching, and the N+1 query problem.

### Interview Summary

If asked to compare them in an interview, I would say:

> REST exposes resources through multiple endpoints and usually lets the server determine the response shape. GraphQL normally exposes a single endpoint and lets the client specify exactly which fields and relationships it needs. REST is simpler and is a good default for most CRUD services, while GraphQL is useful when different clients have different data requirements or when over-fetching and under-fetching are important problems.
