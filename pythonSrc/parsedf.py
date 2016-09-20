#!/usr/bin/env python
# coding=utf-8
import re
import os
import cPickle as pickle

def is_repetition(loc_dict,filename,begin_pos,end_pos):
    if(sum(loc_dict[filename][begin_pos:end_pos])):
        return True
    else:
        return False

author_file = "../data/result/result/dfauthor.tab"
fw_author = open(author_file,"w")

file_loc_dict = {}
file_len_dict = {}
with open("character_counts.tsv","r") as fr:
    text = fr.read()
lines = text.split("\n")
for line in lines:
    if "" == line:
        continue
    tokens = line.split("\t")
    #print tokens
    doc_id = tokens[0]
    doc_len = int(tokens[1])
    file_loc_dict.setdefault(doc_id,[0]*(doc_len + 1))
    file_len_dict[doc_id] = doc_len

with open("quote_regions.tsv","r") as fr:
    text = fr.read()
lines = text.split("\n")
for line in lines:
    if "" == line:
        continue
    tokens = line.split("\t")
    #print tokens
    doc_id = tokens[0].split(".")[0]
    quote_start = int(tokens[1])
    qutoe_end = int(tokens[2])
    if file_loc_dict.has_key(doc_id):
        for i in range(quote_start,qutoe_end + 1):
            file_loc_dict[doc_id][i] = 1
    else:
        print doc_id

fw_fail = open("dffail.tab","w")

def parsedf(fileindir,fileoutdir,filename):
    try:
        doc_id = filename.replace(".xml","")
        fileinpath = fileindir + filename
        print fileinpath
        fileoutpath = fileoutdir + filename
        fw = open(fileoutpath,'w')
        author_reg = re.compile(' author="(.*?)"'.decode("utf-8"))
        with open(fileinpath,"r") as fr:
            lines = fr.readlines()
        start = 0
        for line in lines:
            line = line.decode("utf-8")
            end = start + len(line) - 1
            if "\n" == line:
                start = start + len(line)
                continue

            if is_repetition(file_loc_dict, doc_id,start,end):
                #print "repetition:" + str(start) + "\t" + line 
                start = start + len(line)
                #print str(start - 1)
                continue

            if "</post>\n" == line or "<headline>\n" == line or "</headline>\n" == line or "</doc>\n" == line or line.startswith("<doc"):
                start += len(line)
                continue

            if ' author=' in line:#多加一个空格
                #print line
                author_group = re.search(author_reg,line)
                author = author_group.group(1)
                author_start = start + author_group.start(1)
                author_end = author_start + len(author) - 1
                author_line =  author + "\t" + doc_id + ":" + str(author_start) + "-" + str(author_end) + "\n"
                fw_author.write(author_line.encode("utf-8"))
                fw_author.flush()
                #print str(start) +"\t" + line
                #print author_line 
                start = start + len(line)
                continue
            if len(line) == 2: 
                start = start + len(line)
                continue
            line = line.replace("\t"," ").replace(u"\x80"," ").replace(u"\x82"," ").replace(u"\x99"," ").replace(u"\u0890"," ")
            line = line.replace(u"\x7F"," ").replace(u"\x93"," ").replace(u"\x94"," ").replace(u"\x91"," ").replace(u"\x92"," ")
            line = line.replace(u"\x85"," ").replace(u"\x91"," ")
            fw_line = str(start) + "\t" + line
            
            fw.write(fw_line.encode("utf-8"))
            start += len(line)
        fw.close()
        if start - 1 == file_len_dict[doc_id]:
            pass
        else:
            fw_fail.write("len not equel:" + doc_id+ str(start) + str(file_len_dict[doc_id])+"\n")
    except Exception,e:
        fw_fail.write("error:" + doc_id + e + "\n")
        
    
fileindir = "../data/raw/cmn/df/"
fileoutdir = "../data/xmlParse/cmn/"
file_list = os.listdir(fileindir)
for filename in file_list: 
    parsedf(fileindir,fileoutdir,filename)

fileindir = "../data/raw/eng/df/"
fileoutdir = "../data/xmlParse/eng/"
file_list = os.listdir(fileindir)
for filename in file_list: 
    parsedf(fileindir,fileoutdir,filename)

fileindir = "../data/raw/spa/df/"
fileoutdir = "../data/xmlParse/spa/"
file_list = os.listdir(fileindir)
for filename in file_list: 
    parsedf(fileindir,fileoutdir,filename)

fw_fail.close()
fw_author.close()
pickle.dump(file_loc_dict,file("file_loc_dict.pk","wb"))
