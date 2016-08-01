#!/usr/bin/env python
# coding=utf-8

import cPickle as pickle
import os

def is_reptition(pos_dict,filename,begin_pos,end_pos):
    if not pos_dict.has_key(filename):
        return False
    if(sum(pos_dict[filename][begin_pos:end_pos]) > 0):
        return True
    else:
        return False

def deletebyes(fileindir,fileoutdir,filename,file_loc_dict):
#删除跟词表重叠的mention
    print filename
    fileinpath = fileindir + filename
    fileoutpath = fileoutdir + filename
    fr = open(fileinpath,"r")
    lines = fr.readlines()
    fr.close()
    fw = open(fileoutpath,"w")
    for line in lines:
        id_loc = line.split("\t")[1]
        id = id_loc.split(":")[0]
        loc = id_loc.split(":")[1]
        loc_start = int(loc.split("-")[0])
        loc_end = int(loc.split("-")[1])
        if is_reptition(file_loc_dict,id,loc_start,loc_end):
            continue 
        else:
            fw.write(line)
            fw.flush()
    fw.close()
    

def addbyes(fileindir,fileoutdir,filename,dict):
    print filename
    fileinpath = fileindir + filename
    fileoutpath = fileoutdir + filename
    fr = open(fileinpath,"r")
    text = fr.read()
    fw = open(fileoutpath,"w")
    fw.write(text)
    id = filename.split(".")[0]
    if dict.has_key(id):
        for res in dict[id]:
            restokens = res.split("\t")
            reline = restokens[0] + "\t" + restokens[1] + "\t" + restokens[-1]
            fw.write(reline.encode("utf-8")+"\n")
    fw.close()



     
eng_es_found_dict = pickle.load(file("eng_es_found.pk","rb"))
eng_mention_in_dir = "../data/mention/eng/"
eng_mention_out_dir = "../data/mentiones/eng/"

file_list = os.listdir(eng_mention_in_dir)
for filename in file_list:
    deletebyes(eng_mention_in_dir,eng_mention_out_dir,filename,es_loc_dict)
    addbyes(eng_mention_in_dir,eng_mention_out_dir,filename,eng_es_found_dict)


