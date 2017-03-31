# -*- coding: utf-8 -*-
"""
Created on Sun Jul 31 14:20:09 2016

@author: li
"""

import urllib2
import json
import os
import cPickle as pickle
import os
def entity_link_entity(entity_id, mention_type):
    url='http://10.110.6.43:9200/base_kb/entity/_search'
    if mention_type == "GPE":
        search_query = '''{
           "fields": [
              "f_base.locations.countries.continent",
              "f_location.country.currency_used",
              "f_common.topic.notable_types",
              "f_location.country.official_language",
              "f_common.topic.notable_for"
            ],
           "query":{
               "match":{
                   "_id":"f_m.06bnz"
               }
            }
        }'''
        search_dict = eval(search_query)
        search_dict["query"]["match"]["_id"] = entity_id
    elif mention_type == "PER":
        search_query = '''{
           "fields": [
              "f_people.person.gender",
              "f_people.person.profession",
              "f_people.person.nationality",
              "f_people.person.religion",
              "f_people.person.places_lived",
              "f_people.person.education",
              "f_common.topic.notable_types",
              "f_common.topic.notable_for",
              "f_government.politician.party",
              "f_people.person.languages"
           ],
           "query":{
               "match":{
                   "_id":"f_m.06bnz"
               }
           }
        }'''
        search_dict = eval(search_query)
        search_dict["query"]["match"]["_id"] = entity_id
    else:
        search_query = '''{
           "fields": [
              "f_common.topic.notable_types",
              "f_common.topic.notable_for",
              "r_type",
              "f_organization.membership_organization.members"
           ],
           "query":{
               "match":{
                   "_id":"f_m.06bnz"
               }
           }
        }'''
                
    search_dict = eval(search_query)
    search_dict["query"]["match"]["_id"] = entity_id
    jdata = json.dumps(search_dict)             # 对数据进行JSON格式化编码
    req = urllib2.Request(url, jdata)       # 生成页面请求的完整数据
    response = urllib2.urlopen(req)       # 发送页面请求
    result = []
    decodejson = json.loads(response.read())
    print response.read()
    if decodejson["hits"]["hits"] == []:
        return result
    if 'fields' not in decodejson["hits"]["hits"][0]:
        return None
    for entity_type in decodejson["hits"]["hits"][0]["fields"]:
        for entity_name in decodejson["hits"]["hits"][0]["fields"][entity_type]:
            result.append(entity_name)
    #print result
    #return response.read()                    # 获取服务器返回的页面信息
    return result

#print str(entity_link_entity("f_m.0hzlz", "ORG"))

if os.path.isfile("entity_ids.pk"):
    id_set = pickle.load(file("entity_ids.pk","rb"))
else:
    id_set = set([])

entity_link_entity_file = open("entity_link_to_entity_file","a")

candidate_file_path = "../../data/candidate/spa/"
candidate_files = os.listdir(candidate_file_path)
for candidate_file in candidate_files:
    filePath = candidate_file_path + candidate_file
    print filePath
    with open(filePath,"r") as fr:
        text = fr.read()
    lines = text.split("\n")
    mention_type = ""
    for line in lines:
        if line == "":
            continue
        if line.startswith("@"):
            mention_type = line.split("\t")[-1]        
            continue
        else:
            entity_id = line
            if entity_id in id_set:
                continue
            else:
                id_set.add(entity_id)
            entity_link_entity_res = entity_link_entity(entity_id,mention_type)
            if(entity_link_entity_res == None):
                continue
            fwStr = entity_id + "\t" + "\t".join(entity_link_entity_res) + "\n"
            print fwStr            
            entity_link_entity_file.write(fwStr.encode("utf-8"))
            entity_link_entity_file.flush()
entity_link_entity_file.close()
                
pickle.dump(id_set,file("entity_ids.pk","wb"))
        
        
