# API Gateway and Service Registry

These are straight away taken form spring initializer with few changes made in Zuul gateway, and no changes made in the eureka server, except for adding `@EnableEurekaServer` annotation on main class.

In Zuul gateway, however, few changes are made using `application.yml`. 

This is due to the fact that security is implemented in each service, rather than a gateway, and thus we need to pass the Authorization Bearer token