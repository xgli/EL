#!/usr/bin/env python
# coding=utf-8
with open("loc.txt") as fr:
    lines = fr.readlines()

fw = open("locaddnom.txt","w")
for line in lines:
    line = line.rstrip()
    line = line + "\t" + "NOM\n" 
    fw.write(line)
    fw.flush()
fw.close()
    

