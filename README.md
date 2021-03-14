# Name
ViewApp

<br>

# Features
 
This application can show visualized graphs using data extracted from other applications. 
You can check your health from past few days to date.

The connection with other application is conducted by Oauth2 Authorization Code Grant.
The application access the resource owner's information with token issued by the authorization server.

    |ViewApp|   <---------------------------------->   |Authorization Server|
      |             (authentication/authorization)　
      |              (issued access token)
      |      
      | 
      | 
      -------------------->   |Other Application|
         (access token)


Therefore, this application never contain any data. (No use databese)

This can behave like a viewer using other application's data only.

<br>

# Usage
Please access to the below link.<br>


<https://chocopon-forest.com/ViewApp/#!/>

<br>

If you want to run by yourself, please build by maven install and run a created war file on Tomcat.

<br>

# Requirement
The application is running on AWS EC2. 

<br>


# Technologies
* Maven 
* J2EE
* JAX-RS
* AngularJS
