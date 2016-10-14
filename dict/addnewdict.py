# -*- coding: utf-8 -*-
"""
Created on Fri Sep 16 21:39:30 2016

@author: zhdi
增加新的字典
"""

old_dict = {}
add_dict = {}
eng_old_dict = {}
eng_add_dict = {}
spa_old_dict = {}
spa_add_dict = {}

with open('chinese.tab',"r") as fr:
    lines = fr.readlines()
for line in lines:
    mention = line.split("\t")[0]
    print mention
    old_dict[mention] = ""

with open('spanish.tab',"r") as fr:
    lines = fr.readlines()
for line in lines:
    mention = line.split("\t")[0]
    spa_old_dict[mention] = 0

with open('english.tab',"r") as fr:
    lines = fr.readlines()
for line in lines:
    mention = line.split("\t")[0]
    eng_old_dict[mention] = 0

fw = open("new.tab.bak","w")
with open("new.tab","r") as fr:
    lines = fr.readlines()
for line in lines:
    fw.write(line)
    line = line.strip("\n")
    if line == "":
        continue
    mention,mtype = line.split("\t",1)
    add_dict[mention] = ""   
fw.close()
    
fw = open("new.tab","a")
fw_eng = open("eng_new.tab","a")
fw_spa = open("spa_new.tab","a")

mtype_class = {'per':'PER','PER':'PER','org':'ORG','ORG':'ORG','gpe':'GPE','GPE':'GPE','loc':'LOC','LOC':'LOC','fac':'FAC','FAC':'FAC'}
print "1"
while(1):
    mid = ""
    mtype = ""
    mclass = ""
    newmention = raw_input("mentionname:")
    newmention = newmention.lstrip().rstrip()
    if old_dict.has_key(newmention):
        print "old_dict has"
    elif add_dict.has_key(newmention):
        print "new_dict has"
    else:
        add_dict[newmention] = ""
        mtype = raw_input("mtype:")
        mtype = mtype.lstrip().rstrip()
        while(not mtype_class.has_key(mtype)): 
            mtype = raw_input("mtype:")
            mtype = mtype.lstrip().rstrip()
        mtype = mtype_class[mtype]
        
        mid = raw_input("mid or nil:")
        mid = mid.lstrip().rstrip()
        while(not mid.startswith('m.') and not mid.startswith('nil')):
            mid = raw_input("mid or nil:")
            mid = mid.lstrip().rstrip()
        if mid.startswith('nil'):
            mid  =  'NIL'

        mclass = raw_input("nam or nom:")
        mclass = mclass.lstrip().rstrip()
        while('nam' != mclass and 'nom' != mclass):
            mclass = raw_input("nam or nom:")
            mclass = mclass.lstrip().rstrip()
        if 'nam' == mclass:
            mclass = 'NAM'
        else:
            mclass = 'NOM'

        outline = newmention+ "\t" + mid + "\t" + mtype + "\t" + mclass + "\n"
        fw.write(outline)
        fw.flush()
        print "cmn add success!"

        newmention = raw_input("mentionname eng:")
        newmention = newmention.lstrip().rstrip()
        if "n" == newmention:
            continue
        if eng_old_dict.has_key(newmention):
            print "eng_old_dict has"
        elif eng_add_dict.has_key(newmention):
            print "eng_new_dict has"
        else:
            eng_add_dict[newmention] = ""
            eng_outline = newmention+ "\t" + mid + "\t" + mtype + "\t" + mclass + "\n"
            fw_eng.write(eng_outline)
            fw_eng.flush()
            print "eng add success!"

        newmention = raw_input("mentionname spa:")
        newmention = newmention.lstrip().rstrip()
        if "n" == newmention:
            continue
        if spa_old_dict.has_key(newmention):
            print "spa_old_dict has"
        elif spa_add_dict.has_key(newmention):
            print "spa_new_dict has"
        else:
            spa_add_dict[newmention] = ""
            spa_outline = newmention+ "\t" + mid + "\t" + mtype + "\t" + mclass + "\n"
            fw_spa.write(spa_outline)
            fw_spa.flush()
            print "spa add success!"

fw.close()
fw_eng.close()
fw_spa.close()
    
    

