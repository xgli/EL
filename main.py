
entity_discovery_list = []
for filename in entity_discovery_list:
    print filename
    lan = filename[0:3]
    entity_discovery_file_path = os.path.join(config_dic['entity_discovery'], filename)
    entity_discovery_file = open(os.path.join(entity_discovery_path, filename), 'r')
    candidation_file = open(os.path.join(candidate_path, filename), 'w')
    try:
        for line in entity_discovery_file:
            line = line.strip().decode('utf-8')
            if(not line):
                continue
            line_list = line.split('\t')
            mention = line_list[0]
            mention_type = line_list[-1]
            mention_id = line_list[-2]
            #print '--------' + mention + '----------'
            fwStr = '@' + line + '\n'
            candidation_file.write(fwStr.encode('utf-8'))
            mention_tfidf_expand.write(mention_id + "\n")
            #count += 1
            #mention_id = "EDL14_ENG_" + str(count).zfill(5)
            candidat_entity_list = create_entities(entity_documents_path, mention, mention_type, lan)
            
#            checked_entity_list = []
#            for entity_id in candidat_entity_list:
#                if(check_entity(entity_id, notable_type_dic, mention_type)):
#                    checked_entity_list.append(entity_id)
                    
            res = '\n'.join(candidat_entity_list)
            #print res
            
                
            if(not res.strip()):
                continue
            for entity_id in candidat_entity_list:
                entity_link_to_entity_res = entity_link_to_entity(entity_id, mention_type)
                if(entity_link_to_entity_res == None):
                    continue
                fwStr = entity_id + '\t' + '\t'.join(entity_link_to_entity_res) + '\n'
                entity_link_to_entity_file.write(fwStr.encode('utf-8'))
                entity_link_to_entity_file.flush()
                
            candidation_file.write(res + '\n')
            candidation_file.flush()
        entity_discovery_file.close()
        candidation_file.close()
    except Exception, e:
        print filename, '\t', mention
        print e.message
        print traceback.format_exc()
mention_tfidf_expand.close()
entity_link_to_entity_file.close()

#os.system('java -Xms512m -Xmx1024m -jar D:/EntityDiscovery/ChineseEntityDiscovery/ChineseSegment.jar D:/data/resource/Chinese_entity D:/data/resource/entity_documents')
