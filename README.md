# JMH & JDK 15

Just playing with [Java 15 Records](https://openjdk.java.net/jeps/359) 
and observe performance impact with [JMH](https://openjdk.java.net/projects/code-tools/jmh/)

## How to launch 
Package: `mvn clean package`

Run: `java --enable-preview -jar gamebench.jar` 

## OS Execution context
```
OS           Microsoft Windows 10
Model	     NUC8i5BEK
Processeur   Intel(R) Core(TM) i5-8259U CPU @ 2.30GHz, 2304 MHz, 4 cœur(s), 8 processeur(s) logique(s)
RAM          16,0 Go
```
    
## Benchmarks

### Test 1: [Using classes & some side-effects](benchmark/bench1.md)
### Test 2: [Using records](benchmark/bench2.md)
