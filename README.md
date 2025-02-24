# Java Neural Network

###  About
An open-source neural network engine and frontend GUI implemented in pure Java.

<img src="readme-docs/user-interface.png" alt="User Interface" width="800"/>


## Getting Started
### Development Prerequisites
This project was created with IntelliJ IDEA, but any IDE (or text editor + environment) that supports Java and 
Maven should suffice.

This project's specified Java version is 23, but most (if not all) of its code is likely backwards-compatible. 

### Cloning
You can clone this repository by opening a terminal, navigating to your desired project directory, and typing:
```
git clone https://github.com/gjinrexhaj/java-neural-network.git
```

Alternatively, you can avoid the CLI if you clone the repository using whichever GUI tool your IDE may ship with. 
IntelliJ users should follow [these steps](https://www.jetbrains.com/help/idea/set-up-a-git-repository.html#clone-repo).

### Maven

As stated previously, this project uses Java version 23.
If you're unable to work with this, you can change the ```maven.compiler.source``` and ```maven.compiler.target```
attributes in the ```pom.xml``` file to suit your needs (provided the source code is backwards compatible with your
selection).

Currently, this project only uses one dependency: FlatLaf for styling the Swing GUI.

To build this program successfully, simply launch ```pom.xml``` with the arguments:
```
clean install
```
Afterwards, all compiled Java bytecode and an executable .jar file will be located under a newly created ```target/```
directory.

TODO: add info about unit testing

## Contribute

TODO: write instructions for contributing

TODO: list contributors by pfp