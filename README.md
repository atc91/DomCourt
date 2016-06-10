DomCourt
========

DomCourt is a [Docker](http://docker.io/) Management platform for DomJudge servers.

Features
--------

-   Create a virtual server

-   Delete a virtual server

-   Update admin password of a virtual server

-   Allow a user register as participant into a virtual server

Build
-----

DomCourt use two (2) images:

-   domcourt/court: The app which orquest the DomJudge servers

-   domcourt/domjudge: The DomJudge server itself.

You can modify and/or build that images with the following docker commands:

-   docker built -t domcourt/court -f DomCourtDockerFile .

-   docker built -t domcourt/domjudge -f DomJudgeDockerFile .

Run
---

You only need manually the Court container in the following way:

> docker run -d -P -v /var/run/docker.sock:/var/run/docker.sock --link &lt;myslq container name&gt;:db --name court domcourt/court

Where &lt;mysql container&gt; is a [MySQL](https://hub.docker.com/_/mysql/) container with user and password set to *root*

This table resume the Court Restfull API:

| REQ                        | URL                                                                        | TYPE   | HEADERS                          | REQUEST BODY                                                            | RESPONSE BODY           |
|----------------------------|----------------------------------------------------------------------------|--------|----------------------------------|-------------------------------------------------------------------------|-------------------------|
| SignUp                     | http://localhost:8080/DomCourt-web/rest/users                              | POST   | ContentType application/json     | {"username":”&lt;myusername&gt;","password":"&lt;mypass&gt;"}           | JSON Web Token          |
| SignIn                     | http://localhost:8080/DomCourt-web/rest/users/signIn                       | POST   | ContentType application/json     | {"username":”&lt;myusername&gt;","password":"&lt;mypass&gt;"}           | JSON Web Token          |
| UpdateUserPassword         | http://localhost:8080/DomCourt-web/rest/users/changePassword               | PUT    | ContentType application/json Bearer &lt;my JSON Web Token&gt;  |                                                                         |                         |
| Create DomJudgeServer      | http://localhost:8080/DomCourt-web/rest/judges/&lt;my judge name&gt;       | POST   | ContentType application/json Bearer &lt;my JSON Web Token&gt;  |                                                                         | judge url               |
| Get JudgeServer Url        | http://localhost:8080/DomCourt-web/rest/judges/                            | GET    | ContentType application/json Bearer &lt;my JSON Web Token&gt;  |                                                                         | &lt;array of judges&gt; |
| Get all user judge servers | http://localhost:8080/DomCourt-web/rest/judges/&lt;my judge name&gt;       | GET    | ContentType application/json Bearer &lt;my JSON Web Token&gt;  |                                                                         | judge url               |
| Delete a jugde server      | http://localhost:8080/DomCourt-web/rest/judges/&lt;my judge name&gt;       | DELETE | ContentType application/json Bearer &lt;my JSON Web Token&gt;  |                                                                         |                         |
| Create a team              | http://localhost:8080/DomCourt-web/rest/judges/&lt;my judge name&gt;/users | POST   | ContentType application/json Bearer &lt;my JSON Web Token&gt;  | {"username":”judgeusername&gt;","password":"&lt;judgeuserpassword&gt;"} |                         |
