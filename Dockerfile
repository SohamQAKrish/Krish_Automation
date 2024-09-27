FROM alpine:3.14

RUN  apk update \
  && apk upgrade \
  && apk add ca-certificates \
  && update-ca-certificates \
  && apk add --update coreutils && rm -rf /var/cache/apk/*   \ 
  && apk add --update openjdk11 tzdata curl unzip bash maven \
  && apk add --no-cache nss \
  && rm -rf /var/cache/apk/*

# Workspace Directory
WORKDIR /usr/share/OnitSukaTiger

# Add Project's required folders and files
ADD src/ /usr/share/OnitSukaTiger/src/
ADD pom.xml /usr/share/OnitSukaTiger

# Package the Project
RUN mvn clean package -DskipTests 

# Add allure reporting folder
ADD allure-results/ /usr/share/OnitSukaTiger/allure-results/

## debug
#CMD [ "tail", "-f", "/dev/null" ]