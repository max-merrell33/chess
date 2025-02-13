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

A link to the sequence diagram for the endpoints: [Click here](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIB4GcQC9IC4DMAGAfAYQBaTzzQDKkAjgK6QB2AxjACIgCGA5gE6sC2AOrQTI0AJhwBZVgA9o4yJ06Rw4aAApakYAEkmqaDx7zO6dAEoBuUtBEAWTACh7resAD2naLnAg6wewAdWTlB6EEDaYDJ5ADd5AKCQsNYI6AAJZIATKE544JBQ8MjyTmj8yFzEwugmVmBWAEF6RmJ7DNrWACNWeBgMjsdfTgBPeEDQ2nZoTAA6AE57Llcqf2gAYlpWaKHoACVIdhB4YG5QV1o1gHd8MHKvHxSAWmwokvk9AG0ABQB5UgAVAC60AA9FQepwBABvABEYPkG0M0NQ0Og0IANKjAsQLu4MkiUejUZAeKwQOB8aiAL72YqxDxPNKZbJ6GFwzgIyAUwnQrHwHGcPHI1EY6HE0nkoXQ6npWhZeTQBm0sp6RQHI7yVR7NXAeR7ahEYCmGkxMoK541OqNZrwPTsTQAVXBqjZHKNFoaTSIJAZfVQADEQLLoI75O7oB1ti7eOVWOBIiHOGGMq4iNBaK5IpApIc-O6rV6FYqTYw9LQqMpjSVTQy857iKh6IpapAE87we63e188QzdBffUMhlg+32pXSoxe7XrQ2mzr6lRgPhVKwF-gO-YpwWfR1UAOh-PF+6x9WGTK5Zw9Fqc7qiOW-GfsoXnrS3tBWeCOUjUcLUSvF39XAAazoL8CWpF96QZO5fD0MRMChWEP2jUCf2hP98AA4DaBQqV7CUHph1DdpoFjJsMm2LMc3gDcuzrb0iyrEtCMTUclQnB5TyZV8r3VTg9ngO97AfeUOOfGJXxhQxiA4Tk9GhABRBR3D0UjIFYcjoDqLDcIgp9PG8GDoDsdAEKk+AZNAxTOGUkjwDI7YtJA6k6AyPDZUcRxFmWNZ3GSO1oAAGVcA5zlWK4bnsaDHjE14L2gL5fkBEEemIEAzgQqNEUlEVeX5QUwLHETnmEuL33hZDssxbo+VxLlpS4+kYvHNBoHAYLA1UIKQr1GgjiNNiYBrWjp2gO1gFbTLIAxXLcU7S06N7X1oADIMEzDCNoEmkigxmgUnDjZiw0oo5qM3HtRJeZq9DW1ji0G81hq9PRGzUudV1UA813aOaPWtRad2gPdAdXI8Br0kq9C6wN+MEkq9IgllEPKrLv25dDMJAuSqUKyDnii4BYMweDaDK9kKtRkV0aAzHUec8ACJuuo+xTEh00zbMjho+a-ougbS3LcBj3YzjZWZQL2toGG4yEhr4fE0roTMiy5KsmyqA2Vd3GEPFwPlvT8b0OwAEZTK9ZXUVVuL1fQrWUB1tzXIdjyFms7zVi4SA6HF9glkiMLrh1SKDOiy7X3eJh5IC+S-nkoFgRSxB0o1-9qdoRxdJF88VNXDG07hhirta4Lfc64uFx6g1+ruydHvrUbNE+5cc9Tn7u29Z5fRW-cQeIjaqaw-bIk+o6OeAU7a-oprlWBw9bsY+7qgn1AMiUTRIEb4fvq536tw7gG9h4VxYhnr66iFwbM7Frrfal+9Zd5+WWV12L9eDgnrCJqE6YIzemeTVM2bQGOrmCect578wrGDC6ENvY31vNLfOBcJKKzNnaSySkrbJ3wLbSA9sM5QTfobTAJsSYoOkmglWGC9DW01pwbWuEXJOw8l5FYqxFBDgCjmaAABxaMJB-YRXxr2BG8VuHRzjuwaMAh+50Bxr2GBMi873ynkxbwRxOo5grn1c+NduZPXrsARuijW4LW3P6QM3dZ5Mz7s3AesYh49yZsA8eejzoF2nr-VgOihquJtK1HMvCpKqBMTzPeehuGaFsioQJRBt5tzAYXGJ7p3gAm8ZfV8nCji3xlqLESSCFaSKkkid4MJCmQB0F+I2Ih0A2BFOFHUCZPyVQENCDo4BnCAUaRVAkIoykADloySkpACakciLoGw-sTUpfDinTMMBUvQVSal1IDi2JCWUektLaR0rpGzUL9MGWBEZeF6YwE8czABGYgGjziaY9xTEyyQOrtAhqkNNHwLvrk3GocFZKwoRbKhm0sE4LwXrcZhCjLENNuQ2SALrKYJtnQu2DD3KMOYa7Vh-gqCcH8FATws4YAxMuKsoO9xIj5I+D8f4EipFYNzghA5ezcJjOKq8kiti6AMujAMpl9UvnCLus9AlqhcAEq0YabxD1fG2gbu9YxtzQl9h3F3E+61tiKMHqq4iziFVbnuS1TxkrF7SugC9ZsMTVBJK3mdduSq9BAytWfKB6S4qitepAbJiCmrILKQs6ASybCjPwXjCFcEuXzKYJU6pgaTk-0cawC5rMrnAN1W4lRLVHmC2dayr5eg3XNk9ffClb4yHmX+QpQFNDFwgp0mCghZKiEkMkqg2FFb4XUOBUi3BKLXJos8hitYQwlBtQuNAAAUq4QMPDoxrG2fQQCpLfACtipS+0SVgRlOkRy2gCFcWsCHZwXArg2qcHxAAdVSFoGOwIABCAV6i4AANLcl9ZGxZ0ag1gpzVndlKcsK7vaQeo9J7z2XuvXeh9z7enRj9QGvl55l2FwAFaTtoKoCd0NKC9QlVAqVO865jSMdukJu87UqvOTYv9sj7FaqcaPFx+HJ6XQ8fGo1NqZXAAta+kj50wnLQsdOwwarRowaYKmpjfNBOQCPDR8VuDPDHvcNAQ4aYrmOTTufJ47HlhtB1Bax1rAjTiaeL6e0-hdOEujKDZ5LrUAYclh8nJCGH4rrfJ+l+4KG2TK-rGmAcmhzAaUyp9TTnsiwA4nzez2SILhYeBDZtML0HttsvZTSrBtLUnxrFhGxloVltbZbFSdk1IaXUz23ztGE3-yTezHM4mEnT0zWk79YsouOa9T8xGfyCuVs7fQ5+dJX5eeNnl82ba1Z9eRc5VF7l0VLFYVwfd+k1IeFYP4XF+RahpVCvU24b9ENhwjlHGOcc+g+Yzi1iS8HHz6uelAIIwS2NL1Nfdzgj2bX-UvMSI+ZzlDMXgBiGJAOT652ounGzl3XWvcLfylzdIn5jPrYZMNJNpuuSAA)
