A sample project to practice different aspect of microservices.

It has four microservices : user, account, agregator and naming server. 

This is an dummy project replicating part of banking system. 

USER microservices has user detail. 

Account microservices has Account details. Account has association with User 

Aggregator microservices will fetch user data and then fetch corresponding account details using feign client and aggregate them and 
return user or list of user. 
