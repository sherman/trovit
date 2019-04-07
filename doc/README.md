
## Task 1

The dataset we provide contains an aggregation of classified ads from hundreds of different sources. As you imagine, ad publishers may put the same ad many times to make it more visible, and even with slightly different content in the structure to try to make it more genuine.

We ask you to create and implement an algorithm to detect duplicates on the car dataset. As output, we recommend you to provide sets of unique Ids for each group of duplicates.

Please provide the following in your answers:

* A brief reasoning explaining your strategy, and why you have choosen it.
* Your code. Use whatever you need, but remember that you're applying for a Data Engineer position ;)
* A sample of the output.

## Solution 1

The strategy. 

To make any deduplication we should find a _unique key_.
So, I started analyzing data to find the keys are most _representative_.
First of all, I selected columns which are exist in most elements. They are: make, model, year and mileage.
Then, I started to explore the columns to find out some extra issues with data such as no/default value. Also, I added a rule, that two elements always equal in case of their uniqueIds are equals.
I also dropped elements w/o **"make"** column.

I choose a sample of data and validated the algorithm with a simple, stupid O (n^2) brute force algorithm (find any intersections in unique ids between two groups).

The sample output:

```
{"key":{"make":"Peugeot","model":"208","year":2014,"mileage":6157},"uniqueKeys":["4vubR2S7ag0V1YbniYJo1w==","3VG2uqRAACE1M3+thXEiFw==","Gd42TU6XeiNvsgfwVkcsWQ=="]}
{"key":{"make":"Volvo","model":"S60","year":2013,"mileage":1},"uniqueKeys":["Qg/kBiqm/bfE/8lot2nL1A==","00pC/TAa8zrxVIQqHeFTmA==","Dr98KglXYIjMPx+JUEXMEw=="]}
```

So, the result has fewer rows up to 20%, than the original dataset. 

