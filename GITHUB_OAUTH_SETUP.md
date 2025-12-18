# GitHub OAuth Setup Guide

This application uses GitHub OAuth to authenticate users and access their repositories.

## Setting up GitHub OAuth Application

1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click on "OAuth Apps" in the left sidebar
3. Click "New OAuth App"
4. Fill in the application details:
   - **Application name**: Operational Excellence (or your preferred name)
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
5. Click "Register application"
6. Note down the **Client ID**
7. Click "Generate a new client secret" and note down the **Client Secret**

## Configuring the Application

Set the following environment variables before running the application:

```bash
export GITHUB_CLIENT_ID=your_client_id_here
export GITHUB_CLIENT_SECRET=your_client_secret_here
```

Or add them to your `application.properties` file (not recommended for production):

```properties
spring.security.oauth2.client.registration.github.client-id=your_client_id_here
spring.security.oauth2.client.registration.github.client-secret=your_client_secret_here
```

## Running the Application

```bash
./mvnw spring-boot:run
```

## Using the Application

1. Navigate to `http://localhost:8080/github/repositories`
2. Click "Login to GitHub"
3. You will be redirected to GitHub to authorize the application
4. After authorization, you will be redirected back to the application
5. Your GitHub repositories will be displayed

## Logging Out

Click the "Logout" button to end your session.
