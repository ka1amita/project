- [General](#general)
- [JIRA](#jira)
- [Materials](#materials)
  - [Testing](#testing)
  - [IntelliJ Gradle](#intellij-gradle)
  - [Circleci](#circleci)
- [Swagger](#swagger)

# General

- Add tasks for [design patterns](https://www.tutorialspoint.com/design_pattern/index.htm):
  - Singleton
  - Factory
  - Builder
  - [Adepter](https://www.tutorialspoint.com/design_pattern/adapter_pattern.htm)
  - [Decorator](https://www.tutorialspoint.com/design_pattern/decorator_pattern.htm)
  - Facade

# JIRA

- Add CircleCi story
  - make sure the `mailtrap` (security) context with the smtp key/values (for username and password) is available on circleci
- Remove email verification and password reset from [#11](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-11)
  - remove the verification and password reset db table columns
  - add email verification to [#17](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-17)
	- create the EmailVerification model/entity; make it OneToOne with the User
	- add 'verified_at' column to the User model
  - add password reset model/entity changes to [#19](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-19)
	- create the PasswordReset model/entity; make it OneToOne with the User
- Clarify sending email to only call `EmailUtils.send()` in:
  - User registration = [#15](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-15)
  - User email verification = [#17](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-17)
  - Resend email verification = [#18](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-18)
  - Reset password = [#19](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-19)

- [#9](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-9) rename to 'Cicle CI'
  - move "Google Java Format Plugin" to [#7](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-7)

- [#11](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-11) should have only:
  - id (PK, unique, non null, auto increment)
  - username (unique, not null)
  - email (unique, not null)
  - password (hashed [bcrypt], not null)
    - this should be just plain text passwords at first
    - implement password hashing (bcrypt) in, let's say, user registration [#15](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-15)
  - verified_at (localdatime, default null)
  - created_at (localdatime, not null)
- [#12](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-12) rename to "Permission, Role and Team models and entities"
  - Permission
    - id (PK, unique, non null, auto increment)
    - ability (unique, not null)
  - Role
    - id (PK, unique, non null, auto increment)
    - role (unique, not null)
  - Team
    - id (PK, unique, non null, auto increment)
    - team (unique, not null)
- [#13](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-12) rename to "model relations":
  - have User, Role, Team and Permission models extend the `AbstractAuthorityModel`
  - User should have the following @ManyToMany
    - permissions, roles, teams
  - Add the following methods to User:
    - addPermission, removePermission
	- addRole, removeRole
	- addTeam, removeTeam
  - Role should have the following @ManyToMany:
    - permissions
  - Add the following methods to Role:
    - addPermission, removePermission
  - Team should have the following @ManyToMany:
    - permissions, roles, users
  - Add the following methods to Team:
    - addPermission, removePermission
	- addRole, removeRole
	- addUser, removeUser
  - All methods mentioned above should "forward" the return type (i.e. `boolean`) of the underlying method call, i.e.
```java
  public boolean addPermission(Permission p){
    return permissions.add(p);
  }
```
  - add the `private void initializeDefaults()` method to each of the user, role, team models and call it from the non-args constructor. The method should initialize all (hash)sets so make sure they are non-null 
```java
  public User(){
    initializeDefaults();
  }
  // ...
  private void initializeDefaults(){
    permissions = new HashSet<>();
	// ...
  }
```
  - all other constructors should call the no-args constructor as the very first line
```java
  public User(StoreUserDto dto){
    this();
	username = dto.username;
	// ...
  }
```
- [#14](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-14) rename to Authority models:
  - Skim through [Hibernate Inheritance Mapping](https://www.baeldung.com/hibernate-inheritance) and use the `@MappedSuperclass` annotation in the abstract classes below:
  - create `AbstractModel`
```java
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  public Long getId(){ return id; }
```
  - create `AbstractAuthorityModel` which extends `AbstractModel`
```java
  public abstract boolean can(String ability);
  public boolean can(Permission permission){
    return can(permission.getAbility());
  }
```

- swap the order of:
  - [#15](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-15)
  - [#16](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-16)

- user registration [#15](https://greenfoxacademy.atlassian.net/jira/software/projects/FDP/issues/FDP-15) needs to include hashing the plain text passwords. Use the `PasswordEncoder` class to hash plain text password with bcrypt. The algo complexity (number of cycles) should be configurable via `application.properies`, i.e. `config.security.bcrypt.cycles=14`. See [Recommended # of rounds for bcrypt](https://security.stackexchange.com/questions/17207/recommended-of-rounds-for-bcrypt) for more details
  - the PasswordEncoder instance should be configured via `@Configuration` class
```java
package com.gfa.users.security;

@Configuration
public class SpringBootSecurityConfiguration {
  
  private static final int BCRYPT_DEFAULT_CYCLES = 14;
  
  @Bean
  public PasswordEncoder getPasswordEncoderBean(){
    int cycles = BCRYPT_DEFAULT_CYCLES; 
    try { 
      cycles = Integer.parseInt(
          Objects.requireNonNull(
            environment.getProperty("config.security.bcrypt.cycles")));
    }
    catch(NullPointerException ignored){}
    return new BCryptPasswordEncoder(cycles);
  }
}
```

- add JIRA task for "Use ThymeLeaf templates for emails", e.g. read an example [here](https://codingnconcepts.com/spring-boot/send-email-with-thymeleaf-template/)


- add missing endpoints to JIRA tasks:
  - `/roles/{id}/permissions` (Role.addPermission, Role.removePermission)
  - `/teams/{id}/users` (Team.addUser, Team.removeUser)
  - `/teams/{id}/roles` (Team.addRole, Team.removeRole)
  - `/teams/{id}/permissions` (Team.addPermission, Team.removePermission)
  - `/users/{id}/teams` (User.addTeam, User.removeTeam)
  - `/users/{id}/roles` (User.addRole, User.removeRole)
  - `/users/{id}/permissions` (User.addPermission, User.removePermission)

# Materials

- [Global Error Handling](https://www.bezkoder.com/spring-boot-restcontrolleradvice/)
- [Hibernate Inheritance Mapping](https://www.baeldung.com/hibernate-inheritance)
- [Why Set Is Better Than List In @ManyToMany](https://dzone.com/articles/why-set-is-better-than-list-in-manytomany)
- [Hibernate Many to Many Relations Set Or List?](https://stackoverflow.com/questions/8174667/hibernate-many-to-many-relations-set-or-list)
- [Mocking environment](https://stackoverflow.com/questions/51002731/junit-mock-of-environment-with-mockito-not-working-evaluating-to-null-still)
- [Test coverage](https://www.youtube.com/watch?v=RByR6LqNx5M)
- [Definitive Guide to the JaCoCo Gradle Plugin](https://reflectoring.io/jacoco/)
- [Intellij IDEA keyboard shortcuts](https://www.jetbrains.com/help/idea/mastering-keyboard-shortcuts.html):
  - Use `Ctrl+Alt+L` on Windows or  `⌥ ⌘ L` on macOS to  [Reformat code](https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html#reformat_code)
  - Use `Ctrl+Alt+O` on Windows or `⌃ ⌥ O` on macOS to [Optimize imports](https://www.jetbrains.com/help/idea/creating-and-optimizing-imports.html#optimize-imports)


## Testing

To disable web (spring boot) security for controller unit tests use the following annotation:

```java
@WebMvcTest(
    value = MyController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration
class MyControllerTest {}
```

## IntelliJ Gradle

Show students how to use and run Gradle tasks in InteliJ

## Circleci

# Swagger

- double check class definitions, some still say "abstract" but they shouldn't be