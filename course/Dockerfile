FROM openjdk:8
ADD target/*.jar courseservicejar.jar
EXPOSE 8500
ENTRYPOINT ["java", "-jar", "courseservicejar.jar"]