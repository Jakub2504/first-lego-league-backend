# First Lego League Backend

Template for a Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch-Igualada/spring-template?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch-Igualada/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch-Igualada/spring-template/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch-Igualada/spring-template/actions)
[![CucumberReports: UdL-EPS-SoftArch-Igualada](https://messages.cucumber.io/api/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0/badge)](https://reports.cucumber.io/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0)
[![Deployment status](https://img.shields.io/uptimerobot/status/m792691238-18db2a43adf8d8ded474f885)](https://spring-template.fly.dev/users)

## Vision

**For** the FLL Igualada organizers, volunteers, and participants **who** need to efficiently manage the competition logistics and track real-time progress, **the project** FLL Backend **is an** event management and scoring system **that** allows for seamless coordination of teams, judges, matches, and multimedia content. **Unlike** manual tracking methods, our system enforces specific FLL rules (such as unique matchmaking constraints), validates volunteer roles, and provides an engaging, mobile-accessible experience for the community.

## Features per Stakeholder

| USER                                                       | ADMIN                                                             |
|------------------------------------------------------------|-------------------------------------------------------------------|
| Register & Login                                           | Register & Login                                                  |
| View match schedules, live scores, and global rankings     | CRUD operations for Teams, Educational Centers, and Volunteers    |
| Access multimedia gallery (photos and videos) of the event | Manage Project Rooms and assign Judges to specific rooms          |
| Post comments to foster community (Authenticated users)    | View specific assessment sheets filled out by Judges              |
| View Team profiles and statistics                          | Generate Matches ensuring a team never plays the same rival twice |
|                                                            | Manage Competition Tables and Rounds                              |
|                                                            | Validate Volunteer roles (prevent Judge/Referee conflicts)        |

## Entities Model

![Entities Model](docs/design-diagram.png)

## Get the Spring Boot app running and tested with these commands:

Build the project
```bash
mvn package
```
Run the app locally
```bash
mvn spring-boot:run
```
Run tests
```bash
mvn test
```

### Run API locally

Using your IDE, execute `MainApplication::main`.

If you are using VSCode, you can just press `F5` from anywhere to start debugging.
