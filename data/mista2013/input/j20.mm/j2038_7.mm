************************************************************************
file with basedata            : md358_.bas
initial value random generator: 259038397
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  22
horizon                       :  158
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     20      0       24        8       24
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           8  10  18
   3        3          3           7   9  14
   4        3          3           5   7  12
   5        3          2           6  13
   6        3          3          11  14  20
   7        3          2          16  18
   8        3          3          15  16  19
   9        3          2          11  19
  10        3          2          17  21
  11        3          1          15
  12        3          2          14  20
  13        3          2          17  18
  14        3          1          15
  15        3          1          21
  16        3          2          17  21
  17        3          1          20
  18        3          1          19
  19        3          1          22
  20        3          1          22
  21        3          1          22
  22        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     1       2    8    7    4
         2     1       3    6    7    4
         3     5       2    5    5    3
  3      1     1       8   10    5    5
         2     3       6    9    2    5
         3     4       4    9    2    3
  4      1     6       7    5    8    7
         2     9       7    5    8    6
         3    10       4    4    5    3
  5      1     1       6    8    9    8
         2     6       4    7    4    8
         3     6       5    7    6    6
  6      1     2       6    7    7    9
         2     9       5    7    7    7
         3    10       5    7    5    6
  7      1     6       4    9   10    4
         2     6       4    9    8    5
         3     8       4    8    6    3
  8      1     4      10    8    8    6
         2     6       7    8    5    6
         3     8       5    5    4    4
  9      1     6      10    9   10    8
         2     8       6    9    7    8
         3     8       7    8    7    7
 10      1     2       2    4    9    4
         2     7       2    4    7    4
         3    10       2    1    1    3
 11      1     3       7   10    8    5
         2     7       4    7    8    5
         3     9       2    6    6    3
 12      1     5       7    3    6    9
         2     7       6    3    6    9
         3    10       5    3    6    8
 13      1     2       5    4    4   10
         2     3       5    4    3   10
         3     7       5    3    2    9
 14      1     1       6    9    7    5
         2     3       4    6    6    5
         3     6       4    4    4    4
 15      1     4       9    8    7    8
         2     9       6    5    7    6
         3    10       2    1    6    3
 16      1     2       5   10    7    6
         2     7       5    9    4    6
         3     7       5    9    6    3
 17      1     1       7    9    7    7
         2     3       5    5    7    4
         3     7       4    5    6    1
 18      1     3       9    8    8    2
         2     4       9    7    6    2
         3     4       9    6    7    2
 19      1     9       9   10    6   10
         2     9       9    7    7   10
         3    10       9    6    2   10
 20      1     2       3    2    4    7
         2     5       3    1    3    7
         3    10       3    1    3    6
 21      1     3       9    5    9    3
         2     7       9    4    6    2
         3     9       8    2    2    2
 22      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   19   19  101   99
************************************************************************
