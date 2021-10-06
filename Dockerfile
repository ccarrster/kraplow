# syntax=docker/dockerfile:1

# build with
# docker build -t kraplow:latest .

# run with:
# docker run -it --rm -p 8888:8080 kraplow

FROM alpine as build

RUN apk update
RUN apk upgrade
# Install JDK 1.16
RUN apk add openjdk11
# Install GIT
RUN apk --update add git
# Setting Ant Home
# Create Ant Dir
RUN mkdir -p /opt/ant/
# Download Ant 1.10.11
RUN wget http://archive.apache.org/dist/ant/binaries/apache-ant-1.10.11-bin.tar.gz -P /opt/ant
# Unpack Ant
RUN tar -xvzf /opt/ant/apache-ant-1.10.11-bin.tar.gz -C /opt/ant/
# Remove tar file
RUN rm -f /opt/ant/apache-ant-1.10.11-bin.tar.gz
ENV ANT_HOME=/opt/ant/apache-ant-1.10.11
# Updating Path
ENV PATH="${PATH}:${ANT_HOME}/bin"

# build kraplow
WORKDIR /kraplow
COPY . .
RUN ant clean && ant

# Junit tests, junit-VERSION.jar ships with repository
ENV JUNIT_HOME=/kraplow
ENV JUNIT_VERSION=3.8.2
RUN ant test


FROM tomcat:9.0

COPY --from=build /kraplow/dist/westerncardgame.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
# use this for debugging, doesn't start the server
# CMD /bin/bash
