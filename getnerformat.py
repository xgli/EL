#!/usr/bin/env python
# coding=utf-8


import os


indir = "./data/xmlParse/eng/"
fr = open("./eng_gold.tab") 
fw = open("nerformat.txt","w")
n = 0
a = 0
b = 0
doc_loc_mention_dict = {}
for line in fr:
    #print line
    es = line.strip().split("\t")
    mention = es[2]
    mention_doc = es[3].split(":")[0]
    mention_loc = es[3].split(":")[1]
    start = int(mention_loc.split("-")[0])
    mid = es[4]
    mtype = es[5]
    nam = es[6]

    if 'NOM' ==  nam:
        continue

    if 'NIL' in mid:
        continue
    mention_loc_dict = doc_loc_mention_dict.setdefault(mention_doc,{})

    mention_loc_dict.setdefault(start,mention+"\t" + mtype)
    

filelist = os.listdir(indir)
for file in filelist:
    filepath = indir + file
    print filepath
    fr = open(filepath)
    mention_loc = doc_loc_mention_dict[file.replace('.xml','')]
    for line in fr:
        line = line.strip()
        start,text = line.split('\t')
        start = int(start)
        word = ''
        mlen = 0
        for i in range(len(text)):
            if text[i].isalpha():
                word += text[i]
            else:
                if mention_loc.has_key(start):
                    m = mention_loc[start].split('\t')[0]
                    mt = mention_loc[start].split('\t')[1]
                    if len(m) == len(word):
                        print '***********'
                        print mention_loc[start]
                        print "{0}\t{1}".format(word,'I-'+mt)
                        outline =  "{0}\t{1}\n".format(word,'I-'+mt)
                        fw.write(outline)
                        print '***********'
                        start += len(word) 
                        start += 1
                        word = ''
                        continue

                    if len(m) > mlen:
                        if mlen == 0:
                            print '***********'
                            print mention_loc[start]
                            print "{0}\t{1}".format(word,'B-'+mt)
                            outline = "{0}\t{1}\n".format(word,'B-'+mt)
                            fw.write(outline)
                            print '***********'
                        else:
                            print '***********'
                            print mention_loc[start]
                            print "{0}\t{1}".format(word,'I-'+mt)
                            outline =  "{0}\t{1}\n".format(word,'I-'+mt)
                            fw.write(outline)
                            print '***********'
                    mlen += len(word)
                    word = ''
                    if mlen == len(m):
                        start +=  mlen
                        mlen = 0 
                    else:
                        print text[i]
                        mlen += 1
                    continue
                else:
                    print "{0}\t{1}".format(word,'O')
                    outline = "{0}\t{1}\n".format(word,'O')
                    fw.write(outline)
                    start += len(word) 
                    if text[i] != ' ':
                        print "{0}\t{1}".format(text[i],'O')
                        outline = "{0}\t{1}\n".format(text[i],'O')
                        fw.write(outline)
                    start += 1
                    word = ''

fw.close()



    
    




'''
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
'''
