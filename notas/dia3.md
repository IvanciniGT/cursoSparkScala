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