# API Design Homework

1. What is GraphQL
GraphQL is an API protocol that allows clients to request exactly the data they need. GraphQL uses a single endpoint where the client sends a query and specifies which fields needed in the response.

2. What problems GraphQL is trying to sovle?
GraphQL is mainly designed to solve problems like over-fetching and under-fetching. Different clients, such as mobile and web apps, may need different data. With REST, this can require creating more endpoints or returning more data than a client actually needs. GraphQL allows each client request only the data it needs, making data fetching more flexible.

3. How to use GraphQL?
We first defines a schema that describes the available data types, fields, and relationships. The client then sends a query to request the specific fields it needs, and the server returns the data in that requested structure.

4. What are the differences between REST and GraphQL?
REST usually has multiple resource endpoints, while GraphQL commonly uses a single endpoint. In REST, the server defines the response structure, while GraphQL allows clients to request the specific fields they need. GraphQL is more flexible and can reduce over-fetching and under-fetching, but it requires additional work such as schema validation and query processing.