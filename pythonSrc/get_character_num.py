# -*- coding: utf-8 -*-

import os
from xml.etree.ElementTree import ElementTree
import cPickle as pickle

#%%Chinese
character_num_dict = {}
f = file('cmn_file_len.pk', 'wb')
entity_discovery_path = '../data/raw/cmn/df/'
for root, dirs, files in os.walk(entity_discovery_path):
    for filename in files:
        #print filename
        tree = ElementTree()
        tree.parse(os.path.join(root, filename))
        tree_root = tree.getroot()
        doc = tree_root.getchildren()[0]
        text = doc.getchildren()[0]
        seg = text.getchildren()[-1]
        character_num_dict[filename.split('.')[0]] = seg.attrib['end_char']
        #print seg.attrib['end_char']

entity_discovery_path = '../data/raw/cmn/news/'
for root, dirs, files in os.walk(entity_discovery_path):
    for filename in files:
        #print filename
        if not filename.endswith("xml"):
            continue
        tree = ElementTree()
        tree.parse(os.path.join(root, filename))
        tree_root = tree.getroot()
        doc = tree_root.getchildren()[0]
        text = doc.getchildren()[0]
        seg = text.getchildren()[-1]
        character_num_dict[filename.split('.')[0]] = seg.attrib['end_char']
        #print seg.attrib['end_char']
pickle.dump(character_num_dict, f)
f.close()

#%%English
eng_pos_num_dict = {}
f = file('eng_file_len.pk', 'wb')
entity_discovery_path = '../data/raw/eng/news/'
for root, dirs, files in os.walk(entity_discovery_path):
    for filename in files:
        if not filename.endswith("xml"):
            continue
        #print filename
        tree = ElementTree()
        tree.parse(os.path.join(root, filename))
        tree_root = tree.getroot()
        doc = tree_root.getchildren()[0]
        text = doc.getchildren()[0]
        seg = text.getchildren()[-1]
        eng_pos_num_dict[filename.split('.')[0]] = seg.attrib['end_char']
        #print seg.attrib['end_char']
entity_discovery_path = '../data/raw/eng/df/'
for root, dirs, files in os.walk(entity_discovery_path):
    for filename in files:
        if not filename.endswith("xml"):
            continue
        #print filename
        tree = ElementTree()
        tree.parse(os.path.join(root, filename))
        tree_root = tree.getroot()
        doc = tree_root.getchildren()[0]
        text = doc.getchildren()[0]
        seg = text.getchildren()[-1]
        eng_pos_num_dict[filename.split('.')[0]] = seg.attrib['end_char']
        #print seg.attrib['end_char']
pickle.dump(eng_pos_num_dict, f)
f.close()


#%%Spanish
spa_pos_num_dict = {}
f = file('spa_file_len.pk', 'wb')
entity_discovery_path = '../data/raw/spa/news/'
for root, dirs, files in os.walk(entity_discovery_path):
    for filename in files:
        if not filename.endswith("xml"):
            continue
        #print filename
        tree = ElementTree()
        tree.parse(os.path.join(root, filename))
        tree_root = tree.getroot()
        doc = tree_root.getchildren()[0]
        text = doc.getchildren()[0]
        seg = text.getchildren()[-1]
        spa_pos_num_dict[filename.split('.')[0]] = seg.attrib['end_char']
        #print seg.attrib['end_char']
entity_discovery_path = '../data/raw/spa/df/'
for root, dirs, files in os.walk(entity_discovery_path):
    for filename in files:
        if not filename.endswith("xml"):
            continue
        #print filename
        tree = ElementTree()
        tree.parse(os.path.join(root, filename))
        tree_root = tree.getroot()
        doc = tree_root.getchildren()[0]
        text = doc.getchildren()[0]
        seg = text.getchildren()[-1]
        spa_pos_num_dict[filename.split('.')[0]] = seg.attrib['end_char']
        #print seg.attrib['end_char']
pickle.dump(spa_pos_num_dict, f)
f.close()
