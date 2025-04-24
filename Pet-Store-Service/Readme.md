Team Members :

1- Kunal Gautam Pote 
2- Roshan kumar Gupta
3- Himansh Maheshwari
4- Sanket Patil
5- Adit Khurana
6- Sahith 
7- Selina jenifer

Relationships Between Entities:

Category and Pet:

	@ManyToOne relationship is implemented from Pet to Category.
	because, Many pets can belong to one category.

Tag and Pet:

	@ManyToMany relationship is implemented between Pet and Tag.
	One pet can have multiple tags (e.g., a pet can be "Playful" and "Small"), and one tag can be associated with multiple pets.

Customer and Order:

	@OneToMany relationship is implemented from Customer to Order.
	One customer can place multiple orders.

Order and Pet:

	@OneToOne between Order and Pet.
	One order corresponds to exactly one pet.


We developed a Pet Store Service application using Spring Boot to handle all essential CRUD operations related 
to Pets and Customers. The goal was to create a scalable, maintainable, and efficient service while following 
industry best practices in coding, testing, and deployment. We ensured that the application adhered to clean 
architecture principles, making it easy to extend and maintain.
For build and dependency management, we used Maven, allowing smooth integration and dependency resolution. 
The application supports Postgresql databases, providing flexibility based on different deployment environments. 
To enhance usability, we integrated Swagger UI (OpenAPI UI), enabling seamless API exploration, testing, and 
documentation.
From an architectural perspective, we ensured a well-layered structure, incorporating DTO-DAO conversions 
for clear separation of concerns. We maintained strict coding standards and naming conventions, ensuring a clean
and easily understandable codebase.
To ensure high-quality code and thorough testing, we implemented:
JaCoCo for code coverage analysis, achieving 66% coverage.
SonarQube for code quality checks, achieving 77.2% coverage, improving security, maintainability,
and reliability.
JUnit and Mockito for unit and integration testing, ensuring robust functionality across all modules.
We also implemented efficient exception handling, ensuring that invalid operations return appropriate 
custom exceptions with meaningful HTTP status codes, improving API reliability and user experience.

All source code was maintained in Github, where we followed a structured branching strategy. 

(feature, develop, and main branches). This strategy ensured code integrity and facilitated smooth collaboration
among team members through code reviews and continuous integration (CI/CD) pipelines.
Use Cases Completed
1. Pet and Customer Management
   We implemented CRUD operations for both Pets and Customers with proper validation, exception handling, and 
2. structured responses.
2. Entity Relationships
   A Customer can purchase one or more Pets.
   Once purchased, a Pet is linked to the respective Customer and will no longer be available for sale in the 
3. inventory.
3. Pet Return Policy
   A pet can be returned within one week of purchase.
   If a return is attempted after one week, the system throws a custom exception with a relevant error message.
   Returned pets are re-listed in the inventory, making them available for other customers.
   Additionally, we implemented transaction management to ensure database consistency, preventing data corruption in 
4. case of failures.