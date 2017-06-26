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
cmn_author_and_pinyin_found_dict = pickle.load(file('cmn_es_found_loc.pk','rb')) 
cmn_es_find_dict = {}
filter_for_research_file = open('../dict/zuhewithpinyin.dict', 'r')#词表
fw = open('../data/result/cmn/zuhe_es.tab', 'w')


p = re.compile('<tag>.+?</tag>')
url_str = 'http://10.110.6.43:9200/chinese_kbp2016/text/_search?size=40000'
for line in filter_for_research_file:
    print line
    line = line.strip().decode('utf-8')
    line_list = line.split('\t')
    mention_str = line_list[1]
    mention = line_list[0]
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
                        continue
                    end = int(tag_seg[1])
                    res += '\t' + candidation['_id'] + ':' + str(begin) + '-' + str(end) + '\t' + line_list[2] + '\t' + line_list[3]

                    print res
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
pickle.dump(cmn_es_find_dict,file("cmn_es_zuhe_found.pk","wb"))
pickle.dump(cmn_author_and_pinyin_found_dict, file('cmn_es_found_loc_zuhe.pk', 'wb'))

