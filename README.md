# Algorithm based share taxi TANNAE
```
This service provides taxi service which passengers can ride taxi together with other passengers.

Server will match with passengers that moves on similar path.

The main purpose is to pay low price than general taxi service.
```

## Development Plan
* **Topic**   
```
TANNAE project is a updated taxi service which provides sharing service.  
Passengers can share same taxi which moves on similar path.   
When single service ends passengers who shared same taxi pays the total fee in proportion to distance.  
By this service passengers will pay 40~50% lower price than using normal taxi service.  
```

* **Requirements**    
```
*Main service* : Allows passenger to use the service and drivers can use navigation.  
*Sub service* : Provides account|lost & found|QnA(FAQ)|history|charge services.   
```

* **Tools**  
  - ***Client[Android]***   
  ```
  IDE : Android Studio    
  Language : Java   
  UI design : XML     
  ```
  - ***Server[AWS EC2 Ubuntu]***  
  ``` 
  IDE : Visual Studio Code    
  Framework : Express.js(Node.js)   
  Language : Javascript
  Database : InnoDB engine based MySQL
  ```
  - ***Network Communication(HTTP)***   
  ```
  Retrofit2 : Client(request) to Server(response) communication library    
  Socket.io : Bi-directional communication library    
  ```
  - ***Code Manage***   
  ```
  GitHub 
  ```
  
* **Team**  

***Choi Jae Won[Leader]***  
```
Back-end Developer  
  Server & Algorithm Develop
  Build Database 
  Client-Server Network Develop
```
***Kim Dong Hyeon***  
```
Front-end UI Developer
  UI/UX Design * Develop
  Map Develop
```  
***Lee Seung Chan***  
```
Front-end Java Develop
  Client View & Event handler Develop
  SharedPreferences Develop
```

## Analysis 

* **Requirements Statement**
  - ***Functional Requirement***  
  ```
  Function for Account    : Sign In | Sign Out | Register | Withdrawal | Find Account | Edit Account
  Function for Driver     : Check service request | Change operation status
  Function for Passenger  : Request Service | Rate driver
  Function for User       : Lost & Found | QnA(FAQ) | Check History | Charge point
  ```
  - ***Non-Functional Requirement***
  ```
  Client/Server application   : Project is developed based on Client/Server.
  User Familiarity            : Develop Ui/UX intuitively for easy using.
  System Scalability          : Easy maintenance
  System Connectivity         : Server must track users position and update database in real time
  System Response Speed       : Response time must be faster than 1s for single request
  Security                    : Users private info must be secured
  ```
* **Use Case**  
  - ***Use Case Diagram***    
  ![image](https://user-images.githubusercontent.com/87649850/172292202-1ee5b828-4e94-408d-af57-15467c80557c.png)   
  - ***Use Case Description[Exmaple(Sign Up)]***    
  ![image](https://user-images.githubusercontent.com/87649850/172292321-3cf285f1-13a2-42bb-8e23-345bd031b2d6.png)   
* **Diagram**  
  - ***Sequence Diagram[Example(Payment & Assessment)]***   
  ![image](https://user-images.githubusercontent.com/87649850/172292606-bdbeb8cb-8b1e-4118-afeb-cd67300a9bfb.png)   
  - ***Screen FLow Diagram***   
  ![image](https://user-images.githubusercontent.com/87649850/172292634-80fb4887-c3df-44ab-9e75-caf781c32c18.png)   
  - ***Class Diagram***   
  ![image](https://user-images.githubusercontent.com/87649850/172292660-6c32670f-a6f7-4f39-aa6b-cd23fac7f2ab.png)   
  
  
