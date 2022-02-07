## Info
This application uses
- Java 11
- Spring Boot REST API
- Docker compose
- Postgres 12.9
- Java Migrations (fryway)
- Tests (Testcontainers)

### Description
The application is an API to get informations of a channels e its videos

### Solution
This application cover some things like:
- Use docker to provide all the solution
- Start a docker database initialising with user and schema in a script sh
- Use Java Migrations to upgrade database

## Build the application

- To build the application you will just need to access the `yt_channel` folder and run `./mvnw clean install`

## Docker

### Deploying the application
1 - Open to `docker-compose.yml` file and insert your Google API Key in `YTCHANNEL_GOOGLE_YOUTUBE_CREDENTIALS`
2 - Go to `yt_channel` folder and copy the jar created at `target` folder with `clean install` to `docker` folder
    - `cp target/yt_channel-0.0.1-SNAPSHOT.jar docker/`
3 - Back to `youtube-channel-api` folder and run `docker-compose up -d` to create all images and start them

### Information
- The Application is running in port `9007`, can be accessed with this link: `http://localhost:9007/swagger-ui.html`
- The PgAdmin can be accessed with this link: `http://localhost:16543/browser/`
    - User: `mail@mail.com`
    - Pass: `PgAdmin4!`
- The Postgres is running in port `15432`
    - If you are using your own DB manager, you need to connect to `localhost:15432`
    - If you are going use PgAdmin in docker, it has an auto-sharing network, so the connection is `postgres:5432`
    - User: `root`
    - Pass: `root-password`

## Additional commands
- `docker rmi --force $(docker images -q)` -> remove all images
- `docker kill $(docker ps -q)` -> Stop all containers