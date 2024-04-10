# pixels-rover
The web UI of Pixels.

## Install Step-by-Step
Adjust the configuration in the `application.properties` file

```yaml
spring.datasource.username=pixels # your_username
spring.datasource.password=password # your_password
server.port=8081 # pixels_rover port
```

Login MySQL and create a pixels_rover database for Pixels:
```sql
CREATE DATABASE pixels_rover;
GRANT ALL PRIVILEGES ON pixels_rover.* to 'pixels'@'%';
FLUSH PRIVILEGES;
```