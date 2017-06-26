#!/usr/bin/env python
# coding=utf-8

fw = open('all.tab','w')
cnt = 0
nil = 0
nil_dict = {}
with open('cmn_au.tab') as fr:
    for line in fr:
        tokens = line.split('\t')
        mid = tokens[4]
        mention = tokens[2]
        if 'NIL' in mid:
            if mention in nil_dict:
                tokens[4] = nil_dict[mention]
            else:
                mid = 'NIL_' + str(nil)
                tokens[4] = 'NIL_' + str(nil)
                nil_dict[mention] =  mid
                nil += 1
        tokens[1] = 'EDL_' + str(cnt)
        cnt += 1
        outline = "\t".join(tokens)
        fw.write(outline)
        fw.flush()

with open('eng_au.tab') as fr:
    for line in fr:
        tokens = line.split('\t')
        mid = tokens[4]
        mention = tokens[2]
        if 'NIL' in mid:
            if mention in nil_dict:
                tokens[4] = nil_dict[mention]
            else:
                mid = 'NIL_' + str(nil)
                tokens[4] = 'NIL_' + str(nil)
                nil_dict[mention] =  mid
                nil += 1
        tokens[1] = 'EDL_' + str(cnt)
        cnt += 1
        outline = "\t".join(tokens)
        fw.write(outline)
        fw.flush()

nil_dict = {}
with open('spa_au.tab') as fr:
    for line in fr:
        tokens = line.split('\t')
        mid = tokens[4]
        mention = tokens[2]
        if 'NIL' in mid:
            if mention in nil_dict:
                tokens[4] = nil_dict[mention]
            else:
                mid = 'NIL_' + str(nil)
                tokens[4] = 'NIL_' + str(nil)
                nil_dict[mention] =  mid
                nil += 1
        tokens[1] = 'EDL_' + str(cnt)
        cnt += 1
        outline = "\t".join(tokens)
        fw.write(outline)
        fw.flush()

with open('newsauthor_new.tab') as fr:
    for line in fr:
        tokens = line.split('\t')
        mention = tokens[0]
        mid ="NIL"
        if mention in nil_dict:
            mid = nil_dict[mention]
        else:
            mid = 'NIL_' + str(nil)
            nil_dict[mention] =  mid
            nil += 1
        cnt += 1
        outline = "li\tEDL_" + str(cnt) +'\t' + line.strip() + "\t" + mid + "\tPER\tNAM\t1.0\n" 
        fw.write(outline)
        fw.flush()
fw.close()
