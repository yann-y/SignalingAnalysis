import pandas as pd
import numpy as np
import  re
import time
csv_data = pd.read_csv("服创大赛-原始数据.csv", usecols=['timestamp','imsi','lac_id','cell_id'])
csv_data.dropna(axis=0, how='any', inplace=True)

# axis：0-行操作（默认），1-列操作
# how：any-只要有空值就删除（默认），all-全部为空值才删除
# inplace：False-返回新的数据集（默认），True-在愿数据集上操作

str_index = []
csv_data.reset_index(drop=True, inplace=True)
for index,item in enumerate(csv_data.imsi):
    str1 = "^"
    str2 = "#"
    str3 = "*"
    if(str1 in item):
        str_index.append(index)
    if(str2 in item):
        str_index.append(index)
    if (str3 in item):
        str_index.append(index)
str_index.sort(reverse=True)
for index  in str_index:
    csv_data.drop(index,axis=0,inplace=True)
csv_data.reset_index(drop=True, inplace=True)
for index,timeNum in enumerate(csv_data.timestamp):
    timeStamp = float(timeNum / 1000)
    timeArray = time.localtime(timeStamp)
    otherStyleTime = time.strftime("%Y%m%d%H%M%S", timeArray)

    if(time.strftime("%Y%m%d", timeArray) == "20181003"):
       # print(time.strftime("%Y%m%d", timeArray))
       csv_data.loc[index, 'timestamp'] = otherStyleTime
    else:
        csv_data.drop(index, axis=0, inplace=True)
    #csv_data.loc[index,'timestamp'] = otherStyleTime
csv_data.reset_index(drop=True, inplace=True)
data_sort = csv_data.sort_values(by=['imsi','timestamp'],ascending=(False,True),inplace=True)
#print(csv_data.head(10))
csv_data.to_csv('未关联数据.csv')
print(csv_data.info())