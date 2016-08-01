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

es_loc_dict = pickle.load(file("cmn_es_found_loc.pk","rb"))

cmn_es_found_dict = pickle.load(file("cmn_es_found.pk","rb"))
cmn_mention_in_dir = "../data/mention/cmn/"
cmn_mention_out_dir = "../data/mentiones/cmn/"

file_list = os.listdir(cmn_mention_in_dir)
for filename in file_list:
    deletebyes(cmn_mention_in_dir,cmn_mention_out_dir,filename,es_loc_dict)
    addbyes(cmn_mention_in_dir,cmn_mention_out_dir,filename,cmn_es_found_dict)

     
