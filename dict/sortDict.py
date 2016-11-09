#!/usr/bin/env python
# coding=utf-8

#将字典进行排序，按mention的长度进行排序 2016/11/9

fileinput = raw_input("input dict:")
filename = fileinput.lstrip().rstrip()

with open(filename) as fr:
    lines = fr.readlines()
dict_mention = {}
for line in lines:
    #print line
    tokens = line.split("\t")
    mention = tokens[0]
    if dict_mention.has_key(mention):
        pass
        #dict_mention[mention].append(line)
    else:
        dict_mention.setdefault(mention,[])
        dict_mention[mention].append(line)
dict_mention = sorted(dict_mention.iteritems(),key = lambda d:len(d[0]),reverse=True)
fw = open(filename.replace('.dict','_sort.dict') ,"w")
for e in dict_mention:
    for l in e[1]:
        fw.write(l)
fw.close()
