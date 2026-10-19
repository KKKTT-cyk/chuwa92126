
# GraphQL Interview Questions

## 1. What is GraphQL?
GraphQL is a query language for APIs that allows clients to request exactly the data they need.

## 2. What Problems Does GraphQL Solve?
- **Over-fetching:** REST APIs may return unnecessary data.
- **Under-fetching:** REST APIs may require multiple requests to retrieve related data.
- **Multiple endpoints:** GraphQL typically uses a single endpoint (`/graphql`).

## 3. How to Use GraphQL?
- **Schema:** Defines available data and operations.
- **Query:** Retrieves data.
- **Mutation:** Creates, updates, or deletes data.
- **Subscription:** Provides real-time updates.
- **Resolver:** Retrieves or modifies data on the server.

Example:

    query {
      user(id: 1) {
        name
        email
      }
    }

## 4. REST vs. GraphQL

| REST | GraphQL |
|---|---|
| Multiple endpoints | Usually one endpoint |
| Server defines response structure | Client selects response fields |
| Can cause over-fetching | Reduces over-fetching |
| May require multiple requests | Can retrieve related data in one request |
| Uses HTTP methods | Uses queries and mutations |
| Simpler HTTP caching | More complex caching |
| Simpler implementation | More flexible but potentially complex |

