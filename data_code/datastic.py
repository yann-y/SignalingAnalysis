import pandas as pd

df = pd.read_csv("./data/服创大赛-出行方式静态数据.csv", encoding="gbk")
print(df.info())
print(df.head(10))

