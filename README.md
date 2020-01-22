# Payments REST API

## How to Run
1. Execute console command `./gradlew/bootRun` and go to  [http://localhost:9000/payments](http://localhost:9000/payments)
2. Check H2 database status under [http://localhost:9000/h2-console](http://localhost:9000/h2-console)
3. Find the documentation of the REST API under [http://localhost:9000/documentation.html](http://localhost:9000/documentation.html)

## How to Stop
 * `./gradlew -stop`
 
## H2 database credentials
* `sa / secret`

## Payment records for testing are inserted by PaymentDataInitializer script
A total of 9 records are inserted and can be viewed after application start up under `/payments` url
