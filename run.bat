rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.imeds.data.OutlierGen --master local[4] target\demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar data\lr-data\random.data 4 3 >>1.txt
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class com.myMaven.demo.SimpleApp --master local[4] target\demo-0.0.1-SNAPSHOT.jar
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.apache.spark.examples.JavaWordCount --master local[4] target\demo-0.0.1-SNAPSHOT.jar README.md
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.apache.spark.examples.JavaTC --master local[4] target\demo-0.0.1-SNAPSHOT.jar 
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.apache.spark.examples.JavaPageRank --master local[4] target\demo-0.0.1-SNAPSHOT.jar ..\sparkTest\data\pagerank_data.txt 5
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.apache.spark.examples.JavaLogQuery --master local[4] target\demo-0.0.1-SNAPSHOT.jar
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.apache.spark.examples.mllib.JavaLR --master local[4] target\demo-0.0.1-SNAPSHOT.jar ..\sparkTest\mllib\data\lr-data\random.data 4 3
rem ..\spark-1.0.0-bin-hadoop1\spark-1.0.0-bin-hadoop1\bin\spark-submit --class org.apache.spark.examples.JavaHdfsLR --master local[4] target\demo-0.0.1-SNAPSHOT.jar ..\sparkTest\data\lr_data.txt 20

java -cp .\lib\*;.\target\demo-0.0.1-SNAPSHOT.jar com.myMaven.demo.matrixTest
pause