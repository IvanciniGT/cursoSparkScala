# Modelo de programación map/reduce

Es una forma de crear programas para procesar colecciones de datos. Quién empieza con esto es Google.
Map/Reduce no es un algotirmo. El algoritmo lo hacemos nosotros con nuestra lógica. 
Lo que me ofrece es una forma de estructurar mi programa , de plantear ese algoritmo, de una forma concreta.

 Coleccion de Datos -> Map1 -> Map2 -> Map(n) -> Reduce -> Resultado


    resultado = coleccion.FUNCIONMAP(<args>)            // Comentarios
                         .FUNCIONMAP(<args>)
                         .FUNCIONMAP(<args>)
                         .FUNCIONMAP(<args>)
                         .FUNCIONREDUCE(<args>);

Tipos de funciones:
- Map     Devuelven umna colección de datos a la que puedo seguir aplciandole más maps o un reduce.
    - .map(funcionDeTransformacion)
    - .flatMap(funcionDeTransformacion)  (flatMap = map + aplanar)
    - .filter(funcionDeFiltrado)
    - .sortBy(funcionDeOrdenacion)

  Nota importante: Las funciones de tipo map se ejecutan de forma perezosa (lazy). Realmente solo anotan que una transformación debe ser realizada.

- Reduce  Devuelven un resultado final al que no puedo seguir aplicándole más maps o reduces. 
    - .reduce(funcionDeReduccion)
    - .reduceByKey(funcionDeReduccion) (reduceByKey = reduce + agrupación por clave)
    - .sum()
    - .count()
    - .take(n) | .limit(n) (take/limit = reduce + ordenación + toma de los n primeros elementos)

  Nota importante: Las funciones de tipo reduce se ejecutan de forma ansiosa (eager). Realmente ejecutan las transformaciones anotadas sobre la colección de datos y devuelven el resultado final.

  Pensad en un efecto dominó!

Los motores de procesamiento map/reduce toman decisiones acerca de cómo ejecutar las transformaciones anotadas sobre la colección de datos. Es decir, deciden:
- En qué orden ejecutar las transformaciones anotadas sobre la colección de datos.
- Si es necesario ejecuatrlas sobre todos los datos o solo sobre una parte de ellos.
- Si es necesario ejecuatr todas o no las transformaciones anotadas sobre la colección de datos.

Esta forma de plantear un algoritmo permite la paralelización sencilla de todos los pasos del algoritmo.
La claves es que podemos partir el conjunto de datos inicial en subconjuntos de datos más pequeños, y ejecutar los pasos que hemos definido (quizás no todos... pero muchos de ellos) sobre cada subconjunto de datos de forma paralela. Y luego, el motor de procesamiento map/reduce se encarga de combinar los resultados parciales obtenidos sobre cada subconjunto de datos para obtener el resultado final.

Todas las operaciones son paralelizables?

    .map PARALELIZABLE? SI: Transformar los elementos de una colección... Puedo transformar unos independientemente de otros? TOTALMENTE

        goodVibes       GoodVibes 2
        badVibes        BadVibes 1
        GoodVibes       summerLove 1
        summerLove                          \
        100k            1k                          GoodVibes: 3
        ---                                         BadVibes: 1
        loveMyFamily    loveMyFamily 1              summerLove: 2
        GoodVibes       GoodVibes 1         /       loveMyFamily: 1
        summerLove      summerLove 1
        100k            1k

    .filter? SI: Filtrar los elementos de una colección... Puedo filtrar unos independientemente de otros? TOTALMENTE

    .sort() Algo... Pero más complejo! Y no siempre trae cuenta!

        Yo puedo ordenar mis 100k internamente.. entre ellos
        Tu puedes ordenar tus 100k internamente.. entre ellos

        Pero luego tengo que juntar mis 100k con tus 100k... y ordenar los 200k... Y esto es un proceso complejo!

        Trae cuenta? Depende... 
            Si al final quiero TODOS LOS DATOS NO.
            Si solo quiero los primeros 20 datos ordenados, entonces sí.

            Porque de los 100k que tu ordenas y de los 100k que yo ordeno, solo tomo los 20 primeros de cada uno... y luego ordeno esos 40 datos... y me quedo con los 20 primeros... Y esto es un proceso sencillo!

            Un algoritmo de sort eficiente es de orden nlon(n)
             Si tengo 100k datos -> 100k * log(100k) = 100k * 5 = 500k operaciones
             Si tengo 200k datos -> 200k * log(200k) = 200k * 5.3 = 1.060k operaciones

            Pero solo si luego me quedo con pocos datos.. Si quiero todos... mal asunto!


---

# Trabajando con Spark


Mi Máquina              Cluster de Spark
----------              ------------------------------------------------
Programa  ------------>  Maestro   --------------------->  Trabajador1
          datos + logica            subconjunto         
                                    de dato + logica        
                                                           Trabajador2 
                                                           ...
                                                           TrabajadorN


jvm(local)               jvm(maestro)       jvm(trabajador1)
                                            jvm(trabajador2)
                                            ...
                                            jvm(trabajadorN)


Mi programa, dónde se ejecuta? en Mi máquina!
Desde Mi Máquina, abro conexion al cluster... mediante el nodo maestro.
Y que le mando al maestro por el tubito de la conexión?
 - Datos: el conjunto de datos que quiero procesar.
 - Qué quiero hacer con los datos? PEGANDO UN POSIT ENCIMA DE LOS DATOS ! = LOGICA
     - map, filter, sort, flatmap, ...
        - Qué mandamos en todas esas funciones? Qué argumentos tienen? FUNCIONES: LOGICA

Bendita programación funcional! Que me permite inyectar lógica en tiempo de ejecución!
Y las funciones no son sino más DATOS que mando.


Las funciones que genero en tiempo de ejecución dónde se ejecutan? en los nodos trabajadores.

El maestro:
1. Preparar un plan de ejecución del trabajo
2. Parte el conjunto de datos en trozos...
3. Y va mandando esos trozos a los trabajadores... junto con la lógica que quiero aplicar sobre esos trozos de datos...



N Tareas sobre M Datos

Cómo puedo paralelizar el trabajo?
- Si tengo K trabajadores, puedo mandarles subconjuntos de datos a cada uno de ellos... para que cada uno ejecute las N Tareas
    -> APACHE SPARK

        Trabajador 1                Trabajador 2                 Trabajador 3              ...     Trabajador K
        Todas las tareas            Todas las tareas             Todas las tareas          ...     Todas las tareas
        Subconjunto de datos 1      Subconjunto de datos 2       Subconjunto de datos 3    ...     Subconjunto de datos K

- Mandar todos los datos a N Trabajadores,m donde cada trabajador se especializa en una tarea... y monto un pipe.

    Trabajador 1           Trabajador 2                Trabajador 3           ...     Trabajador N
    Tarea 1          --->   Tarea 2             --->   Tarea 3          --->  ...     Tarea N
    Todos los datos         Salida de tarea 1          Salida de tarea 2              Salida de tarea N-1

    -> APACHE STORM


---

> ¿Cuántos cores tiene mi cluster?  10
> ¿Cuántos datos tengo?             2.000.000 tweets  
> ¿Cuántos subpaquetes de datos estamos montando?  10 subpaquetes de datos de 200.000 tweets cada uno
> ¿Queremos eso? NI DE COÑA !
  ¿Por qué?
    > ¿Qué pasa si un nodo se cae cuando voy por el 199.999 tweet?  PERDEMOS TODO EL TRABAJO REALIZADO HASTA EL MOMENTO
      ¿Spark es capaz de recuperarse de ese problema? SI... pero eso no implica que no perdamos el trabajo realizado hasta el momento.
      Spark mandará esos 200.000 tweets a otro nodo... y el otro nodo tendrá que volver a procesar esos 200.000 tweets... y eso es un proceso lento... y no queremos eso! O me puede interesar tener paquetes más pequeños? Más pequeños!
    > Todos los nodos tardarán lo mismo en procesar sus 200.000 tweets? NO... dependerá de:
      - De la complejidad de los datos (longituds de los tweets, número de hashtags por tweet, número de indecencias por tweet, etc...)
      - ¿Todos los nodos van a tener la mima velocidad de CPU? Por qué? Quién asegura esto?
      - ¿Los nodos del cluster van a estar de brazo cruzados espándome llegar a mi para hacer mi trabajo?
        ¿O previsiblemente están haciendo muchas otras tareas de otros programas? Habrá huevon de cosas!
        Y TODO ESTO JUNTO hace que no todos los nodos tarden lo mismo en procesar los datos.
        Lo que significaría que puedo tener 9 nodos muy rápidos... que acaban en 1 minuto... y un nodo muy lento... que haya encolado el trabajo y que le haya llegado el trabajao más duro (datos más complejos) y tarde 10 minutos.
        Y yo como un gilipollas esperando a que ese nodo acabe mientras hay 9 nodos parados... de brazos cruzados. Tiene sentido? NINGUNO
        Y cómo lo podemos resolver? Haciendo paquetes más pequeños!
    
    A cada nodo no le vamos a UN UNICO PAQUETE DE DATOS...
    Si tengo 10 nodos... tendré 1000 paquetes de datos... y se los voy mandando a los nodos a medida que van acabando... y así evito el problema de los nodos lentos... y el problema de la caída de nodos (perdiendo mucho trabajo)... y el problema de la complejidad de los datos... y el problema de la velocidad de CPU... y el problema de las tareas que estén haciendo otros programas en los nodos...

    Llegados al absurdo, podría mandar paquetes de datos de 1 tweet. Quiero eso? RIDICULO! El problema es el overhead en las comunicaciones.
    Y Además, cada vez que mando un paquete de datos a un nodo, en ese nodo lo primero que se hace es levantar un jvm para ese trabajo.
    Cuánto tarda eso? Lo tendré que amortizar!
    Le mando yo que sé.. 10k tweets... o así. No hay una cifra mágica. Algo sensato.

    Aquí hay que sumar otro factor: Cada vez que se manda un paquete de trabajo a un nodo, se le mandan:
    - Los datos a procesar (el paquete de datos)
    - La lógica a aplicar sobre esos datos (las funciones que quiero ejecutar sobre esos datos)

    En cada comunicación (envío de un paquete) se vuelven a mandar las funciones! (overhead)
    Pero eso no es lo peor! Sabéis que se manda también?.. en nuestro ejemplo? la lista de palabras prohibidas...
    embebida dentro de la propia función de filtro. Cuando se declara esa función, al usar esa lista de palabras prohibidas, esa lista de palabras prohibidas se convierte en parte de la función... y cada vez que se manda la función, se manda también esa lista de palabras prohibidas... Menos mal que tenemos 5 palabras... y 10 paquetes.
    Si tuvieramos 200KB de palabras prohibidas... y 1000 paquetes... cada vez que se manda un paquete, se mandan 200KB de palabras prohibidas... y eso es un overhead brutal! Y no queremos eso!

    Aquí estamos haciendo una gilipollez de palabras prohibidas.. extrapolar el ejemplo:
    - Me llegan unos datos (IPs) de unos clientes y los quiero geoiposicionar.
    - Me llegan unos CP y quiero asignar el municipio o viceversa.

    Y tengo un listado de TODOS LOS PUEBLOS DE ESPAÑA... con CP... o cosas peores.

    Pues alé... Un único paquete... pa' que se mande solo una vez! Resuelto? NO...
    Entonces estamos otra vez con : ¿qué pasa si un nodo se cae cuando voy por el 199.999 tweet? PERDEMOS TODO EL TRABAJO REALIZADO HASTA EL MOMENTO... qué pasa si un nodo va lento?

    Para resolver este problema en Spark existe otro concepto: BROADCAST DE VARIABLES.
    Un broadcast es lo contrario de un acumulador (a nivel conceptual)
    - Acumulador: Variable que puede ser escrita por los nodos y que puede ser vista por mi programa
    - Broadcast: Variable que puede ser escrita por mi programa y que puede ser vista por los nodos
    
    Esa variable se manda una UNICA VEZ... a cada nodo... con independencia de los paquetes de datos que se le manden posteriormente.

---

> Tarea:

Tenemos un programa que va a recibir DNIs de personas...
(por ahora hacemos que se generen 10.000.000 DNIs aleatorios... )
Lo que queremos es validarlos con Spark (Map/Reduce)... para al final quedarme con los DNIs válidos... y con el número de DNIs inválidos que hay.

- La validación que la haga una función llamada validarDNI(dni) que devuelva un booleano indicando si el DNI es válido o no.
- En lugar de trabajar con Strings disfrazados de DNI:
    "12345678Z"
    Trabajemos con Objetos de tipo DNI
- NOTA: Lo que me llegan son TEXTOS/String