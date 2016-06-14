#!/bin/bash

echo "Testscase 1"
cat testcase_1_1.txt testcase_1_2.txt | sort | uniq > out_1.txt
sort $1 > temp
diff out_1.txt temp


echo "Testcase 2"
cat testcase_2_1.txt testcase_2_2.txt | sort | uniq > out_2.txt
sort $2 > temp
diff out_2.txt temp

echo "Testcase 3"
cat testcase_3_1.txt testcase_3_2.txt | sort | uniq > out_3.txt
sort $3 > temp
diff out_3.txt temp