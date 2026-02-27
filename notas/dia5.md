
Voy a montar un cluster real de spark en un cloud.
Pero no voy a contratar el cluster... voy a montar el cluster yo mismo, con máquinas virtuales, en un cloud (AWS)

LINUX


Una vez lo tengamos, le ejecutaremos los trabajos que hemos ido haciendo. Habrá que hacerles algunos cambios.

---

## Arrancar un cluster:

bin/spark-class org.apache.spark.deploy.master.Master 
# Este de arriba es el que me genera la URL del master que usamos abajo

bin/spark-class org.apache.spark.deploy.worker.Worker spark://172.31.76.125:7077

bin/spark-submit --master spark://172.31.76.125:7077 --class PersonasSQL  /home/ubuntu/environment/cursoSparkScala/basicosScala/target/basicos-scala-1.0-SNAPSHOT.jar 