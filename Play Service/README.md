# Play Service

The whole point of this service is **to provide the user with a link** to the media resource.

You may be wondering why create a separate service just to provide a link to resources? why not merge this with view service?

A simple answer is, it'll overburden the view service.

Since we are trying to build a cloud-native application, functions that don't are atomic and frequently used should be separate.

##Flow:

* The client sends a request with a JWT access token (issued by account service) and a `track_id` at `/play`
* the service validates the JWT token using the Shared Secret key.
* upon token validation, the service generates a time-bound link to the resource (AWS CloudFront) and returns it to the user.

## Usage:

### Requirements:
	
 1. AWS CloudFront Distribution [[how?](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/distribution-web-creating-console.html)]

 2. AWS CloudFront Credentials (key-pair) from IAM [[how?](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/auth-and-access-control.html#:~:text=If%20you're%20using%20the,keys%20or%20by%20signing%20requests.)]

 3. A private key shared with the account service 

You will receive the CloudFront private key in .pem format, but you need to convert it into .der. You may use Openssl to do this. [refer this for more](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-trusted-signers.html#private-content-reformatting-private-key)

### Procedure:

All variables where a value is required are specified in the application.properties file
this includes:

 * shared secret
 * AWS region - where your CloudFront distribution is active.
 * AWS private key path of the file in .der format
 * Cloudfront distribution domain name -from CloudFront console
 * Cloudfront keypair - from AWS IAM credentials

You may change all values as per your requirements.

You are also required to specify the link to the Eureka service registry to access this service through the API Gateway

Refer to the TODO comments for more changes.

## Working and Code explanation:

The  Controller consists of only one endpoint --> `/play`

This uses the Post method to get TrackRequest which consists of just a String `track_id`. This value is obtained from the view service and points to the track which the user wants the link to.
The request also consists of an Authentication Header which consists of a Bearer token. this is obtained from the account service.

upon receiving this request, the controller method calls the `AwsUrlService.getUrl()` and returns the link.

The getUrl() method is implemented just as explained in the AWS Cloudfront [documentation](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-creating-signed-url-canned-policy.html). Here we specify our AWS credentials and the path to the resource to generate the link.

For checking the authentication header in the request, the WebSecurityConfig makes use of the JwtRequestFilter. 
This filter extracts the token contents and verifies the token signature against the shared secret using the JwtUtil class. It then gets checks for authorization. This currently is ignored and would we required in the future when the subscription feature is enabled.


## Security

The Music files are the primary resource that we need to protect _(Else we'll be out of business)_.

So to secure the access to the files stored in our S3 bucket, the CloudFront offers us to create a time-restricted URL. However, this needs to be issued by us to authorized users.

Now, authorization is a challenge since we don't directly have access to the user data since it is in the account service. One way would be to access account service each time the user requests a link, but this would defeat the purpose of keeping this as a separate service.

Hence, we make use of a JWT token instead, which is issued by the account service to the client. This token is signed and hence can be trusted by the play service for authorization.