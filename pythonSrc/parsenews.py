#!/usr/bin/env python
# coding=utf-8
import re
import os

file_len_dict = {}
with open("character_counts.tsv","r") as fr:
    text = fr.read()
lines = text.split("\n")
for line in lines:
    if "" == line:
        continue
    tokens = line.split("\t")
    doc_id = tokens[0]
    doc_len = int(tokens[1])
    file_len_dict[doc_id] = doc_len

fw_author = open("../data/result/result/newsauthor.tab","w")

author_reg = re.compile('<AUTHOR>(.*?)</AUTHOR>'.decode("utf-8"))

fw_fail = open("newsfailed.tab","w")
def parsenews(fileindir,fileoutdir,filename):
    try:
        print filename
        doc_id = filename.replace(".xml","")
        fileinpath = fileindir + filename
        fileoutpath = fileoutdir + filename
        fw = open(fileoutpath,"w")
        with open(fileinpath,'r') as fr:
            lines = fr.readlines()
        start = 0
        for line in lines:
            line = line.decode("utf-8")
            if "<TEXT>\n" == line or "</TEXT>\n" == line or "<HEADLINE>\n" == line or "</HEADLINE>\n" == line:
                start += len(line)
                continue
            if "</DOC>\n" == line or line.startswith("<DOC") or line.startswith("<DATE_TIME>"):
                start += len(line)
                continue
            if "\n" == line:
                start += len(line)
                continue
            if "<AUTHOR/>\n" == line:
                start += len(line)
                continue
            if '<AUTHOR>' in line:
                author_group = re.search(author_reg,line)
                author = author_group.group(1)
                author_start = start + author_group.start(1)
                author_end = author_start + len(author) - 1
                author_line = author + "\t" + doc_id + ":" +str(author_start) + "-" + str(author_end) + "\n"
                fw_author.write(author_line.encode("utf-8"))
                #print author_line
                #print line
                start = start + len(line)
                continue
            line = line.replace("\t"," ")
            fw_line =  str(start) + "\t" + line
            fw.write(fw_line.encode("utf-8"))
            start += len(line)
        fw.close()
        
        #print start
        #print file_len_dict[doc_id]
        if start - 1 == file_len_dict[doc_id]:
            pass 
        else:
            fw_fail.write("not equel:" + doc_id + str(start) + "\t" + file_len_dict[doc_id] + "\n")
    except Exception,e:
        print e
        fw_fail.write("error:" + doc_id + "\n")
    
fileindir = "../data/raw/cmn/nw/"
fileoutdir = "../data/xmlParse/cmn/"
file_list = os.listdir(fileindir)
for filename in file_list:
    parsenews(fileindir,fileoutdir,filename)

fileindir = "../data/raw/eng/nw/"
fileoutdir = "../data/xmlParse/eng/"
file_list = os.listdir(fileindir)
for filename in file_list:
    parsenews(fileindir,fileoutdir,filename)

fileindir = "../data/raw/spa/nw/"
fileoutdir = "../data/xmlParse/spa/"
file_list = os.listdir(fileindir)
for filename in file_list:
    parsenews(fileindir,fileoutdir,filename)
fw_author.close()
fw_fail.close()
