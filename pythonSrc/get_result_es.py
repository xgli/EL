# -*- coding: utf-8 -*-

import urllib2
import traceback
import json
import cPickle as pickle
import re
import os

def http_post(url_str, mention):
#    url='http://10.110.6.43:9200/train_text/text/_search?size=300'
    url = url_str
    search_query = '''{
    "fields": [""], 
   "query": {
        "match": {
          "snt" : {
                "query" : "mei gou",
                "type": "phrase", 
                "slop": 0
              }
        }
    },
   "highlight": {
      "pre_tags": [
         "<tag>"
      ],
      "post_tags": [
         "</tag>"
      ],
      "fields": {
         "snt": {
            "number_of_fragments": 1000,
            "fragment_size" : 0
         }
      }
   }
}'''
    search_dict = eval(search_query)
    search_dict['query']['match']['snt']['query'] = mention
    jdata = json.dumps(search_dict)             # 对数据进行JSON格式化编码
    req = urllib2.Request(url, jdata)       # 生成页面请求的完整数据
    response = urllib2.urlopen(req)       # 发送页面请求
    return response.read()                    # 获取服务器返回的页面信息

def is_repetition(pos_dict, filename, begin_pos, end_pos):
    if(sum(pos_dict[filename][begin_pos:end_pos]) > 0):
        return True #repetion
    else:
        return False

#%%Chinese
print 'Chinese'
character_num_dict = pickle.load(file('cmn_file_len.pk', 'rb'))
cmn_author_and_pinyin_found_dict = {}
cmn_es_find_dict = {}
filter_for_research_file = open('../dict/chinese_sort.dict', 'r')#词表
fw = open('../data/result/cmn/cmn_res.tab', 'w')

p = re.compile('<tag>.+?</tag>')
url_str = 'http://10.110.6.43:9200/chinese_kbp2016/text/_search?size=40000'
for line in filter_for_research_file:
    print line
    line = line.strip().decode('utf-8')
    line_list = line.split('\t')
    mention = line_list[0]
    mention_str = line_list[1]
    decodejson = json.loads(http_post(url_str, mention_str))
    cnt = 0
    try:
        for candidation in decodejson["hits"]["hits"]:
            cmn_author_and_pinyin_found_dict.setdefault(candidation['_id'], [0]*(int(character_num_dict[candidation['_id']]) + 1))
            cmn_es_find_dict.setdefault(candidation["_id"],[])
            flag = 0
            res = ''
            is_start = True
            length = len(mention_str.split(' '))
            for tag in candidation['highlight']['snt']:
                #print tag
                flag = (flag + 1) % length
                tag = p.search(tag).group(0)
                #print tag
                tag_seg = tag.split(':')
                if(tag_seg[0][-1] == '>'):
                    res += ' '
                else:
                    res += tag_seg[0][-1]
                if(is_start):
                    begin = int(tag_seg[1])
                    is_start = False
                if(not flag):
                    if res != mention:
                        res = ''
                        is_start = True
                        continue
                    end = int(tag_seg[1])
                    res += '\t' + candidation['_id'] + ':' + str(begin) + '-' + str(end) + '\t' + line_list[2] + '\t' + line_list[3]

                    if(not is_repetition(cmn_author_and_pinyin_found_dict, candidation['_id'], begin, end + 1)):            
                        #print res
                        cnt += 1
                        fw.write(res.encode('utf-8') + '\n')
                        fw.flush()
                        cmn_es_find_dict[candidation["_id"]].append(res)
                        for i in range(begin, end + 1):
                            cmn_author_and_pinyin_found_dict[candidation['_id']][i] = 1
                    res = ''
                    is_start = True
    except Exception,e:
        print line
        print traceback.format_exc()
            
fw.close()
filter_for_research_file.close()
pickle.dump(cmn_es_find_dict,file("cmn_es_found.pk","wb"))
pickle.dump(cmn_author_and_pinyin_found_dict, file('cmn_es_found_loc.pk', 'wb'))
'''
#%%English
print 'English'
eng_pos_num_dict = pickle.load(file('eng_file_len.pk', 'rb'))
eng_es_find_dict = {}
eng_author_and_words_found_dict = {}

#filter_for_research_file = open('d:/data/tmp/english/all_types.txt', 'r')
filter_for_research_file = open('../dict/english_sort.dict', 'r')
fw = open('../data/result/eng/eng_res.tab', 'w')
url_str = 'http://10.110.6.43:9200/english_kbp2016/text/_search?size=40000'

p = re.compile('<tag>.+?</tag>')
for line in filter_for_research_file:
    print line
    line = line.strip().decode('utf-8')
    line_list = line.split('\t')
    mention_str = line_list[0]
    decodejson = json.loads(http_post(url_str, mention_str))
    cnt = 0
    try:
        for candidation in decodejson["hits"]["hits"]:
            eng_author_and_words_found_dict.setdefault(candidation['_id'], [0]*(int(eng_pos_num_dict[candidation['_id']]) + 1))
            eng_es_find_dict.setdefault(candidation["_id"],[])
            flag = 0
            res = ''
            is_start = True
            length = len(mention_str.split(' '))
            for tag in candidation['highlight']['snt']:
                #print tag
                if('author' in tag):
                    continue
                #tag = p.search(tag).group(0)
                for match in p.finditer(tag):                     
                    tag = match.group(0)
                    tag_seg = tag.split(':')
#                    if(tag_seg[0][5:].lower() != mention_str.lower()):
#                        print tag, mention_str
#                        continue
                    flag = (flag + 1) % length
                    if(tag_seg[0][-1] == '>'):
                        res += ' '
                    else:
                        res += tag_seg[0][5:] + ' '
                    if(is_start):
                        begin = int(tag_seg[1])
                        is_start = False
                    if(not flag):
                        end = int(tag_seg[2][:-6])
                        res = res.strip(' ')
                                               
                        if(end > begin and res.lower() == mention_str.lower() and end-begin < 100 and not is_repetition(eng_author_and_words_found_dict, candidation['_id'], begin, end + 1)):
                            res += '\t' + candidation['_id'] + ':' + str(begin) + '-' + str(end) + '\t' + line_list[1] + '\t' + line_list[2]
                            fw.write(res.encode('utf-8') + '\n')
                            fw.flush()
                            eng_es_find_dict[candidation["_id"]].append(res)
                            for i in range(begin, end + 1):
                                eng_author_and_words_found_dict[candidation['_id']][i] = 1
                                
#                        else:
#                            print tag,res,begin,end,mention_str,candidation['_id']
                            
                        res = ''
                        is_start = True
    except Exception,e:
        print line
        print traceback.format_exc()
            
fw.close()
filter_for_research_file.close()
pickle.dump(eng_author_and_words_found_dict, file('eng_es_found_loc.pk', 'wb'))
pickle.dump(eng_es_find_dict,file("eng_es_found.pk","wb"))
#%%Spanish
print 'Spanish'
spa_pos_num_dict = pickle.load(file('spa_file_len.pk', 'rb'))
spa_es_find_dict = {}
spa_author_and_words_found_dict = {}

filter_for_research_file = open('../dict/spanish_sort.dict', 'r')
fw = open('../data/result/spa/spa_res.tab', 'w')
url_str = 'http://10.110.6.43:9200/spanish_kbp2016/text/_search?size=40000'

p = re.compile('<tag>.+?</tag>')
for line in filter_for_research_file:
    print line
    line = line.strip().decode('utf-8')
    line_list = line.split('\t')
    mention_str = line_list[0]
    decodejson = json.loads(http_post(url_str, mention_str))
    cnt = 0
    try:
        for candidation in decodejson["hits"]["hits"]:
            spa_author_and_words_found_dict.setdefault(candidation['_id'], [0]*(int(spa_pos_num_dict[candidation['_id']]) + 1))
            spa_es_find_dict.setdefault(candidation["_id"],[])
            flag = 0
            res = ''
            is_start = True
            length = len(mention_str.split(' '))
            for tag in candidation['highlight']['snt']:
                #print tag
                for match in p.finditer(tag):
                    tag = match.group(0)
                    tag_seg = tag.split(':')
                    flag = (flag + 1) % length
                    if(tag_seg[0][-1] == '>'):
                        res += ' '
                    else:
                        res += tag_seg[0][5:] + ' '
                    if(is_start):
                        begin = int(tag_seg[1])
                        is_start = False
                    if(not flag):
                        end = int(tag_seg[2][:-6])
                        res = res.strip(' ')
                        if res != mention_str:
                            res = ''
                            is_start = True
                            continue
    #                    if(end < begin or end - begin > 100):
    #                        print res
                        if(end > begin  and res.lower() == mention_str.lower() and end-begin < 100 and not is_repetition(spa_author_and_words_found_dict, candidation['_id'], begin, end + 1)):            
                            res += '\t' + candidation['_id'] + ':' + str(begin) + '-' + str(end) + '\t' + line_list[1] + '\t' + line_list[2]
                            fw.write(res.encode('utf-8') + '\n')
                            fw.flush()
                            spa_es_find_dict[candidation["_id"]].append(res)
                            for i in range(begin, end + 1):
                                spa_author_and_words_found_dict[candidation['_id']][i] = 1
                        res = ''
                        is_start = True
    except Exception,e:
        print line
        print traceback.format_exc()
            
fw.close()
filter_for_research_file.close()
pickle.dump(spa_author_and_words_found_dict, file('spa_es_found_loc.pk', 'wb'))
pickle.dump(spa_es_find_dict,file("spa_es_found.pk","wb"))
'''
