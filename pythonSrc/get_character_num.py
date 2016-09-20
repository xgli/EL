# -*- coding: utf-8 -*-

import cPickle as pickle


cmn_len_dict = {}
eng_len_dict = {}
spa_len_dict = {}

fcmn = file('cmn_file_len.pk', 'wb')
feng = file('eng_file_len.pk', 'wb')
fspa = file('spa_file_len.pk', 'wb')


fr = open("character_counts.tsv","r")
text = fr.read()
fr.close()
lines = text.split("\n")
for line in lines:
    if line == "":
        continue
    tokens = line.split("\t")
    doc_id = tokens[0]
    doc_len = tokens[1]
    if "CMN_" in doc_id:
        cmn_len_dict[doc_id] = doc_len
    if "ENG_" in doc_id:
        eng_len_dict[doc_id] = doc_len
    if "SPA_" in doc_id:
        spa_len_dict[doc_id] = doc_len
pickle.dump(cmn_len_dict, fcmn)
fcmn.close()
pickle.dump(eng_len_dict, feng)
feng.close()
pickle.dump(spa_len_dict, fspa)
fspa.close()
