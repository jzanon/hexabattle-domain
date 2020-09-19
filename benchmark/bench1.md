**Description:**  Using classes & some side-effects

**Commit :** `b3ef9b6cc9c6560c1fbc8a7f7659b47b5ede94f2`

**JMH output:** 
```
    # JMH version: 1.25.2
    # VM version: JDK 15, OpenJDK 64-Bit Server VM, 15+36
    # VM invoker: C:\Program Files\AdoptOpenJDK\jdk-15.0.0.36-hotspot\bin\java.exe
    # VM options: --enable-preview
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 1 thread, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: com.aedyl.Game.launch
    
    # Run progress: 0,00% complete, ETA 00:08:20
    # Fork: 1 of 5
    # Warmup Iteration   1: 596,771 ops/s
    # Warmup Iteration   2: 721,271 ops/s
    # Warmup Iteration   3: 692,064 ops/s
    # Warmup Iteration   4: 653,036 ops/s
    # Warmup Iteration   5: 702,418 ops/s
    Iteration   1: 727,064 ops/s
    Iteration   2: 699,037 ops/s
    Iteration   3: 725,420 ops/s
    Iteration   4: 704,081 ops/s
    Iteration   5: 718,851 ops/s
    
    # Run progress: 20,00% complete, ETA 00:06:41
    # Fork: 2 of 5
    # Warmup Iteration   1: 611,790 ops/s
    # Warmup Iteration   2: 667,239 ops/s
    # Warmup Iteration   3: 682,958 ops/s
    # Warmup Iteration   4: 690,230 ops/s
    # Warmup Iteration   5: 662,338 ops/s
    Iteration   1: 649,759 ops/s
    Iteration   2: 688,102 ops/s
    Iteration   3: 682,811 ops/s
    Iteration   4: 675,610 ops/s
    Iteration   5: 669,792 ops/s
    
    # Run progress: 40,00% complete, ETA 00:05:01
    # Fork: 3 of 5
    # Warmup Iteration   1: 620,658 ops/s
    # Warmup Iteration   2: 689,224 ops/s
    # Warmup Iteration   3: 687,548 ops/s
    # Warmup Iteration   4: 703,404 ops/s
    # Warmup Iteration   5: 692,795 ops/s
    Iteration   1: 683,341 ops/s
    Iteration   2: 694,400 ops/s
    Iteration   3: 672,931 ops/s
    Iteration   4: 698,661 ops/s
    Iteration   5: 686,450 ops/s
    
    # Run progress: 60,00% complete, ETA 00:03:20
    # Fork: 4 of 5
    # Warmup Iteration   1: 603,891 ops/s
    # Warmup Iteration   2: 707,145 ops/s
    # Warmup Iteration   3: 685,873 ops/s
    # Warmup Iteration   4: 697,154 ops/s
    # Warmup Iteration   5: 710,915 ops/s
    Iteration   1: 691,093 ops/s
    Iteration   2: 705,781 ops/s
    Iteration   3: 681,016 ops/s
    Iteration   4: 712,380 ops/s
    Iteration   5: 681,856 ops/s
    
    # Run progress: 80,00% complete, ETA 00:01:40
    # Fork: 5 of 5
    # Warmup Iteration   1: 627,660 ops/s
    # Warmup Iteration   2: 685,644 ops/s
    # Warmup Iteration   3: 713,280 ops/s
    # Warmup Iteration   4: 706,216 ops/s
    # Warmup Iteration   5: 692,613 ops/s
    Iteration   1: 697,768 ops/s
    Iteration   2: 716,831 ops/s
    Iteration   3: 706,842 ops/s
    Iteration   4: 688,841 ops/s
    Iteration   5: 699,673 ops/s
    
    
    Result "com.aedyl.Game.launch":
      694,336 ±(99.9%) 13,728 ops/s [Average]
      (min, avg, max) = (649,759, 694,336, 727,064), stdev = 18,326
      CI (99.9%): [680,608, 708,063] (assumes normal distribution)
    
    
    # Run complete. Total time: 00:08:21
    
    REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
    why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
    experiments, perform baseline and negative tests that provide experimental control, make sure
    the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
    Do not assume the numbers tell you what you want them to tell.
    
    Benchmark     Mode  Cnt    Score    Error  Units
    Game.launch  thrpt   25  694,336 ± 13,728  ops/s
```  

