# -*- coding: utf-8 -*-
"""
Created on Fri Jul 29 11:08:04 2016

@author: zhdi
"""
fw = open("cmn.tab","w")
nil_dict = {} 
mention_dict = {}
count = 0

nilcount = 0
with open("cmn_res.tab","r") as fr_res:
    text = fr_res.read().decode("utf-8")
    lines_res = text.split("\n")
for line in lines_res:
    line = line.replace("\r","")
    if "" == line:
        continue
    tokens = line.split("\t")
    #print tokens
    mention_loc =  tokens[1]
    if(mention_dict.has_key(mention_loc)):
        continue
    else:
        mention_dict[mention_loc] = ""
        line = "li\t" +"EDL_" + str(count) + "\t" + line + "\tNAM\t1.0\n"
        fw.write(line.encode("utf-8"))
        count += 1
fw.flush()

with open("collective_linking_result.txt","r") as fr_linking:
    text = fr_linking.read().decode("utf-8")
    lines_linking = text.split("\n")
for line in lines_linking:
    line = line.replace("\r","")
    if "" == line:
        continue
    tokens = line.split("\t")
    #print tokens
    mention = tokens[0]
    mention_loc = tokens[1]
    if(mention_dict.has_key(mention_loc)):
        continue
    else:
        mention_dict[mention_loc] = ""
        candidate_dict = eval(tokens[-1])
        if(0 == len(candidate_dict)):
            if(nil_dict.has_key(mention)):
                mid = nil_dict[mention] 
            else:
                mid = "NIL_" + str(nilcount) 
                nilcount += 1
                nil_dict[mention] = mid
        else:
            candidate_dict = sorted(candidate_dict.iteritems(),key=lambda d:d[1],reverse = True)
            mid = candidate_dict[0][0].replace("f_","")
        line = "li\t" + "EDL_" + str(count) + "\t" + tokens[0] + "\t" + tokens[1] + "\t" + mid + "\t" + tokens[2] + "\tNAM\t1.0\n"
        fw.write(line.encode("utf-8"))
        count += 1
fw.flush()

''' 
with open("tempresult.tab") as fr_temp:
    text = fr_temp.read()
    lines_temp = text.split("\n")
for line in lines_temp:
    line = line.replace("\r","")
    if "" == line:
        continue
    #print line
    tokens = line.split("\t")
    mention = tokens[0]
    mention_loc = tokens[1]
    if(mention_dict.has_key(mention_loc)):
        continue
    else:
        mention_dict[mention_loc] = ""
        fw.write("li\t" +"EDL_" + str(count) + "\t" + line + "\tNAM\t1.0\n")
        count += 1
fw.flush()  
'''

with open("zuhe.tab","r") as fr:
    lines = fr.read().split("\n")
for line in lines:
    line = line.replace("\r","")
    if "" == line:
        continue
    wline = "li\t" +"EDL_" + str(count) + "\t" + line + "\tNAM\t1.0\n"
    fw.write(wline)
    count += 1
    #print count
fw.flush()  
with open("dfauthor.tab","r") as fr_author:
    text = fr_author.read().decode("utf-8")
    lines_author = text.split("\n")
for line in lines_author:
    if "" == line:
        continue
    print line
    tokens = line.split("\t")
    mention = tokens[0]
    mention_loc = tokens[1]
    if(mention_dict.has_key(mention_loc)):
        continue
    else:
        mention_dict[mention_loc] = ""
        if(nil_dict.has_key(mention)):
            mid = nil_dict[mention] 
        else:
            mid = "NIL_" + str(nilcount) 
            nilcount += 1
            nil_dict[mention] = mid
        line = "li\t" + "EDL_" + str(count) + "\t" + tokens[0] + "\t" + tokens[1] + "\t" + mid + "\t" + "PER" + "\tNAM\t1.0\n"
        fw.write(line.encode("utf-8"))
        count += 1
fw.flush()    
fw.close()
