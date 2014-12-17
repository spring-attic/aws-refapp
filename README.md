# Spring Cloud AWS Reference App #
This reference application acts as a showcase for the features provided by [Spring Cloud AWS] [Spring-Cloud-AWS].

To check out the project and build it from source, do the following:

    git clone https://github.com/spring-cloud-samples/aws-refapp.git
    cd aws-refapp
    mvn package

## Run it on your own AWS environment ##
If you want to start this application on your own AWS environment you just need to do the following steps:

* Choose Ireland as region
* Go to the CloudFormation console.
* Create a new stack
    * Name the stack "AwsSampleStack".
    * Choose "Upload a template to Amazon S3".
    * Upload the _AwsSampleStack.template_ file (located at the root of this project).
    * When prompted for a parameter value *rdsPassword* just type a password of your choice with a min length of 8 characters.

The stack needs a while to start (around 15 to 20 minutes). Once it is complete, you can copy the public DNS address
of the created EC2 instance and open it in your browser with port 8080. 
For example http://ec2-54-72-102-202.eu-west-1.compute.amazonaws.com:8080.

## Running the application locally ##
**Please note that you need a running stack on AWS to run it locally!**

If you want to play around with the application, you can start it locally on your machine. 
In order to start the application you have to create a configuration file that configures the necessary
parameters to connect to the environment.

Please create a new properties file (for example access.properties). This file must contain three properties 
named accessKey,secretKey and rdsPassword. These two properties accessKey and secretKey are account/user specific and 
should never be shared to anyone. To retrieve these settings you have to open your account inside the AWS console and 
retrieve them through the [Security Credentials Page] [AWS-Security-Credentials]. 

*Note:* In general we recommend that 
you use an [Amazon IAM] [Amazon-IAM] user instead of the account itself. The last password rdsPassword is used to access 
the database inside the integration tests. This password has a minimum length of 8 characters. 

An example file would look like this

	cloud.aws.credentials.accessKey=ilaugsjdlkahgsdlaksdhg
    cloud.aws.credentials.secretKey=aöksjdhöadjs,höalsdhjköalsdjhasd+
    cloud.aws.region.static=EU_WEST_1
    cloud.aws.stack.name=AwsSampleStack
    cloud.aws.rds.RdsInstance.password=someVerySecretPassword

Once you created the properties file you can start the application using the following command:

    mvn spring-boot:run -Drun.arguments="--spring.config.location=/path/to/your/properties/file,--spring.profiles.active=local"
    
*Note:* There are multiple ways to start a Spring Boot application, please refer to the 
[Spring Boot documentation] [Run-Spring-Boot] to see all the possibilities.

[Spring-Cloud-AWS]: https://github.com/spring-cloud/spring-cloud-aws
[Run-Spring-Boot]: http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-running-your-application
[AWS-Security-Credentials]: https://portal.aws.amazon.com/gp/aws/securityCredentials
[Amazon-IAM]: https://aws.amazon.com/iam/