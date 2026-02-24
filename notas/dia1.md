
# Entorno de trabajo para la formación.

- IntelliJ - EAP
- MAVEN c:\Usuarios\VUESTRO_USUARIO\.m2\settings.xml <---
  - Proxy banco
  - Artifactory del banco <--- Cuenta de usuario (TOKEN)

---

# Apache Spark desde el lenguaje de programación SCALA

- Lunes y Martes: Scala
- Miércoles: MapReduce / Spark
- Jueves/Viernes: Spark (Core, SQL)

---

# Qué es Spark?

Es una reimplementación del motor de procesamiento MapReduce de Hadoop con soporte en memoria RAM.

---

# SCALA

Es un lenguaje de programación.

                                  (bytecode)
  .java ---> compilarlos javac ---> .class ---> interpretados JVM
  .scala --> compilarlos scalac --> .class ---> interpretados JVM
  .kt -----> compilarlos kotlinc -> .class ---> interpretados JVM

SCALA es un lenguaje de programación que ofrece una sintaxis alternativa a la de JAVA para generar BYTECODE, que posteriormente es interpretado por la JVM (Java Virtual Machine).

JAVA es un lenguaje de programación extraordinariamente VERBOSO! Además... java tiene algunas CAGADAS importantes en su sintaxis.

## Características básicas de Scala como lenguaje de programación:

### Tipado estadíco (o fuerte) frente a tipado dinámico (o débil).

En todo lenguaje de programación manejamos datos... y esos datos son de distintos tipos: str(String), int(Integer), float(Float), bool(Boolean), etc...

```py 
texto = "hola"              # Definir la variable texto. Se asigna la variable al valor "hola" (str, ya que va entre comillas)
```

```java
String texto = "hola";      // Definir la variable texto de tipo String. Se asigna la variable al valor "hola" (String, ya que va entre comillas)
                            // 1. Crear un dato de tipo String con el valor "hola" en RAM (cuaderno de cuadrícula)
                            // 2. Definir una variable llamada texto, de tipo String (es decir, puede apuntar a objetos/datos de tipo String)
                            //    Una variable sería como un postit... en este caso de los azules (los que apuntan a Strings)
                            // 3. Asignar la variable texto al dato de tipo String con el valor "hola" (es decir, pegar el postit azul en el cuaderno de cuadrícula, al lado del dato "hola")

texto = "adios";
                            // 1. Crear un dato de tipo String con el valor "adios" en RAM (cuaderno de cuadrícula)
                            //    Dónde? En el mismo sitio que el dato "hola" (es decir, en el mismo cuadrícula del cuaderno)
                            //           En otro sitio <<<- Esta es la buena
                            // En este momento tengo 2 datos de tipo String en RAM: "hola" y "adios"
                            // 2. La variable texto ya está definida... lo que hago es despegarla del dato "hola" y pegarla al dato "adios" (es decir, despegar el postit azul del dato "hola" y pegarlo al dato "adios"). Reasigno la variable texto al nuevo dato "adios"
                            //           En este momento, el dato "hola" queda huérfano de variable (ninguna variable le apunta)... 
                            //           y la JVM (o en intérprete de Python, o el motoro de procesamiento de js) lo marca como BASURA 
                            //           (por ser irrecuperable).
                            //           Y quizás si o quizás no...en un momento dado entre el recolector de basura de la JVM (o el intérprete
                            //           de Python, o el motor de procesamiento de js) lo elimine de RAM (cuaderno de cuadrícula) para liberar
                            //           espacio para otros datos.
```

La diferencia entre estos 2 lenguajes que en JAVA las variables TIENEN UN TIPO DE DATO ASIGNADO, mientras que en PYTHON no.
- JAVA Es un lenguaje de tipado estático (o fuerte), ya que las variables tienen un tipo de dato asignado.
- PYTHON Es un lenguaje de tipado dinámico (o débil), ya que las variables no tienen un tipo de dato asignado.

Y esto marca totalmente la diferencia entre lenguajes.

El tipado débil o dinámico es una característica de ciertos lenguajes que los invalidan como lenguajes para producir software serio!

```py
def generarInforme(titulo, datos): # No sé que tengo que pasar a esto o que me devolverá!
  pass
```

SCALA entra dentro de los lenguajes de tipado estático (o fuerte), al igual que JAVA, C#, C++, etc...

### Paradigmas de programación que soporta.

"Paradigma de programación" es un nombre un tanto "hortera" que los desarrolladores damos a las formas de usar un determinado lenguaje para nuestras necesidades... Pero realmente no es algo propio de los lenguajes de programación... En los lenguajes naturales también lo tenemos:

> Felipe, pon una silla debajo de la ventana! <<< IMPERATIVO
> Felipe, debajo de la ventana tiene que haber una silla. Es tu responsabilidad <<< DECLARATIVO

En el mundo de la informática (desarrollo) hablamos de:
- Imperativo              Cuando le voy dando a la computadora instrucciones que debe ejecutar secuencialmente (de arriba a abajo)
                          A veces me interesa romper esa secuencia: Estructuras de control de flujo (condicionales, bucles, etc...)
                          PY, JAVA, SCALA soportan este paradigma.
- Procedural              Cuando agrupo secuencias de instrucciones en grupos que se denominan: procedimientos, funciones, métodos, subrutinas,
                          etc... (dependiendo del lenguaje de programación), que posteriormente puedo invocar (ejecutar) desde cualquier parte de mi código. 
                          Evidentemente: PY, JAVA, SCALA soportan este paradigma.
                          Ventajas/Motivos por el que crear funciones/métodos/procedimientos:
                          - Mejorar la estructura del código
                          - Evitar duplicaciones = Reutilizar código
- Funcional               Cuando el lenguaje me permite que una variable apunte a una función y posteriormente ejecutar la función desde la
                          variable entonces decimos que el lenguaje soporta el paradigma funcional.
                          - Python tiene un soporte muy pobre del paradigma funcional.
                          - Java no lo tuvo hasta la versión 1.8... y se le metió a calzador!
                          - Scala nace como lenguaje com amplio soporte del paradigma funcional. <<< Y esto marca la diferencia en el uso de SPARK.
                          El problema no es lo que es la programación funcional (que es muy simple).
                          El problema es lo que puedo llegar a hacer si el lenguaje de programación soporta el paradigma funcional:
                          - Definir funciones que aceptan otras funciones como parámetros (funciones de orden superior)
                          - Definir funciones que devuelven otras funciones como resultado (closure)
                          Al trabajar con programación funcional, hay un motivo adicional para definir funciones/métodos/procedimientos: 
                          - Artículo 33... No me quedan más narices... si quiero llamar a una función que me pide una función como argumento.
- Orientado a Objetos     Todo lenguaje de programación define ciertos tipos de datos. En función del tipo de dato, así las 
                          operaciones que puedo hacer:
                                          Que le caracteríza?               Qué puedo hacer?
                            str(String)   una secuencia de caracteres       upper()(toUpperCase()), lower()(toLowerCase()), length(), etc...
                            date(Date)    dia, mes, año                     caesEnBisiesto() caesEnJueves(). Suma 3 días

                          Hay veces que los tipos de datos que me ofrece un lenguaje no son suficientes para mis necesidades... y entonces me invento mis propios tipos de datos... y esos tipos de datos se denominan CLASES, con sus propias características y operaciones:

                            Persona       nombre, apellidos, dni            eresMayorDeEdad()
- ...
---

> "Un producto de software por definición es un producto sujeto a cambios y mantenimientos"

---

# MAVEN!

Maven no es un gestor de dependencias. Es un programa de automatización de tareas cómunes sobre proyectos de software. Principalmente JAVA, pero también otros lenguajes de programación como SCALA, KOTLIN, etc...

La realidad es que para otros lenguajes, solemos usar otras herramienats de automatización:
- KOTLIN: Gradle
- SCALA: SBT (SBT = Scala Build Tool)

Pero por algun motivo que no vamos a discutir ahora, en el banco se decidió que se use MAVEN para la autoamtización de tareas sobre proyectos de software en SCALA... y por eso lo vamos a usar nosotros también.

MAVEN me permite automatizar tareas:
- Compilación del proyecto
- Ejecución de tests
- Generación de documentación
- Generación de artefactos (jar, war, etc...) (EMPQUETADO QUE ENTREGO PARA SU EJECUCION EN EL ENTORNO DE PRODUCCIÓN)
- Gestión de dependencias (descarga de librerías de terceros, etc...)

MAVEN impone (la realidad es que es definible.. pero nadie la define) una estructura de proyecto concreta:

 CARPETA_PROYECTO/
  src/
    main/          # Irá lo necesrio para la ejecución de mi programa
      scala/       # Irá el código fuente de mi programa
      resources/   # Irá los recursos necesarios para la ejecución de mi programa 
                   # (ficheros de configuración, ficheros de datos, etc...)
    test/          # Irá lo necesario para la ejecución de mis tests automatizados
      scala/       # Irá el código fuente de mis tests automatizados
      resources/   # Irá los recursos necesarios para la ejecución de mis tests automatizados 
                   # (ficheros de configuración, ficheros de datos, etc...)
  pom.xml          # Archivo de configuración de MAVEN (dependencias, tareas, ...).  