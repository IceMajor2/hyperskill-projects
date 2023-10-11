# account-service

My implementation of the project 'Account Service' from JetBrains Academy on Hyperskill. It is a simple account management system which emphasizes the importance of structured logs and clear, single responsibility endpoints.

## What's here?
- authentication and authorization using Spring Security
- separation of business and administrative functions
- H2 database
- action logs are saved to database and are used for prevention of brute force attacks

## Endpoints
|							| Anonymous	| User | Accountant | Administrator | Auditor |
|---------------------------|:---------:|:----:|:----------:|:-------------:|:-------:|
| POST api/auth/signup		|     +     |   +  |      +     |       +       |    +    |
| POST api/auth/changepass	|     -     |   +  |      +     |       +       |    -    |
| GET api/empl/payment		|     -     |   +  |      +     |       -       |    -    |
| POST api/acct/payments	|     -     |   -  |      +     |       -       |    -    |
| PUT api/acct/payments		|     -     |   -  |      +     |       -       |    -    |
| GET api/admin/user		|     -     |   -  |      -     |       +       |    -    |
| DELETE api/admin/user		|     -     |   -  |      -     |       +       |    -    |
| PUT api/admin/user/role	|     -     |   -  |      -     |       +       |    -    |
| PUT api/admin/user/access	|     -     |   -  |      -     |       +       |    -    |
| GET api/security/events	|     -     |   -  |      -     |       -       |    +    |

## Logs
| Description                                                  | Action				|
|--------------------------------------------------------------|--------------------|
| A user has been successfully registered                      | `CREATE_USER`     	|
| A user has changed the password successfully                 | `CHANGE_PASSWORD` 	|
| A user is trying to access a resource without access rights  | `ACCESS_DENIED`   	|
| Failed authentication                                        | `LOGIN_FAILED`    	|
| A role is granted to a user                                  | `GRANT_ROLE`      	|
| A role has been revoked                                      | `REMOVE_ROLE`     	|
| The Administrator has locked the user                        | `LOCK_USER`       	|
| The Administrator has unlocked a user                        | `UNLOCK_USER`     	|
| The Administrator has deleted a user                         | `DELETE_USER`     	|
| A user has been blocked on suspicion of a brute force attack | `BRUTE_FORCE`     	|

## Installation

In order to get this project launched on your machine, see the instructions below.

1. Download this directory (it's not repository) through, for example, this link:
```shell
https://download-directory.github.io/?url=https%3A%2F%2Fgithub.com%2FIceMajor2%2FJetBrains_course%2Ftree%2Fmain%2FAccountService
```
2. Open project directory
3. Run using Gradle
```shell
gradle bootrun
```
