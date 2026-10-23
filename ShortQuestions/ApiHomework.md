1. What is GraphQL
GraphQL is one of the API protocols uses a single endpoint with a query language to communicate with bakcend and can flexiabllly get the date needed.


2. What problems GraphQL is trying to sovle?
The problem GraphQL solved is glexible data fetching, comparing with restfulapi, GraphQL does not need to create a new endpoint on slightly different request, it allows client to reqauest exactly the fields and data it nees in one request.

3. How to use GraphQL?
The backend will have the schema that describes the available data and operations, client side send the request, the backend resolvers handle those fields and fetch the actual data based on the schema from the database.
4. What are the differences between REST and GraphQL?
Restful API usually has multiple endpoints, and the server determine the response structure. GraphQL usually has one endppoint, and the client chooses the fields it wants.
GraphQL is more flexible, but it has extra complexxity and N+1 query problem, and not easy to cache.