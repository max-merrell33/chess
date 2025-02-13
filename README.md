# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Sequence Diagram

A link to the sequence diagram for the endpoints: [Click here](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYYAIZj8qCCjgQNQwNAQZgoAAe0MwjOJLJOoNxKnKaB8CAQOJEKjZ2uZpJAkLkKBltI6jIZ2h16jZxlKCg4HD5Lu05qoeNO1pJqlKdpQDoUPjAqVhwHjqVdWvdNtZp29vv9cYTjODodOgIuZQiUKRUEiqhNWFLwL1hSOlxg1w6d3GU0eU2TCfqEAA1ugu6sDi2QadsuZygAmJxObrtoZi+7dr591ID4doUf7MwIAwBgXaGCS+0cMLq6EacO63L6i0ocoywtgy0PwoN1sI6HItR1pg36Ts2xStv0ozqMA5KjgAolA3jlueMaXjA8g7n045gSB8DIDOMAACxOAAzEufSQao0Frn08GIeUyHAKh6Ejgc6AcGYnCmKYXi+P4ATQOw5IwAAMhA0RJAEaQZFkeF5KB1CttUdRNK0BjqAkaBkaKoy7F8LxvB8Y7BpQbLAVcAwdquulPPpSy6QC5yNo+IaGjACBiTysKieJqLorE2LvoYYYZhGZIUk62koLMtnvG6RKZl6HIwNyvKvqewowJFZ68jF7GStKgbyDA16xLeIX3s54LHrKQaBVa5WkjA0aximsL5qm2hxUyEaJeUuYwO1b4GkFJaOa23k8jWgHAU2RQKeWy63FZ5SrE8m7biOK37hOs3Thgc4Llplk6VtG4phtu6naxh6GGlRXyoqyqqhqsTpvFPWfnNLnPkqJpmnVn1mSJHloFNUpAWNOETuZFFUXBCHQOUPjBCm0BIAAXigyxYfNu2yWA5REQAjGRsMwSttGI5lKMJmjmPYxx7GM1xnjeH4gReCg6DA-YvjMJJ6SZJge3MM5inSLBwmwfUsHNC0amqBp3TrUO6CmDto1Aq2Ks7hDWsgvJ33lO5vPxl5Yl875GIBcN9XvSyYVgO1SbnaraBdR6WaFN6KV5imgoZTrav5QN-uniVYBlfbnqfYFfVh-IRYfgSDWRhwKDcJkzuDZ1b3dfe3tJZEwwQDQocFrVtuA5D5TeZbAHgzNsfYVcOPHJ9IsHYuvTXUeOf3QqrJPcVL1YHeMeVa5xqmknI1fjXPP17WjeQ7N0NtuRLJwxTCPlsjm501jmHGThneEU4JO9JvUHk1MlN7zTqSHwzbHM1xPHswEkL+sJ0IwAA4quVkAtpLC3xmydeFR-7SzlvYVcytXa6w1vPfW9FEFqybobKqyBYheWhFbfys87b50auSJ2rUg7uzzp7Xqvty4dSKoHdBaAJRSnoYKCOUcSFZknj9fuwAiHBWjpGNy0JAGQVhB7BK2Ykr-wpGeU0ACgHUOkbw8o4iUCMgqI0QRmsyy13wQ3esq9m7zXMnAyC4wKj9AsSgAAktILsRNZzEQIk8KSmQZSdk+LMbofQdAIFAIOLxy1VpTFsQAOVXP8RoBwT54xyATGA85u42KAVYtJowHFOJcW4qYHjHTHXuKtPxASgkhJOmEvokToljliQePuCdgBGEHkqFUI9NTjx4Vgqef1dEoP0SJQxy9jH6zXi3DeZNqL3yRo-Z+x8NZTnxoTC+pMt63xorvWZB8oAYyPqxTir935sz4tgHwUBsDcHgPaTISjRgpEFjJRJECJlKQaLA+Bj8LpkRqZU-c8S9HAjQf2N2PzVxRL+cfTBX0qrNUyLCOANyUAEKxIIlOwjHbO0oVIj6hcuQ8j9hXRhYRKGsOYPwjppUVEfTUewyuT5iGe3KHClAGjYQaLTF03qA0-R3M0fS76s0gaIpjJkMGoyyzjLMRvWx2TyjONcXExZhQz4pLBVkxx8rcm90MBSh6Q92kR2pRVHpP1p7-SroC1sIqHTir1pK0xJQYbrOmVs6mOy9kM2VbhRJKzL4QRdfDOi7rUa7PpsfV+RzuInMCJYDO7lkgwAAFIQB5HywIZSQCDjAc8sW5ZqiUhUi0WxCCQU7jIpc4AcaoBwAgO5KAKwADqLA7EyxaAAIWEgoOAABpL4srNUwAVQRJV2FTIL0oRWwJ1ba31qbS2ttnbu19qeAOnJir7VOVNeUAAVqmtAsIU2TRQGia2aKZCp0xRQ5hOKC7snxbyClTCy3BzYRSzhxqJ7brpYnAG6LuGOzZQO29Mc8XJQJXygOYQB2fu6TC1yHKgwhxRVjeAdboAwBiG05gzEWGzwKF00ofgtCZDZYh+Q2JYMFG9JSbAJHDDkYEQDK1ZQj2gyMZukyjrwKjtxh3ZZyTDo9waYYFD-pZ0Yaw7hzjUAAA8D445sbtROeTuQzIBpvq64NDEmLAAwgcEWqnoZEVIlfKZQaqY6bCLhiNhybo-uafq7DlKx6pylUbX6M9mMDKBcm-ddqm55udZpizD8PXhrblxpZvrz7+uvpRDZMyQ20zDfs5mUbWa8Q5lAKtuEYywGANgS5hB4iJAeaAkWLzpUVAllLGWctjBLkiwbM4qC2wOTGbSkA3A8CSPPYRpqPW4Qga9veisJcy4KEUTKVQswNGzfoRdDQ6tLU+etUNgLJigvtYBdF-CarhOvyAA)
