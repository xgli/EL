#!/usr/bin/env python
# coding=utf-8

fw = open("zuhe.tab","w")
one_dict = {}
with open("../../../dict/jiaxie.dict", "r") as fr:
    lines = fr.read().split("\n")
for line in lines:
    line = line.decode("utf-8")
    if line == "":
        continue
    print line
    tokens = line.split("\t")
    mention = tokens[1]
    midtype = tokens[2] + "\t" + tokens[3]
    one_dict[mention] = midtype

with open("zuhe_es.tab") as fr:
    lines = fr.read().split("\n")
for line in lines:
    line = line.decode("utf-8")
    if line == "":
        continue
    #print line
    tokens = line.split("\t")
    mentions = tokens[0]
    mentionsloc = tokens[1]
    id = mentionsloc.split(":")[0]
    loc = mentionsloc.split(":")[1]
    start = int(loc.split("-")[0])
    for i in range(0, len(mentions)):
        one = mentions[i]
        end = start + i
        if one in one_dict:
            wline = one + "\t" + id + ":" + str(end) + "-" + str(end) + "\t" + one_dict[one] + "\n"
            print wline
            fw.write(wline.encode("utf-8"))
fw.close()
