#!/usr/bin/env python
# coding=utf-8

from scipy.cluster.hierarchy import fclusterdata

matData=[[1.,2.,3.0],[2.,4.,6.],[3.,6.,9.0]]
result = fclusterdata(matData,t=0.4, criterion='inconsistent',metric='euclidean',method='average')
print result
#matData : 数据
#t: 阈值
#criterion:输出数据的标准
#metric:距离函数
#method:距离计算方式
#R:不知道？？？

