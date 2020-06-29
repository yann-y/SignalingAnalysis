交通时空大数据分析

[TOC]

## 简介

小鸡炖蘑菇-A06-交通时空信令大数据挖掘系统

根据赛题对实时性的要求,本系统将已收集的数据根据时间戳进行排序,形成时间序列,用kafka模拟日志收集。

根据出题方建议,kafka模拟每5s收集一次日志数据

根据出题方的功能要求,系统可以分为三个主要的模块:**实时人群密度图**,**出行方式分析**,**驻留分析**

于是在日志收集后,将数据分别流向三个不同的数据加工/分析模块

对于**实时人群密度图模块**,**驻留点分析模块**的处理可以在数据清洗的同时进行数据分析

对于**出行方式分析模块**的处理分为两步

+ 出行链提取
+ 使用Flink聚类算法以及坐标匹配算法对出行方式进行标签化

根据出题方的要求在数据挖掘完毕后,要对数据挖掘后的结果进行美观和实时的展示

于是使用SpringBoot框架集成Kafka对以上三个模块的数据进行实时接收:

+ 对于**人群密度图模块**

  考虑到实时性,人群密度图实现了可以选择间隔5s,1min,60min更新一次

+ 对于**驻留分析模块**

  考察**驻留**的定义,驻留分析图可以选择间隔5~30min,5~60min,6~90min更新一次

+ 对于**出行方式分析模块**

  考察**出行**的定义,出行方式分析的数据选择间隔30min更新一次

系统架构图

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/系统架构图5.0.png" alt="系统架构图" style="zoom:20%;" />

## 环境说明

+ **Java version 1.8**
+ **Scala version 2.11.0**
+ **Flink 1.9.1**

## 模块说明

+ Web模块

  接收来自不同数据模块加工后的数据,进行数据展示

+ DataAnalyse模块

  主要是对出行方式进行标签化

+ DataClean模块

  其中包含**日志数据的模拟**,**热力点提取**,**驻留点提取**模块

## Kafka中数据说明

> kafka中每一个不同的topic存放不同的数据

+ timeStream

  存放模拟的日志时间序列流,每5s接收一次

+ heatMapData

  经过flink热力点提取后写入的热力图数据

+ stayData

  经过flink驻留点提取后写入的驻留数据

+ routeChain

  经过flink出行链提取后写入的出行链数据

+ GaussianMixture

  经过GaussianMixture聚类处理后的数据

+ KMeans

  经过KMeans聚类处理后的数据

+ BisectingKMeans

  经过BisectingKMeans聚类处理后的数据

## DataClean模块介绍

数据清洗模块的主要任务有:

### 模拟收集日志数据

由于赛题方提供的数据不是以时间有序的,且赛题方提供的数据量较小

所以,通过数据模拟,将数据的数据量扩大到了百万级别,同时通过时间排序

将20181003当天的数据整理为时间序列流

为了达到模拟的效果,Proucer模块将会每5s向kafka中发送一次数据,也就相当于kafka每5s收到一次日志收集数据.

日志收集数据存放于kafka的**“timeStream”**topic中

流程图如下:

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/producer.jpg" alt="producer" style="zoom:20%;" />

### 热力点数据提取

对于赛题方提出的热力图实时数据提取

+ 订阅kafka中timeStream的数据,对数据进行数据清洗

+ 提取timestamp、imsi、lac_id、cell_id
+ 去除imsi中，包含特殊字符的数据条目(‘#’,’*’,’^’)
+ 去除空间信息残缺的记录条目(imsi、lac_id、cell_id中为空)
+ timestamp时间戳转换格式 ‘20190603000000’--年月日时分秒
+ 去除两数据源关联后经纬度为空的数据条目

将清洗后的数据输出到kafka的heatMapData中

流程图

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/heatMapDataProcess.jpg" style="zoom:35%;" />

### 驻留数据提取

对于赛题方提出的驻留分析的数据提取

+ 订阅kafka中timeStream的数据

+ 数据清洗
+ 以30min为一个时间窗口对驻留数据提取
+ 每30min一次向kafka中的stayData发送数据

流程图

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/stayAnalyse.jpg" style="zoom:20%;" />

### 出行链提取

对于赛题方提出的出行分析,分为两步:第一步出行链提取,第二步出行方式判别

+ 订阅kafka中的timeStream
+ 数据清洗
+ 30min为一个时间窗口收集数据
+ 驻留数据清洗
+ 乒乓数据清洗
+ 漂移数据清洗
+ 将每30min加工后的数据下沉到kafka中的routeChain中

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/routeChain.jpg" style="zoom:20%;" />



## DataAnalyse模块

对DataClean模块中出行链提取后的数据进行进一步加工

+ 订阅kafka中的routeChain
+ 每30分钟一次收集来自kafka的数据
+ 对收集的数据分别使用高斯聚类、均值聚类、二分聚类初步判别出行方式
+ 将三种聚类后的结果分别结合地铁站和公交车站经纬度信息,进一步判断出行方式
+ 将判别完毕后的数据分别写入写入kafka中的GaussianMixture,KMeans,BisectingKMeans

## Web模块

对以上模块加工后的数据进行业务逻辑处理,最终进行web端展示

+ 订阅kafka中的heatMapData,以进行热力图展示
+ 订阅kafka中的stayData,以进行驻留分析展示
+ 订阅kafka中的GaussianMixture,KMeans,BisectingKMeans,以进行出行方式分析展示

## 算法介绍

### 数据说明

手机信令通过全球移动经营商(GSM)在移动基站获取的数据。当移动电话开启时,它会自动和移动基站连接。这时,基站的位置信息也会被GSM记录下来。此外,当以下三种活动[9]发生时,GSM会更新手机所在基站的位置:

1. 该手机用户发生通话
2. 该手机用户接收或发送短信
3. 该手机用户改变了移动基站的位置

此外,GSM还会在一个相对固定的时间间隔内,更新手机用户所在的基站位置。(一般为30min[6])

手机信令数据包含imsi、timestamp、lac_id、cell_id等信息其中,IMSI是手机用户的唯一标识。。TimeStamp则代表了GSM更新基站所在位置的时间。lac_id代表地区区域码,cell_id代表一个移动基站。通过组合lac_id和cell_id就可以获得该手机所在的基站经纬度信息

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/信令数据.png" alt="信令数据" style="zoom:33%;" />

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/经纬度.png" alt="经纬度" style="zoom:33%;" />

### 基本数据清洗

对于题目要求和信令数据的特点设计了基本的数据清洗算法

本算法是适用的数据特点是:流数据和大数据

首先将数据以流的形式读入,然后按以下几个步骤对数据进行清洗:

1. 使用flink的filter算子过滤掉imis包含‘#’,’*’,’^’特殊字符的条目
2. 使用flink的filter算子过滤掉imsi、lac_id、cell_id为空的条目
3. 使用flink的map算子将时间戳进行转换
4. 使用flink的filter算子过滤掉不是20181003的数据
5. 使用flink的filter算子结合本地的经纬度表去除lac_id和cell_id关联为空的数据
6. 最后将数据以特定格式输出

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/数据清洗算法.jpg" alt="流程图" style="zoom:33%;" />

### 出行链提取

在进行基本的数据清洗后,我们得到了一系列以人为单位时间升序的出行链.但是,我们还不能直接对这些出行链进行分析.对于一个人的出行链来说,其中可能存在着驻留数据,乒乓数据,漂移数据.我们需要对这些数据进行过滤.为了进行出行链的提取,我们必须对信令数据的特点和出行的定义进行介绍.然后,我们再进行介绍如何实现过滤驻留数据,乒乓数据,漂移数据.最终提取出出行链.

#### 出行的定义

出行是指从起点到终点移动的全过程[1].一次出行包含以下几个常见属性[2]:

1. 包含出行起点和终点

2. 一次出行使用一种或几种交通方式

3. 一次出行要达到一定的时间和距离

上海市第4次居民出行调查[3]中是这样定义出行的:单程400m以上或步行时间超过5分钟的过程.广州2005年居民出行调查定义出行为:使用某种交通工具出行距离超过500m或步行时间超过5分钟.

根据以上资料,本文中定义出行为:包含起点和终点(且起点和终点不相同)的一次距离大于500或时间超过5min的过程.

#### 数据噪声说明

1. 驻留数据

   根据出行的定义,我们可以将以下两种情况定义为驻留:

   A. 当用户的位置在以某一距离阈值为半径且在某一时间阈值范围内变化,则把该用户看作是一动不动的[4].

   根据出行的定义,以及基站和手机交互时间的特点.我们将这个距离阈值设置为500m,将时间阈值设置为30min

   B. 当用户基站位置没有切换时,也是视为驻留.即用户的连续几个出行点的经纬度相同[5]

2. 乒乓数据

   乒乓数据是指,手机信号在附近的基站来回切换,而实际上该用户并没有移动.如该用户产生的轨迹是A-B-C-B-C-D-C-D,存在在某一区域来回切换的情况,而实际轨迹应该是A-B-C-D.这就是乒乓数据[6].

  <img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/乒乓数据.png" style="zoom:33%;" />


3. 漂移数据
   漂移数据是指用户突然临近基站切换到远处基站,一段时间后又切回临近基站所产生的数据[7].

#### 出行距离计算约定

常规计算出行距离的两种方式:

1. 精确计算
   将识别出来的出行点和路网匹配计算实际的距离
2. 近似计算
   直接计算出行点两点之间的直线距离

由于本文针对的数据对象是大数据,如果对大量数据点进行精确计算匹配路网,计算性能上会有很大的损耗.所以,本文采取近似计算方式.但是,对于近似计算,我们可以使用道路的非直线系数[4]来模拟真实的道路距离情况.道路的非直线系数是指道路起点和终点之间实际的距离与起点和终点空间直线距离的比值[8].参考<城市道路交通规划设计规范GB50220-95>.城市路网的平均非直线系数一般为1.15~1.2.本文使用的数据来源是沈阳市的手机用户.沈阳市是一个新一线城市,所以本文对于沈阳市的道路非直线系数设定为1.2.


#### 驻留数据清洗

针对以上提到的驻留数据,我们设置距离阈值为500,时间阈值为30min.如果当前出行点和上一个出行点的距离小于距离阈值且出行时间小于时间阈值.则判定该用户是一动不动的
此外,对于当前出行点的经纬度等于下一个出行点的经纬度的情况,我们也判定为驻留,保留最后一个点.

流程图:

#### 乒乓数据清洗

针对乒乓数据,根据乒乓数据的特点..对于n-1,n,n+1三个出行点,如果满足n-1的位置和n+1的位置相同,且从n-1到n+1的时间小于时间阈值,则判定n,n+1数据为乒乓数据,移除第n,n+1条数据[7].在本文中根据基站与手机交互的时间间隔,这个时间阈值被设定为30min

流程图:

#### 漂移数据处理

根据漂移数据的特点:短时间内发生大位移.我们首先设置一个速度阈值,当当前出行点和下一个出行点的之间的速度大于时间阈值时,判定下一个出行点为漂移数据并清除.此外,为了防止漂移的距离不是很远的情况出行,我们采取考虑上下点的距离关系.对于n-1,n,n+1三个点.如果n-1和n,n和n+2的距离都大于距离阈值,但是n-1到n+1的距离小于两倍距离阈值,则判定n为漂移点,去除第n个点.

流程图:

### 驻留数据提取

对于驻留数据的提取,考察之前驻留数据的清洗方法.我们只要通过将判断之前去除的数据进行保留,同时,将之前保留的数据去除.最终得到的就是驻留数据.

### GaussianMixture

同样是基于原型的聚类方法，混合高斯聚类（Mixture-of-Gaussian）不同于K-Means和LVQ中使用向量作为聚类原型，他使用概率模型（高斯混合模型）来表示原型，与一元高斯分布类似，多元高斯分布可以表示为：
$$
p(\mathbf{x})=\frac{1}{(2 \pi)^{\frac{n}{2}}|\Sigma|^{\frac{1}{2}}} e^{-\frac{1}{2}(\mathbf{x}-\mathbf{\mu})^{T} \Sigma^{-1}(\mathbf{x}-\mathbf{\mu})}
$$
​	其中，$\mu$是$n$维均值向，$Σ$是$n×n$的协方差矩阵，这两个参数决定了整个高斯分布的概率分布，为了体现出不同模型参数对样本点概率的影响，将该概率密度函数记为 $p(x∣μ,Σ)$。由此引申出如下高斯混合分布$p_M\left(\mathbf{x}\right)$（假设由$k$个多元高斯模型混合）的定义式
$$
{p_m}(x) = \sum\limits_{i = 1}^k {{a_i}\cdotp(x|{\mu _i},\sum\nolimits_i ) } 
$$
上式中$\mu_i$和$\Sigma_i$代表第$i$个多元高斯混合成分的模型参数，$\alpha_i>0$为该混合成分的“混合系数”（mixture coefficient），且$\sum_i{\alpha_i}=1$。高斯混合分布中样本的生成过程，首先根据先验概率$ \alpha_i$选择对应的高斯混合成分，然后根据该混合成分的模型参数$\mu_i$和$\Sigma_i$进行采样，进而生成该样本点。

#### 2、具体算法描述

------

**输入**：样本集$$D = \{ {x_1},{x_2},...,{x_m}\} $$；

​			高斯混合成分个数$k$。

**过程**：

初始化高斯混合分布的模型参数$\{ ({a_i},{\mu _i},\sum\nolimits_i ) |1 \le i \le k\} $

**repeat**

​	**for** $j = 1,2,3,...,m$ **do**

​	根据式${p_M}({z_j} = i|{x_j}) = \frac{{{a_i}\cdotp({x_j}|{\mu _i},\sum\nolimits_i ) }}{{\sum\limits_{l = 1}^k {{a_l}\cdotp({x_j}|{\mu _l},\sum\nolimits_l ) } }}$计算${x_i}$由各混合成分生成的后验概率，即

​			  $${\gamma _{ji}} = {p_M}({z_j} = i|{x_j})(1 \le i \le k)$$

​	**end for**

​	 **for** $j = 1,2,3,...,k$  **do**

​		计算新均值向量：$$\mu _i^, = \frac{{\sum\nolimits_{j = 1}^m {{\gamma _{ji}}{x_j}} }}{{\sum\nolimits_{j = 1}^m {{\gamma _{ji}}} }}$$；

​		计算新协方差矩阵：$\sum\nolimits_i^,  =  \frac{{\sum\nolimits_{j = 1}^m {{\gamma _{ji}}({x_j} - \mu _i^,){{({x_i} - \mu _i^,)}^T}} }}{{\sum\nolimits_{j = 1}^m {{\gamma _{ji}}} }}$；

​		计算新混合系数：$a_i^, = \frac{{\sum\nolimits_{j = 1}^m {{\gamma _{ji}}} }}{m}$；

​	**end for**

​	将模型参数$\{ ({a_i},{\mu _i},\sum\nolimits_i {)|1 \le i \le k\} } $更新为$\{ (a_i^,,\mu _i^,,\sum\nolimits_i^, {)|1 \le i \le k\} } $

​	until** 满足停止条件

${C_i} = \emptyset (1 \le i \le k)$

**for** $j = 1,2,3,...,m$ **do**

​		根据式${\lambda _j} = \mathop {\arg \max }\limits_{i \in \{ 1,2,...,k\} } {\gamma _{ji}}$确定${x_i}$的簇标记为${\lambda _j}$；

​		将${x_i}$划入相应的簇：${C_{{\lambda _j}}} = {C_{{\lambda _j}}} \cup \{ {x_j}\} $

**end for**

​	**输出：**簇划分$C = \{ {C_1},{C_2},...,{C_k}\} $

### KMeans

### BisectingKMeans

### 结合站点的出行方式校验

对于聚类分析后得出的标签化数据,为了进一步的提高标签的准确性.我们将公交车,汽车,地铁三种方式进一步结合站点的经纬度信息进行判断.

首先考察到,沈阳市的地铁站与地铁站之间的平均距离不超过1.5km,公交车站与公交车站的距离不超过1km

我们将之前聚类后的平均出行速度前3的出行方式依次进行如下判断:

首先进行地铁的检验:

对于一条出行链,如果出行链上的每一个点都在地铁站点的一定范围内,那么我们判断,这次出行的出行方式是地铁

然后进行公交车的检验:

对于一条出行链,如果出行链上的每一个点都在公交车站站点的一定范围内,那么我们判断,这次出行的出行方式是公交车

最后,在经过地铁检验和公交车校验后的数据,我们可以比较有把握的判断,这些数据的出行方式属于汽车.



## 效果图

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/出行方式分析.png" style="zoom:48%;" />

<img src="D:/gitee/waibao/参考文件/SignalingBigDataAnalyse/images/人群密度图.png" style="zoom:48%;" />

## 参考文献

[1].昭春福.交通规划原理(第二版)[M].中国铁道出版社,2014

[2].刘华斌. 手机信令数据背景下城市交通出行方式选择辨识方法研究[D].北京交通大学,2019.

[3].路锡明,顾啸涛.上海市第五次居民调查与交通特征研究[J].城市交通,2011,9(05):1-7.

[4].Xiaoxu; Yang, Chao; Xu, Xiangdong,Chen.”Utilizing mobile phone signaling data for trip mode identification ”. CICTP 2019: Transportation in China - Connecting the World - Proceedings of the 19th COTA International Conference of Transportation Professionals, p 4588-4599, 2019, CICTP 2019: Transportation in China - Connecting the World - Proceedings of the 19th COTA International Conference of Transportation Professionals;

[5].董路熙,贾梅杰,刘小明,谭墍元,魏向达.基于手机信令数据的居民工作日出行链判别方法[J].桂林理工大学学报,2019,39(04):958-966.

[6].唐杰. 基于手机信令的出行方式识别方法研究[D].重庆邮电大学,2019.

[7].中山大学.一种手机信令数据清洗方法:CN201810797832.9[P].2018-12-18.

[8]冯树民,陈宏仁.用非直线系数分析哈尔滨市道路网[C]//2004

[9].Li, Zufen; Yu, Lei; Gao, Yong; Wu, Yizheng; Song, Guohua; Gong, Dapeng;”Identifying temporal and spatial characteristics of residents’ trips from cellular signaling data: Case study of Beijing” ; Transportation Research Record, v 2672, n 42, p 81-90, January 1, 2018