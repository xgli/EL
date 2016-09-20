#!/usr/bin/env python
# coding=utf-8
one_list = []
with open("cmn_fac_loc_head.tab","r") as fr:
    lines = fr.readlines()
for line in lines:
    if "PRO" in line:
        continue
    one_list.append(line)
one_list = list(set(one_list))

fw = open("cmn_fac_loc_pro.tab","w")
fw.write("".join(one_list))
for e in one_list:
    print e

