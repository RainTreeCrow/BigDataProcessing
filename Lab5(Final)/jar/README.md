# README

原运行环境：hadoop-2.7.1，java-1.7.0_79，idea-IC-213.7172.25

### 任务 1：数据预处理

```
hadoop jar Preprocess.jar /xiyouji /xiyouji_name_list.txt /Preprocess
```

### 任务 2：人物同现统计

```
hadoop jar Cooccurance.jar /Preprocess /Cooccurance
```

### 任务 3：人物关系图构建与特征归一化

```
hadoop jar Normalization.jar /Cooccurance /Normalization
```

### 任务 4：基于人物关系图的 PageRank 计算

```
hadoop jar PageRank.jar /Normalization /PageRank
hadoop jar PageRank.jar /Normalization /PageRank (times)
```



### 任务 5：人物关系图上的标签传播（选做）

同步+随机更新：LabelPropSync

```
hadoop jar LabelPropSync.jar /Normalization /LabelPropSync
hadoop jar LabelPropSync.jar /Normalization /LabelPropSync (20)
```

异步更新：LabelPropAsync

```
hadoop jar LabelPropAsync.jar /Normalization /LabelPropAsync
hadoop jar LabelPropAsync.jar /Normalization /LabelPropAsync (10)
```

异步更新+迭代终止条件判断：LabelPropStop

```
hadoop jar LabelPropStop.jar /Normalization /LabelPropStop
```