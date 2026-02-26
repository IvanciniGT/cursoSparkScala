
Option[T]

- Es un tipo de dato que me permite representar la ausencia de valor (None) o la presencia de valor (Some(valor)).
- Al option le podemos preguntar si esta vacío o no (isEmpty, isDefined) y si no esta vacío, podemos obtener el valor que contiene (get).


"2300000T" Esto supuestamente lleva un DNI... Será verdad o no! : String
"federico"



def validarDNI(dni:String): Boolean = {
    ...
}

class DNI <- ADT (Abstract Data Type) (Protección de invariantes de datos)
    - Atributos: numero, letra
    - Métodos:  getNumero, getLetra, dámelo formateado de tal forma, etc...

val datoPotencialmenteDeTipoDNI: String = "2300000T"
val dni: DNI = new DNI(datoPotencialmenteDeTipoDNI) ????
val dni: DNI = DNI.parse(datoPotencialmenteDeTipoDNI)

- Si viene un DNI guay, que de devuelva un DNI
- Pero si viene un DNI no guay:
  - Que no devuelva nada: Option[DNI] = None
    - Menos agresiva!
    - Aunque... uy! Hay una cosa chulita de lo del exception...
  - Pegar un bofetón al usuario (throw an exception) = DNI.parse(datoPotencialmenteDeTipoDNI) --> Exception
    - Más agresiva!
    - Pero... puedo dar información complementaria, de por qué el DNI no es guay (no tiene pinta de DNI, la letra no es correcta, etc...)
      - Puedo hacerlo con distntas exceptions o con una única exception pero con un mensaje de error que me de información complementaria.
  
La opcion con la que nos quedamos es con un híbrido (a nivel funcional):
- Algo que me permita devolver un objeto sin ser agresivo (sin lanzar una exception) pero que a la vez me permita dar información complementaria de por qué el DNI no es guay (no tiene pinta de DNI, la letra no es correcta, etc...) : Either[T,Z]

- El tipo T, el de la izquierda se usa para representar el error (el motivo por el que el DNI no es guay)
- El tipo Z, el de la derecha se usa para representar el valor correcto (el DNI guay)

class DNI {

}

trait DNIError {                // Un objeto de este tipo, siempre tendrá un mensaje de error que me explique por qué el DNI no es guay.
    def message: String
}

FormatoDeDNINoReconocido extends DNIError {
    def message: String = "El formato del DNI no es reconocido"
}

LetraDeDNINoValida extends DNIError {
    def message: String = "La letra del DNI no es válida"
}

object DNI {
    def of(dni:String): Either[DNIError, DNI] = {
    }
    def isValid(dni:String): Boolean = {
    }
}

De hecho el tipo Either se puede transformar a un Option.
val dni: Either[DNIError, DNI] = DNI.of(datoPotencialmenteDeTipoDNI)
val dniOption: Option[DNI] = dni.toOption (Si hay dato en la derecha, lo devuelve envuelto en un Some, si no hay dato en la derecha, devuelve un None)


---



// SOLID:
// L: Principio de Sustitución de Barbara Liskov: Si S es un subtipo de T, entonces los objetos de tipo T pueden ser sustituidos por objetos de tipo S sin alterar las propiedades deseables del programa (corrección, tarea realizada, etc.)


// Monto un programa que use esa libreria de DNIs que tenemos
// Mi librería devoverá: Either[DNIError, DNI]

// Caso que haya un error, lo puedo procesar con un match case, en función de los tipos de error que se que existen (FormatoNoReconocido, LetraIncorrecta)

23000000T VALIDO 
23000000A INVALIDO
23.000.000-T 
23.000.000 t 
15 J

230.0.......0.0.......00 t INVALIDO
230.00.000 t INVALIDO    Tengo menos desconfianza en el dato éste que en el los datos:
    230.0.......0.0.......00 t INVALIDO
    23000000A INVALIDO

Cuadrado no es un rectángulo

class Rectangulo (base: Double, altura: Double) {
    area: Double = base * altura
    perimetro: Double = 2 * (base + altura)
}

class Cuadrado (lado: Double) extends Rectangulo(lado, lado) {
    area: Double = lado * lado
    perimetro: Double = 4 * lado
    setLado(lado: Double): Unit = {
        this.base = lado
        this.altura = lado
    }
    setBase(base: Double): Unit = {
        this.base = base
        this.altura = base
    }
    setAltura(altura: Double): Unit = {
        this.base = altura
        this.altura = altura
    }
}

// Tengo este programa de pruebas:
val rectangulo: Rectangulo = new Cuadrado(3)
assert(rectangulo.area == 9)
assert(rectangulo.perimetro == 12)  
rectangulo.setBase(4)
assert(rectangulo.area == 12)         <--- 16
assert(rectangulo.perimetro == 14)    <--- 16


---

# SparkSQL

SparkCore, lo que hemos trabajado hasta ahora, es quien ofrece una implementación del motor de procesamiento MapReduce.
Eso es duro! Plantear un algoritmo con operaciones MapReduce puras es complejo. Necesito mucha práctica para dominarlo.

La gente de Spark crea una libreria llamada spark-sql que me permite cargar y procesar datos con un lenguaje de consulta similar al SQL, pero que se ejecuta sobre el motor de procesamiento MapReduce de SparkCore.

    Nosotros escribiremos SQL (o algo parecido) y spark-sql lo transforma internamente en operaciones MapReduce que se ejecutan sobre SparkCore.
    Es decir, spark-sql es una capa de abstracción que me permite usar sparkcore y mapreduce, pero escribiendo en lenguaje SQL.

Cualquier cosa la puedo hacer con SparkSQL? NO
Si mis datos encajan con una estructura tabular, sin problema (90% de los casos o más)... pero no siempre va a funcionar.

    TABLA TWEETS:
    COLUMNA: TWEET
      "En la playa con mis amig@s #veranito#GoodVibes#FriendForever#PedoPis",
      "En casa con mi perrito #DogLover#PetLover#GoodVibes#CuloCaca",
      "Preparando la oposición #MierdaVibes#Estudio#QuieroSerFuncionaria",
      "1, 2,3 nos vamos otra vez! #MasVerano#GoodVibes #PartyAllNight#OtroCaca"

    vvvv pasar de TWEETS a HASHTAGS con SQL? EN SERIO????

    TABLA: HASHTAGS
    COLUMNA: HASHTAG
                goodvibes
                goodvibes
                summerLove
                goodvibes
                lovemyfamily
                ...

Quiero generar la tabla de tranding topics... Me podeis dar el SQL?

    SELECT hashtag, COUNT(*) AS count
    FROM HASHTAGS
    GROUP BY hashtag
    ORDER BY count DESC
    LIMIT 10


---

Spark es un programa que usamos mucho (relacionado ) con lo que llamamos el mundo del BigData.

# BigData

Conjunto de técnicas que aplicamos al trabajar con datos cuando las técnicas tradicionales que hemos aplicado hasta la fecha ya no me sirven, o no me traen cuenta!
De hecho el uso más habitual de las técnicas de BigData es cuando no me traen cuenta, aunque me sirvan!

> EJEMPLO 1: Quiero hacer una lista de la compra. Producto x Cantidad (Mercadona)
    Unos 50 productos. PROGRAMA? Bloc de notas
    Unos 500 productos. PROGRAMA? Excel
    Unos 500.000k. EXCEL? Se hace caquita? No puedo! MariaDB
    Unos 100.000.000k. MariaDB? Se hace caquita? No puedo! Microsoft SQL Server
    Unos 10.000.000.000k. Microsoft SQL Server? Se hace caquita? Oracle DB (Exadata)
    Y si sigo subiendo? Entonces qué? Que servidor encuentro ya? Más grande y potente que un Exadata? Que BBDD más potente que Oracle?
> EJEMPLO 2: Tengo un pincho USB de 16Gbs... recien sacado de la caja... vacio.
    >  Le quiero grabar un fichero de 5Gb, puedo? DEPENDE! Del formato del sistema de archivos.
            FAT-16 -> 2Gb
            FAT-32 -> 4Gb
            Hay otros formatos... con sus límites!
            Y cuando quiero grabar un fichero de 6 eB? Entonces qué? Que formato de sistema de archivos encuentro que me permita grabar un fichero de 6 eB? Y si lo encuentro, que sistema operativo me permite usar ese formato de sistema de archivos? NTFS? KKTA, ZFS Nasti de plasti!
            De hecho, que Cabina tiene hueco para 6eB?
> EJEMPLO 3: clash royale 2vs2
        En un segundo, juagndo a ese juego, puedo hacer 2 movimientos (sacar 2 cartas)
        Pero, esa información debe llegar a los otros 3 jugadores. Hay que despachar 6 mensajes por segundo
        Pero somos 4 jugando -> 6 mensajes por segundo -> 24 mensajes por segundo
        Durante mucho tiempo, ha sido la app más descargada en AppStore como Google Play. Y en un momento dado puede haber 50k partidas en marcha.
        50k? -> 50k partidas en marcha -> 24 mensajes por segundo -> 1.200.000 mensajes por segundo
        Que servidor es capaz de despachar 1.200.000 mensajes por segundo? Olvidate!

Da igual lo que quiera hacer con los datos:
- Almacenarlos
- Procesarlos
- Analizarlos
- Transmitirlos

Hay un momento en que debido al:
- Volumen de datos tan grande
- A la velocidad con la se generan los datos.
- Complejidad de los datos (videos, imagenes, audios)

Las técnicas tradicionales que hemos aplicado (Oracle, Rabbit(Kafka en un servidor... o clustercito), etc...) ya no me sirven.
Quizás no tengo ni siquiera FORMATO DE ARCHIVOS para guardar archivos tan grandes.

En este caso, busco una alternativa, y esa alternativa es el BigData, que tiene que ver más con INFRAESTRUCTRA que con SOFTWARE (realmente es una combinación de ambos)

Lo que voy a hacer es en lugar de usar un megaservidor con un megaprograma, voy a usar un cluster(granja) de maquinas de mierda (COMMODITY HARDWARE: 16gbs i7)... pero tengo 400 de esas máquinas, y las voy a usar todas como si fueran una sola... aprovechando:
- Su capacidad de procesamiento conjunta
- Su capacidad de almacenamiento conjunta
- Su capacidad de transmisión conjunta

Los primeros que tuvieron problema con esto fue la gente de google... Estaban scrapeando la web.
Montaron un producto: Google BigTable. No solo eso, publicaron 2 papers, explicando cómo lo habían hecho:
- GFS (Google File System): Un sistema de archivos distribuido que me permite guardar archivos tan grandes como 6eB, y que me permite acceder a ellos de forma rápida. Ese sistema de archivos toma un archivo y lo divide en trozos... y cada trozo se guarda en una computadora (de hecho en al menos 3 - replicación) del cluster. De esta forma, puedo guardar archivos tan grandes como quiera... guardándolos en paralelo en 400HDD (velocidad de lectura y escritura conjunta de 400HDD)
- Modelo de programación: MapReduce

Un hombrecillo, tomo esos papers y creo un producto de software Opensource y gratuito, implementando esos conceptos: Hadoop.
Hadoop ofrece 3 componentes:
- HDFS (Hadoop Distributed File System): Una implemenmtación Opensource del GFS de google
- MapReduce: Una implementación Opensource del modelo de programación MapReduce de google
- YARN: Un sistema de gestión de recursos que me permite gestionar el cluster (asignar tareas a las máquinas, monitorizar el estado del cluster, etc...)

Hadoop es el equivalente a un Sistema Operativo en el mundo BigData.
Es quien toma control de los recursos del cluster: (HDD, CPU, RAM) permitiéndome hacer uso de todos ellos como si fueran uno solo.
Yo mando a Hadoop una tarea (un programa) y Hadoop se encarga de ejecutarla en el cluster, aprovechando todos los recursos disponibles:
- Colocar los datos en RAM de todas las máquinas
- Repartir tareas entre los Cores de todas las máquinas
- Usar y planificar el almacenamiento en los HDD de todas las máquinas
Básicamente lo más básico que hace un sistema operativo, pero a nivel de cluster.

Para que vale un Sistema Operativo en la práctica? Realmente para poco. 
Lo que aporta valor a negocio, no es el SO (ni Hadoop) sino el software que se ejecuta sobre él.
Hay una colección gigante de software que se ejecuta sobre Hadoop, y que me permite hacer cosas muy chulas con mis datos:
- Almacenamiento/BBDD: HBase, Hive, Cassandra, MongoDB, etc...
- Procesamiento: Spark, Flink, Storm, etc...
- Análisis: Mahout, SparkMLlib, etc...
- Transmisión: Kafka, etc...

Pero... dónde entra Spark? Si hemos dicho que solo es una reimplementación del MapReduce de Hadoop...
Si... eso es. Es que el de Hadoop es mu malo. Cada vez que un nodo manda o recibe datos, lo primero que hace es escribir esos datos en el disco duro, y luego leerlos... y eso es muy lento. 

Spark implementa un motor MapReduce paralelo (y que sustituye) a MapReduce de Hadoop, pero que en lugar de escribir los datos en el disco duro, los mantiene en RAM ( y aún asi, sin persistencia, consigue Resiliencia), lo que hace que sea mucho más rápido.

En cuanto hay que aplicar cualquier tipo de procesamiento a mis datos, Spark es la mejor opción, y es por eso que se ha convertido en el motor de procesamiento más popular en el mundo BigData.

Spark al final, su uso principal está en la creación de ETLs.

> ETL? Script que me permite sacar datos de algún sitio, transformarlos y cargarlos en otro sitio.
    > Extract: Sacar los datos de algún sitio (BBDD, CSV, etc...)
    > Transform: Aplicar cualquier tipo de transformación a esos datos (limpieza, filtrado, agregación, etc...)
    > Load: Cargar esos datos transformados en otro sitio (BBDD, CSV, etc...)


En realidad a muchos procesos los llamamos ETLs... pero hay muchos subtipos:
    - ELT: Extract, Load, Transform. Primero saco los datos, los cargo en otro sitio, y luego los transformo allí.
    - TELT: Transform, Extract, Load. Primero transformo los datos, luego los saco de algún sitio, y luego los cargo en otro sitio.
    - ETLT: Extract, Transform, Load, Transform. Primero saco los datos, luego los transformo, luego los cargo en otro sitio, y luego los vuelvo a transformar allí.

La realidad es que spark más bien se encarga de la parte de Transform, aunque también tiene funcionalidades para Extract y Load, pero no es su fuerte.
Básicamente el extract y el load de Spark es a Kafka, HDFS.

---

# BBDD de producción.

En las BBDD de producción NO TENEMOS MUCHOS DATOS.
Mantenemos en ellas los datos VIVOS... lo que están bajo gestión!
Si no... las destrozo en rendimiento, y flipas con los backups.
De ellas continuamente estamos quitando datos! Datos ya gestionados / muertos.

A dónde llevamos esos datos? DataLake!
Un datalake es un repositorio de datos en crudo... según salen de las BBDD de producción.
O nos los toco, o los toco poco.
En un datalake SI GUARDAMOS CANTIDADES INGENTES DE DATOS!

Los guardo para analizarlos luego por ejemplo? puede ser... o por motivos legales simplemente.
Pero la responsabildiad del datalake es simplemente guardar los datos.
Contra un datalake no hacemos ningún tipo de procesamiento, ni análisis, ni nada... simplemente guardamos los datos.

Cuando llega el momento de usar esos datos, lo que hacemos es volver a procesarlos... adecuándolos a una estructura más propicia para la tarea que quiero realizar... y los vuelvo a guardar en otro sitio... que es lo que llamamos un DataWarehouse.

DataWarehouse es un repositorio de datos ya procesados... con una estructura más propicia para la tarea que quiero realizar... y que me permite hacer análisis, consultas, etc... sobre esos datos.
    Por ejemplo, para hacer BI (Business Intelligence) sobre esos datos genero estructuras de datos en copo de nieve, o en estrella, etc... que me permiten hacer consultas muy rápidas sobre esos datos. Normalmente con datos ya DESNORMALIZADOS.
    Quizás lo que quiero es hacer Machine Learning sobre esos datos... entonces lo que hago es generar un dataset con una estructura más propicia para el entrenamiento de modelos de Machine Learning.


    BBDD de producción -> ETL -> DataLake -> ETL -> DataWarehouse
                           ^^                ^^
                          (1)                (2)
Esas cosas son las que solemos hacer con SPARK.
Sacamos datos de una BBDD de producción, los transformamos con Spark, y los guardo en ficheros en un datalake.
Ficheros planos: PARQUET

Posteriormente, cuando quiero hacer algo con esos datos, los vuelvo a cargar con Spark (desde PARQUET), los transformo de nuevo, y los guardo en otro sitio... que es el DataWarehouse (BBDD Oracle)

En (1) tendré muchos datos? 4 datos
En (2) tendré muchos datos? LA HUEVA! DATOS PARA ABURRIR! Un acumulado de años y años!
Entiendo que en 2 se decida usar Spark... pero en 1... para qué? no porque no haya otras formas tradicionales de hacerlo... sino porque NO ME TRAE CUENTA! 

En muchos casos, usamos técnicas bigdata, no porque no tengamos otras técnicas tradicionales que nos sirvan, sino porque esas técnicas tradicionales no nos traen cuenta! 

Cuándo proceso la ETL? 1 vez al día? una vez a la semana? una vez al mes? 1 vez cada hora?
Cuándos datos vienen en cada paquete? Es constante? NPI

Qué capacidad de procesamiento necesitaré para el siguiente paquete? Cuántos cores? Depende los datos que lleguen... yo que sé! Ni lo sé, ni me importa, ni me trae cuenta averiguarlo!
Y desde luego no quiero tener 10 máquinas paradas el día entero (que cuestan mucha pasta!) para cuando llegue un paquete para ser procesado (a las 3:00am un jueves)

Y directamente lo que hago es contratar un cluster de Spark en un cloud, con la consecuente flexibilidad del pago por uso! Y la facilidad de AUTOESCALADO!

Cómo está el cluster? Y YO QUE SE
Quizás a las 3:00pm apagado... o con 1 máquina procesando una mierdecilla que llegó
Quizás a las 3:00am con 100 máquinas procesando un paquetazo que llegó... durante 30 minutos...
Que luego quito 90 de esas máquinas, para procesar otro paquete...
Y luego subo 5 para otro
Y luego lo apago entero.


En SparkSQL en lugar de manejar RDDs y DataFrames

Un dataframe es una estructura tabular, mientras que un RDD es una estrucra lineal y secuencial. 

    RDD[T]      Es como un array de objetos de tipo T.

    Dataframe   Es como una tabla con filas y columnas, donde cada columna tiene un nombre y un tipo de dato, y cada fila es un registro de datos.

        Pero... hay una equivalencia entre ambos... de hecho un dataframe es un RDD de filas, pero con una estructura tabular y con información de los tipos de datos de cada columna.
            Dataframe -> RDD[Row]   Donde Row es un objeto que define Apache Spark SQL, que dentro tiene columnas con nombre y tipo de dato, y un valor para cada columna.

            Eso se lo come SparlSQL... Nosotros en muchos casos ni nos enteramos de que detrás del Dataframe hay un RDD de filas... pero es así!
            A no ser que queiera explicitamente convertir de uno a otro... que también se puede hacer.

                En algunos casos me interesa trabajar con RDDs y con operaciones MapReduce puras... y en otros me interesa trabajar con DataFrames y con operaciones SQL... que es mucho más fácil y rápido de escribir, y que a la vez es mucho más eficiente que las operaciones MapReduce puras.

                Y hay veces que hasta mezclamos... y parte del trabajo lo hagoi sobre el DataFrame, y parte del trabajo lo hago sobre el RDD... y eso también se puede hacer sin problema.


Las BBDD resuleven joins mediante distintos algoritmos de join 

- merge  <- Esto es hipereficiente, si quiero sacar TODOS LOS DATOS.

        TABLA1                                  TABLA 2
        id.  campo1   id_tabla2                id.  campo2
        1    valor1   1                        1    valor2  
        2    valor3   2                        2    valor4 <
        4    valor7   2         <              3    valor6  
        3    valor5   3                        4    valor8

    Si ambas tablas están ordenadas por el campo de join (id_tabla2 en tabla1, id en tabla2), entonces el algoritmo de merge me permite recorrer ambas tablas a la vez, y por cada fila de una tabla, buscar la fila correspondiente en la otra tabla... y eso es muy eficiente.

        TABLA RESULTADO
        id.  campo1   id_tabla2                id.  campo2
        1    valor1   1                        1    valor2  
        2    valor3   2                        2    valor4
        4    valor7   2                        2    valor4

    Solo tiene una pega... que los datos tiene que estar ordenados de la misma forma... 
    Oye.. yo que se... llamadme loco! Acaso no tenemos un ORDENADOR... pues que primero ordene los datos!
        LOCO!!!! Que tal se le da a un ordenador ordenar datos? COMO EL CULO (nlog(n))
        De hecho a las computadoras no las denominamos ordenadores por su capacidad de ordenar datos... sino porque somos uns copiotas de los gabachos.
        L'ordinateur... no por ordenar datos.. sino por processar órdenes.
        Y entonces.. cómo resulven las BBDD los merge? Los aplican mucho o no? UN HUEVO!
        Lo que pasa es que las BBDD tienen los datos PREORDENADOS, por un monton de campos a la vez...
        Ein? Si los datos solo los puedo tener ordenados por una secuencia única no por varias.
        De hecho si se puede... si duplico los datos... o los triplico o cuadriplico... que es lo que hacen las BBDD relacionas: INDICE!
        En Spark no hay nada de esto... por mucho que podamos usar SQL. NO NOS OLVIDEMOS!

- lookup <- Es el equivalente a un bucle:
     Voy recorriendo una tabla, y por cada fila de esa tabla, hago una consulta a la otra tabla para buscar el valor que necesito... y luego lo añado a la fila que estoy recorriendo. 
        Esto es eficiente? Si quiero sacar pocos datos, y el hacer la consulta a la otra tabla es rápido, puede ser eficiente... pero si quiero sacar muchos datos, o la consulta a la otra tabla es lenta, entonces no es eficiente.