1. What is GraphQL ?
GraphQL is a query language and runtime for APIs that lets clients request exactly the data they need.
2. What problems GraphQL is trying to sovle?
It addresses over-fetching, under-fetching, and complex data aggregation requirements.
3. How to use GraphQL?
   In Java, GraphQL can be implemented using Spring Boot and Spring for GraphQL, with annotations such as @QueryMapping and @MutationMapping.

4. What are the differences between REST and GraphQL?
   Feature	REST	GraphQL
   API design	Resource-oriented	Schema and query-oriented
   Endpoints	Usually multiple endpoints	Usually one endpoint
   Data fetching	Typically predefined response	Client selects response fields
   Over-fetching	Can occur	Reduced through field selection
   Under-fetching	May need multiple requests	Related data can be requested together
   HTTP methods	GET, POST, PUT, PATCH, DELETE	Commonly POST; queries may use GET
   Type system	Not required by REST itself	Strongly typed schema
   Versioning	Often URL or header versioning	Commonly evolves a shared schema
   Caching	Straightforward HTTP caching for many GET resources	Supports caching, but often needs specialized strategies
   Error handling	Heavily uses HTTP status codes	GraphQL errors can coexist with partial data
   Implementation	Often simpler for CRUD APIs	More schema and resolver infrastructure
   Flexibility	Server primarily determines response shape	Client determines requested fields within the schema
   