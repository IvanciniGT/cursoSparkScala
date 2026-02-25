import org.apache.spark.{SparkConf, SparkContext}

import java.util.concurrent.atomic.LongAdder
//import scala.collection.parallel.CollectionConverters._

object ExtraerHashtagsSpark {

  def main(args: Array[String]): Unit = {

    // Al trabajar con Spark, lo que hacemos es mandar nuestro algoritmo map/reduce a un cluster de máquinas que lo ejecutarán.
    // El cluster me mandará el resultado de vuelta.

    // Paso 1. Conectarnos a un cluster de Spark
    val configuracionConexion = new SparkConf()
                                  .setAppName("ExtraerHashtags")  // Identificador de mi app en el cluster (mpara monitorización)
                                  .setMaster("local[10]")          // La ruta del cluster al que me conecto
                                                                  // Eso suele ser de la forma: "spark://ip-del-nodo-maestro:puerto"
    // Como estamos desarrollando y no queremos ejecutar esto contra un cluster de verdad, Spark me permite levantar un cluster de pruebas en mi máquina (1 solo nodo)
    // Esto es para ejecutar en local. Entre corchete ponemos el número de core que quiero que pueda usar el cluster local.
    // No pongo *... me deja máquina frita.
    val conexion = new SparkContext(configuracionConexion)

    val baseTweets = List(
      "En la playa con mis amig@s #veranito#GoodVibes#FriendForever#PedoPis",
      "En casa con mi perrito #DogLover#PetLover#GoodVibes#CuloCaca",
      "Preparando la oposición #MierdaVibes#Estudio#QuieroSerFuncionaria",
      "1, 2,3 nos vamos otra vez! #MasVerano#GoodVibes #PartyAllNight#OtroCaca"
    )
    val tweets: List[String] = List.fill(500000)(baseTweets).flatten // Tenemos una lista con 2.000.000 tweets (500k veces la misma lista de 4 tweets)
    val palabrasProhibidas = List("caca", "culo", "pedo", "pis", "mierda")

    //var numeroDeHashtagsEliminados = 0L
    val numeroDeHashtagsEliminados: LongAdder = new LongAdder() // Para evitar la race condition. El long adder es lo que denomínamos: Thread-safe.
    // Es decir, es una estructura de datos que se puede usar de forma concurrente sin necesidad de sincronización explícita (sin necesidad de usar locks)
    // Me garantiza la atomicidad de las operaciones (en este caso, la operación increment() que suma 1 al contador) y la visibilidad de los cambios realizados por un hilo a otros hilos.

    val resultado: Array[String] = conexion.parallelize(tweets)
                                        .map(      _.replaceAll("#"," #")              ) // Separando Hashtags juntos
                                        .flatMap(  _.split("[.; ,:_\"'¿?¡!()-]+")      ) // separando términos en el tweet (palabras, hashtags)
                                        .filter(   _.startsWith("#")                   ) // Nos quedamos solo con los hashtags
                                        .map(      _.substring(1)                      ) // Eliminamos el símbolo # del principio del hashtag
                                        .map(      hashtag => hashtag.toLowerCase()    ) // Normalizamos case (minúsculas)
                                        //.filter(   hashtag => palabrasProhibidas.filter(palabraProhibida=> hashtag.contains(palabraProhibida)).length == 0 ) // Eliminamos los hashtags que contienen palabras prohibidas
                                        .filter(   hashtag => {
                                                                val contieneProhibidas = palabrasProhibidas.exists(palabraProhibida=> hashtag.contains(palabraProhibida))
                                                                //if (contieneProhibidas) numeroDeHashtagsEliminados += 1 // Race condition: Al ser un contador compartido entre hilos, el resultado no va a ser bueno.
                                                                                                                        // La operación += 1 no es atómica. Realmente por dentro es:
                                                                                                                        // 1. Leer el valor actual de numeroDeHashtagsEliminados
                                                                                                                        // 2. Sumarle 1 al valor leído
                                                                                                                        // 3. Escribir el nuevo valor de vuelta en numeroDeHashtagsElimin
                                                                if(contieneProhibidas) numeroDeHashtagsEliminados.increment() // Esta operación se hace de forma atómica.
                                          // Para evitarlo, tendríamos que usar un contador atómico (AtomicLong) o una estructura de datos concurrente (ConcurrentHashMap) para almacenar los resultados.
                                                                !contieneProhibidas
                                                              }
                                        ).collect()
    //println(s"Número de hashtags eliminados: ${numeroDeHashtagsEliminados}") // Cuando usábamos el Long
    println(s"Número de hashtags eliminados: ${numeroDeHashtagsEliminados.sum()}")

    //for (hashtag <- resultado) {
    //    println(hashtag)
    //}
    println("En total hemos encontrado " + resultado.length + " hashtags")

    // Al paralelizar, hemos tenido que cambiar algo de lo que es la secuencia (el algoritmo base Map-Reduce)? NO realmente
    // Ahora bien... si hay que tener en cuenta que pasamos de programaión monohilo a multihilo, y eso implica que hay que tener cuidado con las variables compartidas entre hilos (en este caso, el contador de hashtags eliminados)
    // Y usar tipos de datos thread-safe (LongAdder)

    // Hay veces que mi programa tarda más tiempo si paralelizo que si no.
    // No solo va de cuantos datos tengo.
    // Va de qué operaciones estoy haciendo con esos datos. Si las operaciones son muy sencillas,
    // el overhead de crear los hilos, gestionar la concurrencia, y que el SO empiece a enrutar operaciones a los cores... y a controlar los huecos que le van quedando, puede ser mayor que el beneficio de paralelizar.

    // La librería parallel collections de scala, paraleliza el trabajo para usar todos los cores disponibles de la máquina.
    // Spark no va a usar todos los cores de mi máquina... va a usar todos los cores de muchas máquinas en un cluster.
    // Eso es algo que hace Spark.. y de lo que a priori yo no debería de preocuparme... A priori!

    // En principio tampoco debía preocuparme de la paralelización con la lib de scala... siempre que solo quiera usar los cores de mi máquina
    // Pero la realidad es que tuvimos que tocar algo? La variable de los hashtags eliminados. Para asegurar que no ocurran race-conditions.
    // Spark nos lo pone un poco más complejo! Al menos a nivel conceptual.
    // Que problema tendremos al llevarnos esto a spark?
    // La variable numeroDeHashtagsEliminados, donde reside ahora mismo? En memoria RAM de mi máquina...
    // Y todos los hilos COMPARTEN VARIABLE... por eso la race-condition.
    // Cuando este programa lo llevemos a Spark... donde va a residir esa variable? En la memoria RAM de cada máquina del cluster.
    // Cada máquina tendrá su variable numeroDeHashtagsEliminados... y no me vale con ir sumando a nivel de esa máquina.
    // Necesito ir sumando a nivel global...

    // Spark me lo resuelve... tiene un concepto similar al LongAdder, que se llama Accumulators.
    // Es una variable global que se puede usar para acumular valores a través de los nodos del cluster.
    // Pero esto hay que tenerlo en cuenta.

    // Cerrar la conexión al cluster
    conexion.stop()  // Como tenemos un cluster local, que se crea en automático, este método también lo cerrará el cluster. (lo apaga)
  }


}