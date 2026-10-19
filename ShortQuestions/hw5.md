## API Design Homework

### Dengtai Wang

#### Conceptual Questions

1. **What is GraphQL?**

   Answer:

   ​	GraphQL is an API protocol, it uses single endpoint with a query language to let client decides what data needed. 

2. **What problems GraphQL is trying to solve?**

   Answer:

   ​	It solved in different platform, same API call may need different data. Like for normal web, the API call needs everything to get a comprehensive analyze; but in mobile platform, we only need to show brief infomation. Without GraphQL, we have to either create multiple API calls for different platforms or fetch response depends on platforms.

3. **How to use GraphQL?**

   Answer:

   ​	To use GraphQL, we need to think about the schema of datas, like types and relationships. And then design the query to get what data we need. The use case of the GraphQL is that we need diverse clients with different data needs.

4. **What are the differences between REST and GraphQL?**

   Answer:

   ​	REST exposes multiple endpoints, but GraphQL exposes one endpoint and lets the client specify exactly what data it wants.

   ​	For use case, REST is usually a good choice when the API is simple, CRUD-based, and HTTP caching matters. And GraphQL is often better when the frontend needs flexible combinations of related data, especially when different clients like web and mobile need different fields.