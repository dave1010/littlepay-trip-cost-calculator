## Trip Cost Calculator, by Dekel Pilli

### How to run
To build the app, you need to be using Java 11 and Maven 3.5.0+ (preferably 3.6.1). 
Once you have set this up, simply run `mvn clean install` from the parent directory (where this file is).

Once you have built the app, you can run it by executing the JAR. Again with Java 11, run the following command from the parent directory:
`core/target/Trip-Cost-Calculator-App.jar`

By default, the app will look for input in `input/taps.csv`, and will output to `output/trips.csv`. You can overwrite with the `input` and `output` arguments:
`core/target/Trip-Cost-Calculator-App.jar --input=core/src/test/resources/taps.csv --output=someOtherOutput.csv`

Alternatively, from the `core` directory, run `mvn spring-boot:run`. Note that you will have to change the input for this.

### Assumptions
1. We can't have two ONs without an OFF for the same user on the same bus
1. We can't get an OFF without a corresponding ON for that user
1. The data given is already sorted by time (earliest event first), as per the example
1. Just as the input should be sorted, so should the output
1. We have the cost mappings for each pair of stops
