# -*- coding: utf-8 -*-

import itertools
fw = open("../data/dict/countrys.tab","w")
with open("../data/dict/country.tab","r") as fr:
	text = fr.read()
lines = text.split("\n")
jc_list = []
for line in lines:
	line = line.decode("utf-8")
	if '' == line:
		continue
	tokens = line.split("\t")
	jc = tokens[1]
        jc = jc.strip()
	jc_list.append(jc)
print len(jc_list)
permutations_list = list(itertools.permutations(jc_list,3))

for each in permutations_list:
	zh = ""
	for e in each:
		zh += e.encode("utf-8")
	#print zh
        fw.write(zh+"\n")   

permutations_list = list(itertools.permutations(jc_list,2))
for each in permutations_list:
    zh = ""
    for e in each:
        zh += e.encode('utf-8')
    #print zh
    fw.write(zh+"\n")
fw.close()
#print premutations_list
