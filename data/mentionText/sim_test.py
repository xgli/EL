#!/usr/bin/env python
# coding=utf-8

import os
from gensim import models,corpora, similarities
from gensim.models.doc2vec import TaggedDocument,Doc2Vec

data_dir = "./cmn/"

doc = []
files = os.listdir(data_dir)
for filename in files:
    filepath = data_dir + "/" + filename
    with open(filepath) as fr:
        text = fr.read()
    words_list = text.rstrip().split("\t") 
    doc.append(words_list)


corpora_documents = []
for i, words_list in enumerate(doc):
    document = TaggedDocument(words=words_list,tags=[i])
    corpora_documents.append(document)

model = Doc2Vec(size=50,min_count=1,iter=10)
model.build_vocab(corpora_documents)
model.train(corpora_documents)
print("######", model.vector_size)

inferred_vecotr = model.infer_vector(doc[0])
sims =  model.docvecs.most_similar([inferred_vecotr],topn=3)
print(sims)
