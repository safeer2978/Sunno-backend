# Account Service

This service is responsible for maintaining user information and authentication. It provides the user with a JWT access token which the user may use to access services.

the user may access the play-service using the token obtained from this service.

## Usage Flow:

1. The client sends a signup request to `/sign_up`. Receives a status boolean.
2. Client logs in using login requests to `/sign_in`. Receives an Access token(JWT), refresh token and expiration date for an access token.
3. Using the JWT access token, the client may access the authorized services till expiration time. After the access token has expired, the client may request a new token by sending refresh token to `/refreshToken`. This will return a new JWT access token.
4. the refresh token has a longer expiration, upon which the user may have to authenticate again.


## Data Model:

The main entities we're persisting are: -
	1. User
	2. Role
	3. RefreshToken

The relations between each of these entities given in the figure.

![alt text](https://github.com/safeer2978/Sunno-backend/blob/master/Diagrams/account-service-datamodel.png)

Each user is issued a new refresh token on each signin, which we keep a record of. therefore the user has a one to many relations with RefreshToken
User may be a `FREE_USER` or `PREMIUM_USER`, depending on Subscription(To be implemented later), and may also have other access (like an Artist account, Admin, etc). Hence User has many to many relations with Roles

You may refer to the `account-service-mysql.sql` in the SQL folder for the query to create this schema in MySQL.


The application relies on JPA annotations for mapping relationships; and makes use of Hibernate ORM.

### Schema:

	User(id, first_name, last_name, password, age, gender, email, imageUrl);
	Role(id, name)
	user_roles(user_id, role_id)
	JwtRefreshToken(id, token, user_id, expirationDateTime)


## API Endpoints:

* `/sign_in` 		- `POST` Takes email and password, returns authentication response
* `/sign_up` 		- `POST` Takes
* `/refreshToken`	- `POST` Takes refreshToken and returns authentication response
* `/hello`	 		- `GET` uses Authorization Bearer token. For testing Access token validity

## Future improvements:

It'd make sense to have this service in the API gateway itself, along with the option to sign-in with third-party service.