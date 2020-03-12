import pandas as pd

csv_data = pd.read_csv("服创大赛-基站经纬度数据.csv", usecols=['longitude','latitude','laci'])
data1 = pd.read_csv("未关联数据.csv", usecols=['timestamp','imsi','lac_id','cell_id'])
data1['lac_id'] = data1['lac_id'].astype(str)
data1['cell_id'] = data1['cell_id'].astype(str)
data1['laci'] = data1['lac_id']+'-'+data1['cell_id']
#print(data1.head())

#print(csv_data.laci.count())
#print(csv_data.laci.nunique())

#print(csv_data.info())
for index,timeNum in enumerate(data1.laci):

    df = csv_data.loc[csv_data['laci'] == timeNum]
    if(len(df.index)==1):
        #print(df.iloc[0,0])
        data1.loc[index,['lac_id','cell_id']] = [df.iloc[0,0],df.iloc[0,1]]
data1 = data1.rename(columns={'lac_id':'longitude'})
data1 = data1.rename(columns={'cell_id':'latitude'})
print(data1.head())
print(data1.info())
data1.to_csv('关联数据.csv')
