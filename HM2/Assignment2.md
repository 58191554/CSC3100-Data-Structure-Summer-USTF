# Assignment 2 Small Database

Students are asked to mimic a small database. Assume that a company want to store data in integer form.
You need to write a data structure to maintain numbers that provide the following operations: 
1. insert data x；
2. delete data x (If more than one data have the same value, delete only one of them)；
3. find the rank of data x. If more than one data have the same value, output the smallest rank.
4. Find the number rank x (the x smallest element).
5. Find the largest number less than x
6. Find the smallest number bigger than x
We highly recommend you to use a self-balance tree to implememt this assignment because we will set strict time constraint.   

**Input Format**

The first line is a number $n$, representing how many number.From the second to the last line, each line has two number $opt$ and $x$ .$opt$ representing $(1≤opt≤6)$。

**Output Format**

If $opt \in \lbrace 3, 4, 5, 6 \rbrace$, output a line as the corresponding answer.

**Example**

**Input**

```bash
10
1 106465
4 1
1 317721
1 460929
1 644985
1 84185
1 89851
6 81968
1 492737
5 493598
``
```

**Output**

```bash
106465
84185
492737
```

**Test Data Range**

$1≤n≤10^5, -10^7≤x≤10^7$
