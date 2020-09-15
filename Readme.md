# Introduction

This code is for a water flow simulator that demostrates concuncerrent programming for CSC2002S Assignment 2.

# Usage

A make file has been provided to compile hence in the root directory run the below to initiate the build

``` bash

make

```

Relevant files will be compiled and added to the bin folder in the FlowSkeleton folder. To run the program navigate to
the bin folder via

``` bash

cd bin

```

after navigating into the folder, enter the below command to run simulation

``` bash

java FlowSkeleton.Flow inputFileLocation

```

for example to run the simulation with the medium dataset provided run

``` bash

java FlowSkeleton.Flow ../data/medsample_in.txt

```

Note that it is important that the program be executed once entering the bin folder else the java file fails to work out which
package it is using and hence fail to find the main method and throws an error