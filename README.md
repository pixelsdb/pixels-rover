# pixels-rover
The web UI of Pixels.

## Install Step-by-Step
Login MySQL and create a user and create a pixels_rover database for Pixels:
```sql
CREATE USER 'pixels'@'%' IDENTIFIED BY 'password';
CREATE DATABASE pixels_rover;
GRANT ALL PRIVILEGES ON pixels_rover.* to 'pixels'@'%';
FLUSH PRIVILEGES;
```

Use `db/pixels_rover.sql` to create tables in `pixels_rover`

Adjust the configuration in the `application.properties` file

```yaml
# mysql username
spring.datasource.username=pixels
# mysql password
spring.datasource.password=password
# pixels_rover port
server.port=8081
# text to sql url
text2sql.url=http://localhost/text2sql
# pixels server port
pixels.server.port=18890
```
