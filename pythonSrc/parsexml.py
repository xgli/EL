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

fw_fail = open("parsefail.tab","w")


fw_news_author = open("../data/result/result/newsauthor.tab","w")
author_reg = re.compile('<AUTHOR>(.*?)</AUTHOR>'.decode("utf-8"))

author_file = "../data/result/result/dfauthor.tab"
fw_df_author = open(author_file,"w")

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
                fw_news_author.write(author_line.encode("utf-8"))
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

def parsedf(fileindir,fileoutdir,filename):
    try:
        doc_id = filename.replace(".xml","")
        fileinpath = fileindir + filename
        print filename
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
                fw_df_author.write(author_line.encode("utf-8"))
                fw_df_author.flush()
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
        fw_fail.write("error:" + doc_id + str(e) + "\n")
        
    
cmndffileindir = "../data/raw/cmn/df/"
cmnnewsfileindir = "../data/raw/cmn/nw/"
cmnfileoutdir = "../data/xmlParse/cmn/"

engdffileindir = "../data/raw/eng/df/"
engnewsfileindir = "../data/raw/eng/nw/"
engfileoutdir = "../data/xmlParse/eng/"

spadffileindir = "../data/raw/spa/df/"
spanewsfileindir = "../data/raw/spa/nw/"
spafileoutdir = "../data/xmlParse/spa/"

print len(file_loc_dict)

for key in file_len_dict.keys():
    if "CMN_" in key:
        if os.path.isfile(cmndffileindir+key+".xml"):
            parsedf(cmndffileindir, cmnfileoutdir,key+".xml")
        else:
            parsenews(cmnnewsfileindir,cmnfileoutdir, key+".xml")
    elif "ENG_" in key:
        if os.path.isfile(engdffileindir+key+".xml"):
            parsedf(engdffileindir, engfileoutdir,key+".xml")
        else:
            parsenews(engnewsfileindir, engfileoutdir, key+".xml")
    else:
        if os.path.isfile(spadffileindir+key+".xml"):
            parsedf(spadffileindir, spafileoutdir,key+".xml")
        else:
            parsenews(spanewsfileindir, spafileoutdir, key+".xml")

fw_fail.close()
fw_news_author.close()
fw_df_author.close()
pickle.dump(file_loc_dict,file("file_loc_dict.pk","wb"))



