# CrunchDAO App

- [CrunchDAO App](#crunchdao-app)
  - [Services](#services)
  - [Libraries](#libraries)

## Services

| Name | Type | Port | Description |
| --- | --- | --- | --- |
| [Achivement](achivement-service/) | Service | [8010](http://localhost:8010/swagger-ui/index.html) | Store achivements progression |
| [API Key](api-key-service/) | Service | [8001](http://localhost:8001/swagger-ui/index.html) | Manage API Keys and their authentication. |
| [Auth](auth-service/) | Service | [8002](http://localhost:8002/swagger-ui/index.html) | Authentication. |
| [Avatar](avatar-service/) | Service | [8013](http://localhost:8013/swagger-ui/index.html) | Manage user avatars. |
| [Connection](connection-service/) | Service | [8009](http://localhost:8009/swagger-ui/index.html) | Link social account. |
| [Follow](follow-service/) | Service | [8014](http://localhost:8014/swagger-ui/index.html) | Handle following. |
| [Gateway](gateway/) | Gateway | [8000](http://localhost:8000) | API gateway. |
| [GraphQL](graphql/) | API | [8007](http://localhost:8007/graphiql?path=/graphql) | GraphQL endpoint. |
| [Keycloak](keycloak-service/) | Service | [8004](http://localhost:8004/swagger-ui/index.html) | Abstract the Keycloak API. |
| [Model](model-service/) | Service | [8016](http://localhost:8016/swagger-ui/index.html) | Store models. |
| [Notification](notification-service/) | Service | [8012](http://localhost:8012/swagger-ui/index.html) | Store notifications. |
| [Referral](referral-service/) | Service | [8015](http://localhost:8015/swagger-ui/index.html) | Store referrals. |
| [Registration](registration-service/) | Service | [8005](http://localhost:8005/swagger-ui/index.html) | Handle user accounts. |
| [Round](round-service/) | Service | [8018](http://localhost:8018/swagger-ui/index.html) | Handle rounds. |
| [Segment Analytics](segment-analytics-service/) | Service | [8008](http://localhost:8008/swagger-ui/index.html) | Segment.io publisher. |
| [Submission](submission-service/) | Service | [8019](http://localhost:8019/swagger-ui/index.html) | Handle submissions. |
| [User](user-service/) | Service | [8003](http://localhost:8003/swagger-ui/index.html) | Store users. |

## Libraries

| Name | Type | Description |
| --- | --- | --- |
| [Security](security-common/) | Common | Shared security code. |
| [Security Common Test](security-common-test/) | Test | Shared security testing code. |
| [Web Common](web-common/) | Service | Common web classes. |
