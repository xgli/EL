# -*- coding: utf-8 -*-

import os
import logging
import math
import cPickle as pickle
import numpy as np
from gensim import models,corpora,matutils
import traceback

def init_mention_tfidf_expand(mention_tfidf_expand_file_path):
    mention_tfidf_dic = {}
    querys = []
    fr = open(mention_tfidf_expand_file_path, 'r')
    for line in fr:
        line = line.strip()
        line_list = line.split()
        querys.append(line_list[0])
    for queryid in querys:
        tfidf = 0.5
        mention_tfidf_dic.setdefault(queryid,{})
        mention_tfidf_dic[queryid]["tf-idf"] = tfidf

    return mention_tfidf_dic

#%%
def get_query_entity_document_score(query_document_tfidf_vector,candidate_entity_id):
    try:
        entity_document_tfidf_vector = temp_entity_id_content_vec_dic[candidate_entity_id]
    except KeyError as ke:
        content_text = ""
        fr = open(entity_documents_file_path + candidate_entity_id, 'r')
        line = fr.readline().decode(coding)
        while(line):
            line = line.strip()
            if(line != ''):
                content_text += line + " "
            line = fr.readline().decode(coding)
        fr.close()
        entity_document_tfidf_vector = tfidf_model[tfidf_dictionary.doc2bow(content_text.strip().lower().split())]
        temp_entity_id_content_vec_dic[candidate_entity_id] = entity_document_tfidf_vector

    score = matutils.cossim(query_document_tfidf_vector,entity_document_tfidf_vector)

    return score
#%%

def get_entity_to_from_page_list(search_entity_id):
    page_id_list = []
    try:
        page_id_list = temp_entity_id_to_page_id_dic[search_entity_id]
    except KeyError as ke:
        pass
    return page_id_list
    
def get_entity_entity_score(first_entity_id,second_entity_id,entity_entity_score_method):
    from_entity_total_count = 3425926
    try:
        score = temp_entity_entity_score_dic[(first_entity_id,second_entity_id)]
    except KeyError as ke:
        first_entity_page_list = get_entity_to_from_page_list(first_entity_id)
        second_entity_page_list = get_entity_to_from_page_list(second_entity_id)

        A = len(first_entity_page_list)
        B = len(second_entity_page_list)
        AB = len(set(first_entity_page_list).intersection(set(second_entity_page_list)))
#        A = 3
#        B = 3
#        AB = 1
        if(entity_entity_score_method == "1"):
            if(AB == 0): return 0
            score = math.log(from_entity_total_count) - (math.log(max([A,B])) - math.log(AB)) / (math.log(from_entity_total_count) - math.log(min([A,B])))
        elif(entity_entity_score_method == "2"):
            if(AB == 0):return 0
            NGD_score = (math.log(max([A,B])) - math.log(AB)) / (math.log(from_entity_total_count) - math.log(min([A,B])));
            score = 1 / (NGD_score + 1)
        """
        if(score < 0):
            print(A)
            print(B)
            print(AB)
       """

        temp_entity_entity_score_dic[(first_entity_id,second_entity_id)] = score
        temp_entity_entity_score_dic[(second_entity_id,first_entity_id)] = score
    return score
#%%
        
coding = "utf-8"

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)

eng_tf_idf_model_file_path = "../../../data/resource/eng_tf_idf.model"
eng_tf_idf_dictionary_file_path = "../../../data/resource/english.dic"
eng_tfidf_model = models.TfidfModel.load(eng_tf_idf_model_file_path)
eng_tfidf_dictionary = corpora.Dictionary.load(eng_tf_idf_dictionary_file_path)

cmn_tf_idf_model_file_path = "../../../data/resource/cmn_tf_idf.model"
cmn_tf_idf_dictionary_file_path = "../../../data/resource/chinese.dic"
cmn_tfidf_model = models.TfidfModel.load(cmn_tf_idf_model_file_path)
cmn_tfidf_dictionary = corpora.Dictionary.load(cmn_tf_idf_dictionary_file_path)

spa_tf_idf_model_file_path = "../../../data/resource/spa_tf_idf.model"
spa_tf_idf_dictionary_file_path = "../../../data/resource/spanish.dic"
spa_tfidf_model = models.TfidfModel.load(spa_tf_idf_model_file_path)
spa_tfidf_dictionary = corpora.Dictionary.load(spa_tf_idf_dictionary_file_path)

mention_tfidf_expand_file_path = "mentionlist.tab"
entity_documents_file_path = "../../../data/entityText/eng/"
entity_link_to_entity_file_path = "../entity_link_to_entity_file"

candidate_dir = "../../../data/candidate/eng/"

output_dir = "../../../data/collective_linking_result/eng/"
mention_entity_score_dir = "../../../data/mention_entity_score/eng/"

#from entity_id to page_id dic...
temp_entity_id_to_page_id_dic = {}#entity_id-[page_id1,page_id2,page_id3,...]
temp_entity_entity_score_dic = {}#(entity_id_1,entity_id_2)-score

#entity entity score parameter
entity_entity_score_method = "2";

print("init temp entity id to page id dic...")
fr = open(entity_link_to_entity_file_path,'r')

line_count = 0
line = fr.readline().decode('utf-8')
while(line):
    line_count += 1
    if(line_count % 10000 == 0):print(line_count)
    line = line.strip().decode('utf-8')
    if(line != ''):
        line_list = line.split("\t")
        entity_id = line_list[0]
        entity_id_list = line_list[1:]

        temp_entity_id_to_page_id_dic[entity_id] = entity_id_list

    line = fr.readline()

fr.close()

#temp entity_id content vec
temp_entity_id_content_vec_dic = {}#entity_id-content_vec

#init mention tf-idf expand xml file...
mention_information_dic = init_mention_tfidf_expand(mention_tfidf_expand_file_path)#query_id td-idf or other keys
#init tf-idf model
print("init tf-idf model...")
#tfidf_model = models.TfidfModel.load(tf_idf_model_file_path)
#tfidf_dictionary = corpora.Dictionary.load(tf_idf_dictionary_file_path)

#%%
#visit candidate
#for _,_,candidate_file_name_list in os.walk(candidate_dir):
#    break
#candidate_file_name_list = pickle.load(file('../data/resource/mention_files_list.pk', 'rb'))
candidate_file_name_list = os.listdir("../../../data/candidate/eng/")
collective_linking_result_file = open('collective_linking_result.txt', 'w')
for candidate_file_name in candidate_file_name_list:
    #if(os.path.exists(output_dir + candidate_file_name) == True):continue
#    if( not candidate_file_name.startswith('SPA_')):
#        continue
    print("process {0}...".format(candidate_file_name))
    #init query document tf-idf vector
    if('CMN_' in candidate_file_name):
        source_docs_dir_path = "../../../data/mentionText/cmn/"
        tfidf_model = cmn_tfidf_model
        tfidf_dictionary = cmn_tfidf_dictionary
    if('SPA_' in candidate_file_name):
        source_docs_dir_path = "../../../data/mentionText/spa/"
        tfidf_model = spa_tfidf_model
        tfidf_dictionary = spa_tfidf_dictionary
    if('ENG_' in candidate_file_name):
        source_docs_dir_path = "../../../data/mentionText/eng/"
        tfidf_model = eng_tfidf_model
        tfidf_dictionary = eng_tfidf_dictionary
        
    fr = open(source_docs_dir_path + candidate_file_name,'r')

    context_text = ""
    line = fr.readline()
    while(line):
        line = line.strip().decode('utf-8')
        if(line != ''):
            context_text += line + " "
        line = fr.readline()
    fr.close()

    query_document_tfidf_vector = tfidf_model[tfidf_dictionary.doc2bow(context_text.strip().lower().split())]

    #init mention and entity
    mention_list = []#mention query_id list
    entity_list = []#entity entity_id list

    mention_candidate_dic = {}#mention  candidate_list
    #init mention-candidate
    fr = open(candidate_dir + candidate_file_name,'r')

    mention_total_value = 0.0

    line = fr.readline()
    if(0 == len(line)):
        continue
    while(line):
        line = line.strip().decode('utf-8')

        if(line != ''):
            if(line.startswith("@")):
                #query_id = line.split("\t")[0].strip("@")
                query_id = line.split('\t')[1]
                mention_information_dic[query_id]["info"] = line.strip('@')
                mention_total_value += mention_information_dic[query_id]["tf-idf"]

                mention_list.append(query_id)
                mention_candidate_dic.setdefault(query_id,[])

            else:
                entity_id = line
                
                

                if(entity_id not in  mention_candidate_dic[query_id]):
                    mention_candidate_dic[query_id].append(entity_id)

                if(entity_id not in entity_list):
                    entity_list.append(entity_id)

        line = fr.readline()
    fr.close()
    
    print("\tmention_list_len:{0}".format(len(mention_list)))
    print("\tentity_list_len:{0}".format(len(entity_list)))


    #mention_document entity document score
    print("\tmention document and entity document score...")
    fw = open(mention_entity_score_dir + candidate_file_name,'w')
    mention_entity_score_dic = {}#mention-entity_id_list-score total_score
    for query_id in mention_candidate_dic:
        candidate_list = mention_candidate_dic[query_id]

        mention_entity_score_dic[query_id] = {}
        mention_entity_score_dic[query_id]["total_score"] = 0.0
        mention_entity_score_dic[query_id]["entity_score"] = {}#entity_id cosine-score

        for candidate_entity_id in candidate_list:
            score = get_query_entity_document_score(query_document_tfidf_vector,candidate_entity_id)

            fwString = "{0}\t{1}\t{2}\n".format(query_id,candidate_entity_id,score)
            fw.write(fwString)

            mention_entity_score_dic[query_id]["entity_score"][candidate_entity_id] = score
            mention_entity_score_dic[query_id]["total_score"] += score

    fw.close()
    

#entity-entity score
    print("\tentity and entity score...")
    entity_entity_score_matrix = []#[[] [] []]

    entity_list_len = len(entity_list)
    for i in range(entity_list_len):
        row_list = []
        first_entity_id = entity_list[i]
        for j in range(entity_list_len):
            if(i == j):row_list.append(0)
            else:
                second_entity_id = entity_list[j]
                #print("\t\t{0}\t{1} score...".format(first_entity_id,second_entity_id))
                score = get_entity_entity_score(first_entity_id,second_entity_id,entity_entity_score_method)
                row_list.append(score)
        entity_entity_score_matrix.append(row_list)
    entity_entity_score_matrix = np.array(entity_entity_score_matrix)

    for i in range(entity_list_len):
        row_total = np.sum(entity_entity_score_matrix[i])
        if(row_total == 0):continue
        for j in range(entity_list_len):
            entity_entity_score_matrix[i][j] = entity_entity_score_matrix[i,j] / row_total

    #print("shape:{0}".format(entity_entity_score_matrix.shape))
    #print(entity_entity_score_matrix)
    #init s


    print("\tinit s...")
    init_s = []
    for query_id in mention_list:
        init_s.append(mention_information_dic[query_id]["tf-idf"] / mention_total_value)
    for entity_id in entity_list:
        init_s.append(0)
    #print("\ts = {0}".format(init_s))
    init_s = np.matrix(init_s).T
    #init T matrix
    mention_list_len = len(mention_list)
    entity_list_len = len(entity_list)
    T_matrix_size = mention_list_len + len(entity_list)
    T_matrix = np.zeros(T_matrix_size * T_matrix_size).reshape((T_matrix_size,T_matrix_size))
    #init T entity part
    for i in range(entity_list_len):
        for j in range(entity_list_len):
            T_matrix[i + mention_list_len][j + mention_list_len] = entity_entity_score_matrix[j][i]
    #inti T mention-entity part
    entity_id_index_list = [(entity_list[index],index) for index in range(entity_list_len)]
    for i in range(mention_list_len):
        for j in range(entity_list_len):#mention to entity score
            #mention_entity_score_dic[query_id]["entity_score"][candidate_entity_id] = score
            #mention_entity_score_dic[query_id]["total_score"] += score
            query_id = mention_list[i]
            entity_id = entity_list[j]
            try:
                if(mention_entity_score_dic[query_id]["total_score"] == 0):
                    T_matrix[j + mention_list_len][i] = 0
                    #print(query_id)
                else:
                    T_matrix[j + mention_list_len][i] = mention_entity_score_dic[query_id]["entity_score"][entity_id] / mention_entity_score_dic[query_id]["total_score"]
            except KeyError as ke:
                pass
    #solve analysis solution
    print("\tsolve solution...")
    lambda_value = 0.1
    c = 1 - lambda_value
    I_matrix = np.matrix(np.identity(T_matrix_size))
    T_matrix = np.matrix(T_matrix)

    #print(T_matrix)
    #r = lambda_value(I - cT)-1s c = 1 - lambda_value
    try:
        r = lambda_value * ((I_matrix - c * T_matrix).I) * init_s

        #write result to file
        fw = open(output_dir + candidate_file_name,'w')

        for i in range(r.shape[0]):
            if(i < mention_list_len):
                fwString = mention_information_dic[mention_list[i]]["info"]
                #fw_list = []
                fw_dict = {}
                for x in range(entity_list_len):
                    if(entity_list[x] in mention_candidate_dic[mention_list[i]]):
                        #fw_list.append(entity_list[x])
                        #fw_list.append(str(r[x + mention_list_len,0]))
                        fw_dict[entity_list[x]] = r[x + mention_list_len,0]
                #fwString += '\t'.join(fw_list) + '\n'
                fwString += '\t' + str(fw_dict) + '\n'

#            else:
#                fwString = "{0}\t{1}\n".format(entity_list[i - mention_list_len],r[i,0])

                fw.write(fwString.encode('utf-8'))
                fw.flush()
                collective_linking_result_file.write(fwString.encode('utf-8'))
                fw.flush
        fw.close()
    except ValueError as ve:
        print("Can not process:{0}".format(candidate_file_name))
        print traceback.format_exc()
collective_linking_result_file.close()
