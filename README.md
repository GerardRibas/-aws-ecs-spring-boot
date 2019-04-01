# AWS CloudFormation ECS [![Say Thanks](https://img.shields.io/badge/SayThanks.io-%E2%98%BC-1EAEDB.svg)](https://saythanks.io/to/GerardRibas)

This repo contains two CloudFormation templates that demonstrates how to create and deploy services and tasks in ECS using AWS ECS-Fargate or ECS-EC2. 

The main design of these templates have been created following these considerations:

- High Availability
- Auto Scaling
- Metrics Enabled
- Logging through CloudWatch

Both templates are using the VPC created in [this repository](https://github.com/GerardRibas/aws-cloudformation-vpc)

## ECS - Fargate

As the official documentation mentions the main advantage of ECS - Fargate is that you don't need to think in provision EC2 instances or managing them. 

The template creates:

- Application Load Balancer (ALB):
    - Deployed in the Public Subnets.
    - Security Group that allows traffic in and out to the Listener Port (default is 8080) to everywhere.
    - A Target Group with Target Type as IP.

- Elastic Container Service (ECS):
    - An ECS Cluster.

    - A Task Definition with:
        - RequiredCompabilites: FARGATE.
        - NetworkMode: awsvpc. (Required when using Fargate)
        - Cpu and Memory passed in as a parameter.
        - Container Definition with the ALBPort Mapping.
        - Task Execution IAM Role, that allows to get images from ECR and put logs to CloudWatch.
        - Logging to CloudWatch
    - An ECS Service:
        - Launch Type: FARGATE
        - Desired Count passed in as a parameter.
        - Link Application Load Balancer.
        - Link Task Definition.
        - Network Configuration: AwsvpcConfiguration required that deploys containers in private subnets.  
        - Security Group that allows web traffic (80,443) and the ALB Port (default 8080).
	- CloudWatch Log Group used for logging container events. 

	- Auto Scaling:
		- CloudWatch Alarm for MemoryUtilization Metric in AWS/ECS Cluster and Service when metric is greater than or equal than 70%.
		- CloudWatch Alarm for MemoryUtilization Metric in AWS/ECS Cluster and Service when metric is less than or equal than 69%
		- Scalable Target for ECSService that defines min capacity of 1 and max capacity of 20.
		- Scaling Out Scaling Policy for the ECS Scaling Target that defines the step adjustments:
			- 70% - 85%: Add one task
			- 85% - 95%: Add two tasks
			- \> 95%: Add three tasks
		- Scaling In Scaling Policy for the ECS Scaling Target that defines the step adjustments:
			- 59% - 69%: Do not remove any task
			- 49% - 59%: Remove two tasks
			- \< 49%: Remove three tasks

## ECS - EC2 Instances

This Cloudformation template is creating the same resources as the Fargate Cloudformation template plus all the required resources to manage EC2 instances to deploy the application containers.

The template creates:

- Application Load Balancer (ALB):
    - Deployed in the Public Subnets.
    - Security Group that allows traffic in and out to the Listener Port (default is 8080) to everywhere.
    - A Target Group with Target Type as IP.

- Elastic Container Service (ECS):
    - An ECS Cluster.

    - A Task Definition with:
        - RequiredCompabilites: EC2.
        - Container Definition with the ALBPort Mapping and the MemoryReservation passed in as a parameter.
        - Task Execution IAM Role, that allows to get images from ECR and put logs to CloudWatch.
        - Logging to CloudWatch
    - An ECS Service:
        - Launch Type: EC2
        - Desired Count passed in as a parameter.
        - Link Application Load Balancer.
        - Link Task Definition.
        - Security Group that allows web traffic (80,443) and the ALB Port (default 8080).

	- CloudWatch Log Group used for logging container events. 

	- Auto Scaling ECS Service:
		- CloudWatch Alarm for MemoryUtilization Metric in AWS/ECS Cluster and Service when metric is greater than or equal than 70%.
		- CloudWatch Alarm for MemoryUtilization Metric in AWS/ECS Cluster and Service when metric is less than or equal than 69%
		- Scalable Target for ECSService that defines min capacity of 1 and max capacity of 20.
		- Scaling Out Scaling Policy for the ECS Scaling Target that defines the step adjustments:
			- 70% - 85%: Add one task
			- 85% - 95%: Add two tasks
			- \> 95%: Add three tasks
		- Scaling In Scaling Policy for the ECS Scaling Target that defines the step adjustments:
			- 59% - 69%: Do not remove any task
			- 49% - 59%: Remove two tasks
			- \< 49%: Remove three tasks
			
    - EC2 Container Instances Management:
    
        - AutoScalingGroup for EC2 Instances with Min and Max Size passed in as a parameter. Those instances will reside in the private subnets.
        - Launch Configuration for Container Instances configured in order to lunch and add the instance to the cluster.
        - Security Groups to allow Basition Host to SSH in and Ephemeral ports from Application Load Balancer.
        - Scaling Policy Up/Down to add more instances to the cluster.
        - CloudWatch Alarms to fire Scaling Policies based on memory reservation in ECS Cluster.

