
DNI. Validación de la Letra.
Cogemos el número y dividimos entre 23... y nos quedamos con el restod e esa división.

23.000.022 | 23
           +----------
        22   1.000.000
        ^^
        Esto es lo que me interesa: Estará en el rango 0-22.
        El ministerio da una tabla con la letra correspondiente a cada número.

Cómo guardo un DNI en una BBDD
- TEXTO(9) <--- Cuánto ocupa eso? Depende del juego de caracteres
                        ASCII
                        ISO-8859-1
                        UTF-8
                        Con cualquiera de esos: 1 caracter básico = 1 byte -> 9 bytes
- NUMERO + LETRA        4 bytes + 1 byte de la letra= 5 bytes
- NUMERO                4 bytes

Es decir, puedo llegar a una reducción del 55% del dato.

El almacenamiento es caro o barato hoy en día? Sigue siendo de las cosas más caras en un entorno de producción.
- Usamos otro tipo de soportes de almacenamiento: x5-x10 en el precio
- En un entorno de pro hacemos al menos 3 copias de cada dato.
  Para tener los 2 tbs, necesito 3x2 discos de 2tbs : x 15-x30
- Y ahora backups: x2.5
Y resulta que al echar cuentas: 1Tb -> 10.000€
Y en casa me cuesta 55 €

---

# MAVEN

Me permite automatizar tareas:
- Compilación
- Ejecución de pruebas
- Generar una imagen docker con mi aplicación
- Desplegar mi aplicación en un servidor de producción
- Mandarla a un repo de git
- Solocitar un análisis a SonarQube
- ...

Maven no sabe hacer la "o" con un canuto!
Cualquier tarea que quiera automatizar quien la ejecuta SIEMPRE es un plugin.
Maven delega la ejecución de tareas a los plugins.

Maven define un ciclo de vida para un proyecto de software. Habla de etapas o GOALS. Esos goals (la mayor parte) son secuenciales:

  validate  -> compile          -> test                 -> package      -> verify -> install
                Compilación        Pruebas unitarias       .jar
                                                            Generar Documentación 

Qué tareas se ejecutan en cada fase/etapa/goal? Depende de mi configuración.
Maven trae una por defecto, simple, muchas veces suficiente. Pero si quiero, puedo definir mi propia configuración, y entonces en cada fase/etapa/goal se ejecutarán las tareas que yo haya definido.


---

# MapReduce

Apache Spark es tan solo una reimplementación del modelo map-reduce que incluye de serie Apache Hadoop.

Modelo de programación? Una forma de construir un programa... Cómo vamos a plantear el algoritmo.
No qué algoritmo concreto vamos a usar... sino cómo vamos a plantear el algoritmo.

El modelo de programación MapReduce es especialmente potente para procesar CONJUNTOS DE DATOS.

Cuando trabajamos ocn MapReduce, lo que vamos a hacer es aplicar sobre una colección de datos de entrada un conjunto de funciones map, acabando con una función reduce que me devuelve el resultado que me interese.

    COLECCION(Lista, Map, Array...) -> Map1 -> Map2 -> ... -> MapN -> Reduce -> RESULTADO

Tenemos 2 tipos de funciones:

## Funciones de tipo MAP

Una función de tipo MAP es una función que al aplicarla sobre una colección que soporte el modelo de programación MapReduce, me devuelve otra colección que siga soportando el modelo de programación MapReduce.
Hay un huevo de funciones de tipo MAP: Esas son las que puedo usar. PUNTO PELOTA

Funciones MAP:
- map:             coleccionDeDatos.map(funcionDeTransformacion) => coleccionDeDatos2

    donde: coleccionDeDatos2 es otra colección de datos donde cada elemento de coleccionDeDatos2 es el resultado de la funcionDeTransformacion aplicada sobre el correspondiente elemento de coleccionDeDatos.


    (1,2,3,4,5) -> Función de transformación: x2 -> (2,4,6,8,10)

    Me permite transformar TODOS LOS ELEMENTOS DE UNA COLECCION DE DATOS, a la vez, con una función de transformación que yo defina.
    A esa función de transformación, muchas veces la llamamos también función de mapeo, o función de mapeado, o función de mapeo de datos, etc...


    Pensad en el codigo de map(funcionDeTransformacion):

    ```scala
        def map(funcionDeTransformacion: String => String, coleccionInicial: List[String]): List[String] = {
            coleccionResultado = List()
            for (elemento in coleccionInicial) {
                elementoResultado = funcionDeTransformacion(elemento)
                coleccionResultado.add(elementoResultado)
            }
            return coleccionResultado
        }
    ```
- filter:         coleccionDeDatos.filter(funcionDeFiltro) => coleccionDeDatos2

    Donde: funcionDeFiltro es lo que llamamos un PREDICADO, es decir, una función que devuelve un valor booleano (true o false)

           coleccionDeDatos2 es otra colección de datos que incluye cada elemento de la colección Original que cumpla la función de filtro, es decir, cada elemento de la colección Original para el que la función de filtro devuelva true.

    (1,2,3,4,5) -> Función de filtro: esPar -> (2,4)

    Me permite filtrar los elementos de una colección de datos, a la vez, con una función de filtro que yo defina.

- sort/orderBy:     coleccionDeDatos.sortBy(funcionDeOrdenacion) => coleccionDeDatos2

    Donde: funcionDeOrdenacion es lo que llamamos una función de ordenación. Esa función habitualmente recibe 2 datos y devuelve un valor numérico que indica el orden relativo de esos 2 datos. Por ejemplo, si la función de ordenación devuelve un número negativo, eso indicará que el primer dato va antes que el segundo dato. Si devuelve un número positivo, eso indicará que el primer dato va después que el segundo dato. Y si devuelve 0, eso indicará que ambos datos son iguales a efectos de ordenación.

           coleccionDeDatos2 es otra colección de datos que incluye cada elemento de la colección Original ordenados según el resultado de aplicar la función de ordenación a cada elemento de la colección Original.

    (1,2,3,4,5) -> Función de ordenación: orden natural, primero pares -> (2,4,1,3,5)

                    funcionDeOrdenación(dato1, datos):
                        if(paridad es Igual) {
                            return dato1 - dato2
                        } else {
                            if(dato1 es par) {
                                return -1
                            } else {
                                return 1
                            }
                        }


    Me permite ordenar los elementos de una colección de datos, a la vez, con una función de ordenación que yo defina.

- flatten:
  
  Permite aplanar una colección de colecciones de datos, es decir, convertir una colección de colecciones de datos en una colección de datos.

  [(1,2,3), (4,5)] -> flatten -> (1,2,3,4,5)

- flatMap(funcion de transformacion)

    Esta funcion hace un map con la funcion de transformacion, y luego un flatten.

    [(1,2,3), (4,5)] -> flatMap(x2) -> internamente genera: [(2,4,6), (8,10)] -> flatten -> (2,4,6,8,10)

## Funciones de tipo REDUCE

Una función de tipo REDUCE es una función que al aplicarla sobre una colección que soporte el modelo de programación MapReduce, me devuelve un resultado que no es una colección que soporte el modelo de programación MapReduce.

    coleccion -> funcionA
    Si funcionA devuelve otra colección, a la que puedo seguir aplicándole funciones de tipo MAP, entonces funcionA es una función de tipo MAP.
    Si funcionA devuelve un resultado que no es una colección a la que pueda seguir aplicándole funciones de tipo MAP, entonces funcionA es una función de tipo REDUCE.
Hay un huevo de funciones de tipo REDUCE: Esas son las que puedo usar. PUNTO PELOTA

Cuando uso estas funciones de tipo MAP y REDUCE, lo que estoy haciendo es construir un programa usando el modelo de programación MapReduce.

Dicho de otra forma, el lenguaje o la librería de turno, me ofrecerá un conjunto de funciones de tipo MAP y REDUCE, y yo lo que haré es construir un programa usando esas funciones, es decir, usando el modelo de programación MapReduce.

Funciones:
- reduce
- dameLosPrimerosN     .take(n)    .limit(n)    .head(n)    
- count()
- sum()
- max()
---

(1,2,3,4,5,6) .map(x2) -> (2,4,6,8,10,12)
              .filter(>=10) -> (10,12)
              .map(x/2) -> (5,6)
              .filter(esPar) -> (6)
              .count() -> 1

Eso es un modelo de programaicón mapReduce... Lo he usado para dar lugar a mi algoritmo concreto... con mi lógica... Un tanto absurda!

---

# Montemos el sistema de trending topics de X (Antes Twitter)

Colección de tweets que los usuarios de X han publicado en un periodo de tiempo determinado.

    "En la playa con mis amigos #goodVibes#SummerLove... que guay!!!"
    "Estudiando en casa para recuperación #MierdaDeVerano#MierdaDeMates"
    "En el cine con mi novia #Planazo#Peliculón (incluso...#GoodVibes)"

    GoodVibes           -> 2
    SummerLove          -> 1
    ~~MierdaDeVerano    -> 1~~
    ~~MierdaDeMates     -> 1~~
    Planazo             -> 1
    Peliculón           -> 1

Palabras prohibidas: Caca, Culo, Pedo, Pis, Mierda!

Apliquemos programación MapReduce.

    map          (a cada tweet le hacemos un replace de "#" -> " #")
        EN CADA TWEET... "En la playa con mis amigos #GoodVibes#SummerLove... que guay!!!" -> "En la playa con mis amigos  #GoodVibes #SummerLove... que guay!!!"
        coleccionDeTweets -> coleccionDeTweetsConHashtagsSeparados
        List[String] -> List[String]

    flatMap
        map          (a cada tweet le aplicamos un split por todo lo que no es una letra o un #)
            "En la playa con mis amigos #GoodVibes #SummerLove... que guay!!!" -> ("En", "la", "playa", "con", "mis", "amigos", "#GoodVibes", "#SummerLove", "que", "guay")
            
            coleccionDeTweetsConHashtagsSeparados -> coleccionDeArraysDetokens (palabras|hashtags)
            List[String] -> List[Array[String]]


        flatten    Juntar todos los arrays en uno solo
            coleccionDeArraysDetokens ->  coleccionDeTokens
            List[Array[String]] -> List[String]

    filter        (empiezaPorCuadradito(token))
            coleccionDeTokens -> coleccionDeHashtags
            List[String] -> List[String]

    map      Dado un hashtag, le haga un substring(1) para quitarle el #
            "#GoodVibes" -> "GoodVibes"
            coleccionDeHashtags -> coleccionDeHashtagsLimpios
            List[String] -> List[String]

    map     hashtagLimpio -> normalizar el case
            "GoodVibes" -> "goodvibes"
            coleccionDeHashtagsLimpios -> coleccionDeHashtagsLimpiosYNormalizados
            List[String] -> List[String]
    
    filter  (contieneAlgunaPalabraProhibida(token))
            coleccionDeHashtagsLimpiosYNormalizados -> coleccionDeHashtagsLimpiosYNormalizadosYFiltrados
            List[String] -> List[String]
        
    countByValue      FUNCION DE REDUCCION
            coleccionDeHashtagsLimpiosYNormalizadosYFiltrados -> Map[Hashtag, Cuenta]
            List[String] -> Map[String, Int]

Se supone que esto se desarrolla (Google) para mejorar el rendimiento de transformaciones sobre colecciones enormes de datos. 
Este es el objetivo!

---




Eso es programación Funcional MAP/REDUCE...
Puedo escribir ese código en programación imperativa?

```scala
    val listaInicial = (1,2,3,4,5,6)
    var numeroDeResultadosFinal = 0
    for (elemento in listaInicial) {
        elemento1 = elemento * 2
        if (elemento1 >= 10) {
            elemento2 = elemento1 / 2
            if (esPar(elemento2)) {
                numeroDeResultadosFinal = numeroDeResultadosFinal + 1
            }
        }
    }   
    return numeroDeResultadosFinal
```


```
(1,2,3,4,5,6) .map(x2) -> (2,4,6,8,10,12)       // Aplica la funcion x2 a todos los elementos para producir una nueva coleccion de datos con los dobles
              .filter(>=10) -> (10,12)          // Aplicar la funcion de filtro >=10 a todos los elementos para producir una nueva coleccion de datos con los que la cumplen 
              .map(x/2) -> (5,6)
              .filter(esPar) -> (6)
              .count() -> 1
```

Cuantos bucles hay ahi? 4.

En que universo MARVEL 4 bucles son más rápidos que 1 bucle? EN NINGUNO!
Y ... evidentemenete esas operaciones son en bucle!

Donde está el truco del almendruco? El truco es que realmente no se hacen 4 bucles! Se hace 1.
Dicho de otra forma:
(1,2,3,4,5,6) .map(x2) -> (2,4,6,8,10,12)       // Aplica la funcion x2 a todos los elementos para producir una nueva coleccion de datos con los dobles = MENTIRA!
              .filter(>=10) -> (10,12)          // Aplicar la funcion de filtro >=10 a todos los elementos para producir una nueva coleccion de datos con los que la cumplen = MENTIRA!
              .map(x/2) -> (5,6)                // Aplica la funcion x/2 a todos los elementos para producir una nueva coleccion de datos con los resultados = MENTIRA!
              .filter(esPar) -> (6)             // Aplicar la funcion de filtro esPar a todos los elementos para producir una nueva coleccion de datos con los que la cumplen = MENTIRA!
              .count() -> 1                     // Contar el número de elementos que hay en la colección de datos

Eso no es lo que en realidad OCURRE!

Y ahora viene un matiz sobre las funciones MAP/REDUCE:

- MAP: Se ejecutan en modo perezoso (lazy)
- REDUCE: Se ejecutan en modo ansioso (eager)

Cuando hacemos un map, no se hace la transformación... solo se anota que es necesario hacer esa transformación

Imaginad que me llega un paquete de hojas (folios) y digo... los quiero poner en mayúsculas... todos: .map(ponerEnMayusculas)
 Lo unico que hace esa funcion MAP es devolver exactamente el mismo paquete de hojas... pero con un postit en la portada, que diga: PonerEnMayusculas antes de leer.
Cuando digo: .filter(masDe50Caracteres) 
 Lo unico que hace esa funcion FILTER es añadir otro postit al paquete de hojas que ya tiene un postit, y ese nuevo postit dice: Filtrar por masDe50Caracteres antes de leer.

Esos postits se acumulan en pila.

La función de reducción es la que hace que se ejecute todo el proceso... Es la que ya necesita leer los datos. Y empieza a aplicar postits:

    def count():
        numeroDeElementos = 0
        while (hayDatos) {
            elemento = leerSiguienteElemento()
            // Aquí es donde se aplican los postits... en el orden inverso al que se han ido añadiendo a la pila de postits.
            if (ponerEnMayusculas(elemento) cumple masDe50Caracteres) {
                numeroDeElementos = numeroDeElementos + 1
            }
        }
        return numeroDeElementos

Lo que se aplican son las funciones una detrás de otra:

    for(elemento in listaInicial)
        if esPar(x/2(>10(x2(elemento)))) :
            numeroDeResultadosFinal = numeroDeResultadosFinal + 1

Esto es lo que ocurre!

En nuestro ejemplo de los tweets:

    val coleccionDeTweets = List(...)
    val trendingTopics = coleccionDeTweets 
                        .map(ponerEspacioAntesDeHashtag)         // Anotar que hay que poner en mayusculas antes de leer
                        .flatMap(splitPorLoQueNoEsLetraONumero)  // Anotar que hay que hacer un split por lo que no es letra o numero antes de leer y juntar los resultados en una sola colección de datos
                        .filter(empiezaPorCuadradito)            // Anotar que hay que filtrar por los que empiezan por cuadradito antes de leer
                        .map(quitarCuadradito)
                        .map(normalizarCase)
                        .filter(noContienePalabrasProhibidas)

                            // Y llegado a este punto, cuánto ha tardado el programa en ejecutase si tengo 10MMM de elementos? NADA!

                        .countByValue()   // Esto es lo que tardará la hueva!
    
Esta es la clave del modelo de programación MapReduce... y la clave de su rendimiento!
La librería/motor de procesamiento que implemente el modelo de programación MapReduce y que nosotros hayamos decidido usar (Scala Collections, Apache Spark, Java Streams,etc...) se encargará de optimizar la ejecución de las funciones de tipo MAP y REDUCE, para que el programa se ejecute lo más rápido posible.

Son librerías muy inteligentes... De hecho ni siquiera en muchos casos ejecutan el algoritmo completo sobre todos los datos....Si le pido al final, dame los 4 primeros...
Quizás no (o si) ejecute el algoritmo completo sobre todos los datos... pero en cuanto tenga los 4 primeros resultados quizás para.

Hay otra cosa con respecto a este modelo de programación. Los algoritmos a los que da lugar son PARALELIZABLES! Es decir, puedo tener muchos CORES DE CPU simultáneamente ejecutando mi programa... de hecho, el mimo programa (transformaciones map) pero sobre subcolecciones de los datos.



```scala
    val listaInicial = (1,2,3,4,5,6)
    var numeroDeResultadosFinal = 0
    for (elemento in listaInicial) {
        elemento1 = elemento * 2
        if (elemento1 >= 10) {
            elemento2 = elemento1 / 2
            if (esPar(elemento2)) {
                numeroDeResultadosFinal = numeroDeResultadosFinal + 1
            }
        }
    }   
    return numeroDeResultadosFinal
```

Cuantos cores se usan en ese ejemplo? 1... porque solo hay un hilo de ejecución. Hemos abierto hilos de ejecución? NO
Podríamos hacerlo:
- Abrir hilos
- Repartir los datos entre hilos
- Establecer puntos de sincronización para cuando cada hilo termine su parte del trabajo juntar los resultados parciales de cada hilo para obtener el resultado final.

Qué tan fácil es la programación concurrente? BUF !!!! Nada facil, ni ente universo ni en ninguno de los de marvel!

Eso es lo que hacen estas librerias por mi. O una de las cosas que hacen por mi.. hacen más!
