# Q1.
GraphQL is an API protocol, it uses a single endpoint with a query language 
that lets clients specify exactly what data they need, the server responds with data in exactly that shape, no more or no less.

The query and response schema is the contract between client and server. It uses a strong type system to define what types exist, what fields each type has, and how the types relate to each other.
queries read data, mutations create, update, or delete data, and subscriptions let clients receive real time update.

GraphQL addresses two common Rest problems. the first is over fetching, where a client receives more data that it needs. The second is under fetching where a client has to call several endpoints to assemble on view.
GraphQl also makes an API self-documenting through introspection: tools can query the schema itself, which enables features like autocomplete and type-checking. Front-end teams can also evolve their data needs without waiting for new backend endpoints.

GraphQL has some downside. HTTP caching is harder because most requests go through a single POST endpoint.
Badly designed queries can be expensive, for example deeply nested requests or the N+1 database query problem，
because client can freely compose their own queries, a query that looks short can force the server to a huge amount of work.
So servers often need query depth limits an batching tools like DataLoader.

# Q2.
GraphQL was designed to fix several pain points:   
over fetching:A rest endpoint return fixed data shape, if a mobile app only need name and avatar but an API return 30 fields the app download it never uses.
this wastes bandwidth,with GraphQL the clients ask exactly the fields it needs.

Under-fetching and multiple round trips: 
To render a single page, the REST client might need to call multiple times, GraphQL lets the client get all of this data in a single request.

Endpoint sprawl and tight coupling between front end and back end:
REST team often create custom endpoints for specific screens, with GraphQL the backend expose one flexible schema, and front-end teams can change what they request without waiting for new endpoints.

Can support many different client.

# Q3.
Build a server that exposes a schema and writing a client tht sends queries to it.
1. Define a schema, it's written in the GraphQl Schema Definition Language.
2. Write resolvers, resolvers are functions that return actual data for each field.
3. Start a server.
4. Send queries from a client.

# Q4.
Rest has many endpoints while GraphQL usually has one.  
Rest is defined by server while GraphQL is defined by client.  
Rest is easy to use HTTP cache while GraphQL needs extra tools.