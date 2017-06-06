#!/usr/bin/env python
# coding=utf-8

indir = "./data/xmlParse/eng/"
fr = open("./eng_gold.tab") 
fw = open("mention_context.txt","w")
n = 0
a = 0
b = 0
for line in fr:
    #print line
    es = line.strip().split("\t")
    mention = es[2]
    mention_doc = es[3].split(":")[0]
    mention_loc = es[3].split(":")[1]
    mid = es[4]
    mtype = es[5]
    nam = es[6]
    if 'NOM' ==  nam:
        continue

    if 'NIL' in mid:
        continue
    n += 1
    start = int(mention_loc.split("-")[0])
    end = int(mention_loc.split("-")[1])
    filepath = indir + mention_doc + ".xml"
    fr_doc = open(filepath)
    for in_line in fr_doc:
        in_start, text = in_line.split("\t")
        in_start = int(in_start)
        if start >= in_start and end <= in_start + len(text):
            characters = list(text)
            #print words
            #print in_start
            #print mention
            m = ''.join(characters[start - in_start : end - in_start + 1])
            if m != mention:
                print(m)
                print(mention)
                print(line)
                break
            outline = mention_doc+":"+mention_loc  + "\t" + text
            fw.write(outline)
            #print in_line
fw.close()
print(n)
print(a) 
print(b) 

