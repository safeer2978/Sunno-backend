# Upload Application

This is very different from all other services. It's more of a desktop application, however it's still a Spring Boot application so several features may be added in the future.


## Workflow:

![alt text](https://github.com/safeer2978/Sunno-backend/blob/master/Diagrams/upload-application-flow.png)

1. This application accepts a directory URI and prepares files for upload.
 * It extracts metadata from MP3 files and fits them in a model called MusicData. It then checks for null values and stages files that have all required fields.

2. It then uses google Custom search API to collect relevant image URLs. it does so by using the querying name of album, artist, and genre. Preference is given to Wikimedia links(arbitrary reasons).

3. Further, the user is asked to start the upload. It then uses the AWS.s3 library to upload all files to the s3 bucket. 

4. for each file successfully uploaded, it then makes an HTTP request to view-service. On failure, it retries thrice. Then it prints the request on console.

## Usage:

You'll first need to fill all the required credentials in the application.properties file.

After that, it's pretty straight forward. just Run the UploadServiceApplication and type a directory that consists of .mp3 files. 

You'll also need to specify the link to view-service.
Please Refer to the TODO for more.

### Requirments:

1. AWS IAM user with [s3 previliges](https://aws.amazon.com/blogs/security/writing-iam-policies-how-to-grant-access-to-an-amazon-s3-bucket/).
2. Google API account with [Custom search with image enabled](https://cse.google.com/cse/all)


more links: 
[upload object to aws s3 with java](https://docs.aws.amazon.com/AmazonS3/latest/dev/UploadObjSingleOpJava.html)

## Further work:

There's a lot of room for improvement here. A UI for starters will be great!