# curio-q
A web application designed for users to ask and answer questions on a wide range of topics.

## Features and Functionalities

### Actors

- User 
- Admin

### System features

User Service

- [x] User can register themselves with authentication (Basic and OAuth) : `User`
- [x] User has profiles to manage user details (user_name, name, email, password) : `User`
- [x] User can follow other users : `User`
- [x] User can remove other users, can reset passwords : `Admin`

QA-Service

- [ ] Users can ask questions : `User`
- [ ] Users can reply to questions as answers : `User`
- [ ] Users can like questions and answers : `User`

Search Service

- [ ] Users can search questions using keywords : `User`
    > Extended Requirement : Users can search questions with type ahead suggestions : `User`

Feed Service

- [ ] Users can see feeds containing questions from :
  - Followed users
  - Own questions
  - Random questions (if there are no questions or user is following other users)

Notification Service

- [ ] Notify users when user's questions and answers are liked by other users.
- [ ] Notify users when users are followed by other users.
- [ ] Notify users when a new answer is added for their questions.

Analytics/Monitoring

- [ ] Visualize utilized resources using Kibana, Elastic Search.
