# Bước 1: Lấy JDK nhẹ
FROM openjdk:21-jdk-slim

# Bước 2: Tạo thư mục trong container
WORKDIR /app

# Bước 3: Copy file JAR từ máy vào container
COPY target/identity_service-0.0.1-SNAPSHOT.jar app.jar

# Bước 4: Expose port ứng dụng
EXPOSE 8080

# Bước 5: Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]


#Docker-compose.yml: chạy nhiều container trong 1 file
#
#Dockerfile: build image -> sau đó run
#- tạo network và kết nối với 2 container đó
#- mysql
#  + docker run -d --name mysql --network mynetwork -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=identity_service  -p 3306:3306 mysql:8.0
#- identity-service (cần thêm env cho cho spring_datasource_url)
#  + docker run -d --name identity-service --network mynetwork -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/identity_service identity-service:latest





