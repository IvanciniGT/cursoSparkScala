# Introducción a Scala

## Clase = Tipo de datos custom

```py
class Persona:
    def __init__(self, nombre, edad):
        self.nombre = nombre
        self.edad = edad
    
    def saludar(self):
        print(f"Hola, me llamo {self.nombre} y tengo {self.edad} años.")

persona1 = Persona("Juan", 30)
persona1.saludar()  # Output: Hola, me llamo Juan y tengo 30 años.
print(persona1.nombre)  # Output: Juan
persona1.edad = 31
print(persona1.edad)  # Output: 31
```

```java
public class Persona {
    private String nombre;
    private int edad;   
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public void saludar() {
        System.out.println("Hola, me llamo " + nombre + " y tengo " + edad + " años.");
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}

Persona persona1 = new Persona("Juan", 30);
persona1.saludar();  // Output: Hola, me llamo Juan y tengo 30 años.
System.out.println(persona1.getNombre());  // Output: Juan
persona1.setEdad(31);
System.out.println(persona1.getEdad());  // Output: 31

/*
// Día 1
public class Persona {
    public String nombre;
    public int edad;   
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public void saludar() {
        System.out.println("Hola, me llamo " + nombre + " y tengo " + edad + " años.");
    }
}

// Día 2-100
Persona persona1 = new Persona("Juan", 30);
persona1.saludar();  // Output: Hola, me llamo Juan y tengo 30 años.
System.out.println(persona1.nombre);  // Output: Juan
persona1.edad = 31;
System.out.println(persona1.edad);  // Output: 31

// Día 101: Me planteo: Quiero que no me puedan poner una edad negativa.
Como tengo sitio en el código, me toca encapsular con setters y getter... que me dan un sitio para poder poner esa restricción:
public class Persona {
    public String nombre;
    private int edad;   
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public void saludar() {
        System.out.println("Hola, me llamo " + nombre + " y tengo " + edad + " años.");
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad >= 0) {
            this.edad = edad;
        } else {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
    }
}

Día 102: 300.000 tios (tias) kalasnikov en mano persiguiendome por la oficina. Su código no complila.

Persona persona1 = new Persona("Juan", 30);
persona1.saludar();  // Output: Hola, me llamo Juan y tengo 30 años.
System.out.println(persona1.nombre);  // Output: Juan
persona1.setEdad(31);
System.out.println(persona1.getEdad());  // Output: 31


Los getters y setters son una buena práctica... mejor dicho, su no uso (y tirar de variables públicas en JAVA es una mala práctica) para:
FACILITAR EL MANTENIMIENTO DEL CÓDIGO:
Para eviktarme esto, en JAVA me dicen: Aunque no te hagan falta para nada los getters y setters el día 1... ponlos EN TODAS TUS VARIABLES por si acaso el día de mañana quieres meter algo que los requiera... sin joder a nadie.
*/
```

En python, si quiero conseguir eso el día 101, puedo tirar del concepto de props:

```py
class Persona:
    def __init__(self, nombre, edad):
        self.nombre = nombre
        self._edad = edad  # Variable privada (convención)
    def saludar(self):
        print(f"Hola, me llamo {self.nombre} y tengo {self._edad} años.")

    @property
    def edad(self):
        return self._edad

    @edad.setter
    def edad(self, nueva_edad):
        if nueva_edad >= 0:
            self._edad = nueva_edad
        else:
            raise ValueError("La edad no puede ser negativa")

persona1 = Persona("Juan", 30)
persona1.saludar()  # Output: Hola, me llamo Juan y tengo 30 años.
print(persona1.nombre)  # Output: Juan
persona1.edad = 31
print(persona1.edad)  # Output: 31
```

Este concepto (property) Existe en Python, C#, JS, TS, SCALA, etc... pero no en JAVA... ASCO DE JAVA!


```java


```java
import lombok.Setter;
import lombok.Getter;
public class Persona {
    @Getter
    private String nombre;
    @Setter
    @Getter
    private int edad;   
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public void saludar() {
        System.out.println("Hola, me llamo " + nombre + " y tengo " + edad + " años.");
    }

}

Persona persona1 = new Persona("Juan", 30);
persona1.saludar();  // Output: Hola, me llamo Juan y tengo 30 años.
System.out.println(persona1.getNombre());  // Output: Juan
persona1.setEdad(31);
System.out.println(persona1.getEdad());  // Output: 31
```

```scala
class Persona(val nombre: String, var edad: Int) {
  def saludar(): Unit = println(s"Hola, me llamo $nombre y tengo ${edad} años.")
}

val persona1 = new Persona("Juan", 30)
persona1.saludar()  // Output: Hola, me llamo Juan y tengo 30 años.
println(persona1.nombre)  // Output: Juan
persona1.edad = 31
println(persona1.edad)  // Output: 31
```

```py
class Persona:
    def __init__(self, nombre, edad):
        self.nombre = nombre
        self.edad = edad
    
    def saludar(self):
        print(f"Hola, me llamo {self.nombre} y tengo {self.edad} años.")
```

Scala está pensado para escribir poco. Y eso hace que sea muy complejo de aprender... ya que por debajo hace mucha mágia.
JAVA y python son explícitos... y eso hace que sean más fáciles de aprender... pero a la larga, escribir código en JAVA o PYTHON es un coñazo... y escribir código en SCALA es un gustazo!

El día 101, si queremos meter esa limitación de que la edad no puede ser negativa, en SCALA, lo haríamos así:

```scala
class Persona(val nombre: String, private var _edad: Int) {
  def saludar(): Unit = println(s"Hola, me llamo $nombre y tengo ${edad} años.")        
  def edad: Int = _edad                      // Getter
  def edad_=(nuevaEdad: Int): Unit = {       // Setter
    if (nuevaEdad >= 0) {
      _edad = nuevaEdad
    } else {
      throw new IllegalArgumentException("La edad no puede ser negativa")
    }
  }
}   

// Esas funciones se invocan como si fueran variables:

val persona1 = new Persona("Juan", 30)
persona1.saludar()  // Output: Hola, me llamo Juan y tengo 30 años.
println(persona1.nombre)  // Output: Juan
persona1.edad = 31
println(persona1.edad)  // Output: 31
```
Cuando en scala invocamos a una función, si la función no tiene argumentos puedo invocarla sin necesidad de poner los paréntesis...

println(persona1.edad)
// Equivalente a println(persona1.edad()) // Invocando a la función edad() que es el getter de la variable privada _edad
// Solo que en SCALA esos paréntesis son opcionales...
// Y directamente las funciones con _ al final del nombre, se invocan como si fueran variables al hacer un set
  

El Unit de Scala es el equivalente al void de JAVA...
Aunque es diferente.
En java void significa que no se devuelve nada.
En scala Unit es una constante. Un tipo de dato que solo tiene un valor posible: Unit.
Y lo usamos cuando no queremos que una función devuelva nada explicitamente . En scala toda función devuelve algo (aunque ese algo sea UNIT)

```
def edad: Int = _edad
````

Eso es equivalente a:

```
def edad(): Int = {
    return _edad
}
```

En scala si una función no tiene argumentos, al definirla no necesito poner los paréntesis... y al invocarla tampoco... aunque si quiero, puedo ponerlos...
Además, si una función tiene solo una línea de código, puedo omitir las llaves

Y además, toda función siempre devuelve lo que devuelva la última línea de código que contiene, sin necesidad de poner explicitamente la palabra return... aunque si quiero, puedo ponerla...

ES SCALA EL CODIGO PARECE UN JEROGLÍFICO... ES MUY CRIPTICO... porque hay mucho que se omite, en favor de escribir MUY POCO CODIGO... y eso hace que sea un lenguaje muy difícil de aprender... pero a la larga, escribir código en SCALA es un gustazo!



```scala
class Persona(val nombre: String, var edad: Int) {
}

val persona1 = new Persona("Juan", 30)
```

Pregunta... dónde está ahí el constructor? qué es el cuerpo del constructor.
Lo que hago al declarar la clase es poner entre parentesis (justo detrás del nombre de la calse) la firma (signatura) del constructor...

```java
public class Persona {
    private String nombre;
    private int edad;   

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
}

Persona persona1 = new Persona("Juan", 30);
```

Y aquí se complica.

De entrada, las asignaciones de las variables de instancia (nombre y edad) se hacen en la propia firma (signatura) del constructor... y no en el cuerpo del constructor como en JAVA.
Cuando hemos puesto var o val delante de las variables de instancia, automáticamente se guarda la variable de instancia y se asigna el valor que se le pasa al constructor... sin necesidad de escribir nada más. Y tengo getters y setters, según sea var o val... sin necesidad de escribir nada más.
Además, ahí puedo poner si las variables son privadas o públicas... y eso me da un control total sobre el acceso a las variables de instancia... sin necesidad de escribir nada más.

```scala

class Persona(val nombre: String, private var edad: Int) {
  def saludar(): Unit = println(s"Hola, me llamo $nombre y tengo ${edad} años.")
}
````

Pero... y si quiero tener más código en el constructor? Imaginad que edad fuera un val (solo getter) INMUTABLE... pero quiero que tenga la restricción de valores no negativos... ¿cómo lo hago? 

```scala
class Persona(val nombre: String, private var edad: Int) {  // LINEA1

    if (edad < 0) {                                                             // Este código, que estyá tirado en medio de la clase
                                                                                // Es el cuerpo adicional del constructor
        throw new IllegalArgumentException("La edad no puede ser negativa")
    }
    
}   
```

Es decir, ese código scala, da lugar al mismo bytecode que este JAVA:

```java
public class Persona {
    private String nombre;  // LINEA1: val nombre: String
    private int edad;       // LINEA1: var edad: Int

    public Persona(String nombre, int edad) { // LINEA 1: (val nombre: String, var edad: Int)
        this.nombre = nombre; // Es implicito de la LINEA 1: val nombre: String
        if (edad < 0) { // Esto, estas tres lineas, son las que en scala tengo tiradas en la clase
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
        this.edad = edad;     // Es implicito de la LINEA 1: var edad: Int
    }

    public String getNombre() { // Todo esto es IMAPLICITO en la LINEA 1: val nombre: String
        return nombre;
    }
}
```

Y lo de tirado en el cuerpo de la clase es literal!

```scala
class Persona(val nombre: String, private var edad: Int) {  // LINEA1

    if (edad < 0) {
        throw new IllegalArgumentException("La edad no puede ser negativa")
    }
    
    def saludar(): Unit = println(s"Hola, me llamo $nombre y tengo ${edad} años.")

    if( nombre.isEmpty) { // Este código, que estyá tirado en medio de la clase
        throw new IllegalArgumentException("El nombre no puede estar vacío")
    }
}   
```

Cuál es el constructor en ese caso?

```java

    public Persona (String nombre, int edad) { // LINEA 1: (val nombre: String, var edad: Int)
        if (edad < 0) { // Esto, estas tres lineas, son las que en scala tengo tiradas en la clase
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
        if( nombre.isEmpty) { // Esto, estas tres lineas, son las que en scala tengo tiradas en la clase
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.edad = edad;     // Es implicito de la LINEA 1: var edad: Int
        this.nombre = nombre; // Es implicito de la LINEA 1: val nombre: String
    }
```

Unit en SCALA es un dato de un tipo (clase) UNIT que solo tiene una instancia posible.
Y por convenio, cuando quiero que una función no devuelva nada, le pongo como tipo de dato de retorno el UNIT. y eso hace que la función devuelva un dato Unit... como si fuera 33 o "Federico", solo que todo el mundo admite que ese dato Unit es un dato que no tiene ningún valor... y que se usa para indicar que la función no devuelve nada.

# clase que solo tiene una instancia posible: SINGLETON

Un singleton es un patrón de programación que me permite asegurar que una clase solo puede tener una única instancia:

```java
public class MiSingleton {

    private static final volatile MiSingleton instancia;
            // volatile... La variable puede ser modificada por muchos hilos. por ende: DESACTIVA EL CACHEO DE ESAS VARIABLE A NIVEL DE LA CACHE DE CPU.
    private MiSingleton() {
        if(instancia == null) { // Este evita la realización del synchronized si ya existe la instancia... para mejorar el rendimiento.
            synchronized (MiSingleton.class) { // Semaforo de programación sincrona multihilo.
                // Evitar race conditions (condiciones de carrera)
                // Si 
                if(instancia == null) { // Este garantiza que solo se cree una instancia de la clase, aunque haya muchos hilos intentando crearla al mismo tiempo.
                    instancia = new MiSingleton();
                }
            }
        }
    }

    public static MiSingleton getInstancia() {
        return instancia;
    }
}

MiSingleton singleton1 = MiSingleton.getInstancia();
```

La única forma de obtener una instancia de esa clase es a través del método estático getInstancia()... y ese método se encarga de crear una única instancia de la clase y devolverla... y si ya existe esa instancia, simplemente la devuelve... sin crear una nueva.

Sabeís como creo eso en scala?:

```scala
object MiSingleton

MiSingleton // Con esto accedo a la instancia.
```

Hecho!
En scala un object es una clase de tipo Singleton, de la que solo puede haber una instancia... y esa instancia se crea automáticamente la primera vez que se accede a ella... y se accede a ella a través del nombre del object... sin necesidad de llamar a ningún método... y sin necesidad de escribir ningún código adicional para garantizar que solo haya una instancia de la clase.

Unit es un singleton de SCALA:

```scala
object Unit
```

No de todas las clases que cree en el código querré generar multiples instancias... a veces, solo quiero una instancia de esa clase:
- Repositorio de datos
- Configuración de la aplicación
- Servicio 

En estos casos uso singleton... y en scala, para crear un singleton, simplemente creo un object... y ya está!

---
Reglas simples / Resumen:
- El contructor principal (podrá haber otros) se define:
  - Firma: Detras del nombre de la clase
  - Asignaciónes: Son implicita
  - Cuerpo adicional: Código que esté tirado en medio de la clase (donde sea... se va acumulando)
- Cuando quiero de una calse tener solo una instancia: object en lugar de class. Esto me da un patrón singleton.
- Cuando quiero que una función no devuelva nada, le pongo como tipo de dato de retorno el Unit.
- Al definir funciones:
  - Uso def, como en python.
  - Siempre despues de la firma va el signo =
  - Si la función tiene varias lineas, las agrupo entre llaves.
  - No hace falta poner return explicitito, si no lo pongo, la función devuelve lo que devuelva la última línea de código que contiene.
  - Si no recibe argumentos, no tengo que poner parentesis ni en la firma, ni en la llamada... aunque si quiero, puedo ponerlos.

---

# Option (en Java Optional...en python NO EXISTE EQUIVALENTE!)

```java
public class Diccionario {
    
    //...

    public boolean existe(String palabra){}

    public List<String> getSignificados(String palabra){
        //... Con lo que sea de código
    }

}
```
Entendemos que cada significado es un String

Que hacemos que devuelva getSignificados()? Listado de String. = MIERDA !!!!!

Para que queremos tipado estático en JAVA? (frente a python que no lo tiene)?

```py
def generarInforme(titulo, datos): # No sé que tengo que pasar a esto o que me devolverá!
  pass
```
COMUNICARME CON UNA FUNCION = Saber que leches le tengo que mandar y saber que leches me va a devolver.
El tipado estático se supone que me ayuda a saber como comunicarme con una función, solo con ver la firma (signatura). Sin ver el código.

```java
public void generarInforme(String titulo, List<String> datos) {}
```
```java
    public List<String> getSignificados(String palabra) throws NoSuchWordException{}
```

Viendo la firma de la función, sé como comunicarme con ella?
Es decir, sé lo que tengo que pasar y lo que me devolverá?          NO. NAdie en esta clase me puede decir como se compartará esa función.

Imaginad que tengo un diccionario de Español... y pido los significados de la palabra "MELÓN", que me devuelve?
Una lista de Strings con las definiciones ["Fruto del melón", "Persona con pocas luces"]

Imaginad ahora que tengo un diccionario de Español y pido los significados de la palabra "ARCHILOCOCO", que me devuelve?
NPI. Opciones:
- null
- Una lista vacía []
- Una excepción . En este caso mala práctica, aunque de bueno tiene que es explicito.
                  Pero uso una exception (computacionalmente es muy cara) para algo que no es excepcional.
                  Una Exception es para casos donde no se a priori (ni hay forma de averiguarlo) si algo va a pasar... solo que que podría pasar.

                  No tengo forma de saber si tendré un error o no de conexión a la BBDD hasta que no intente (TRY) conectarme a la BBDD.
                  Eso es excepcional. El saber si una palabra existe en un diccionario... no es excepcional... es lógica de negocio que un diccionario debería de implementar! 

Pues.,.. estoy jodido... resulta que esto del tipado estático me iba a ayudar con este tinglao... pero parece que no!
Y en JAVA 1.8 se incluye la clase Optional, para resolver estos escenarios. Desde esta versión de java, se considera una 
TERRIBLEMENTE HORRIBLE FATAL practica que una función devuelva null. NO SE HACE.
Y cuando una función me devolviese null, lo cambiamos por Optional. Reduce AMBIGÜEDAD, para facilitar manto y evolución del código.

```java
public Optional<List<String>> getSignificados(String palabra){}
```

En este caso me devuelve una lista de strings o no!

Un optional es como una caja... La función siempre me da la caja... pero esa caja puede venir llena con algo (en este caso con un listado de strings) o puede venir vacía (si la palabra no existe)... pero caja (optional) siempre me dan.

Y así evito ambigüedad. 

Este mismo concepto lleva en SCALA desde el día 1: Option[T] es un tipo de dato que puede contener un valor de tipo T o no contener ningún valor (None). Es una forma de representar la ausencia de valor sin usar null.

```scala
def getSignificados(palabra: String): Option[List[String]] = {}

// Al usarlo:

val potencialesSignificadosMelon: Option[List[String]] = getSignificados("MELÓN")
if (potencialesSignificadosMelon.isDefined) {
  val significadosMelon: List[String] = potencialesSignificadosMelon.get
  // Hacer algo con los significados de "MELÓN"
} else {
  // La palabra "MELÓN" no existe en el diccionario
}
```

Esto no evita el NullPointerException... solo me ayuda a nteneder que podría llegar un valor VACIO... nada... lo que en java sería un null
que provocase un NullPointerException... 

En SCALA una fucnión no puede devolver null de hecho! Podra devolver Unit... que si es un dato.
Pero claro... como hago entonces para que esa función me pueda devolver una List[String] o un Unit! Esto no puedo en scala.. que una función devuelva un dato de tipo A o uno de tipo B (Bendito TypeScript) ... para esto tenemos que usar por el artículo 33 (en JAVA porque podemos y queremos... aquí porque no hay alternativa) el tipo de dato Option[List[String]]... que es un tipo de dato que puede contener un List[String] o no contener nada (None)... y así evito el null y la ambigüedad y de paso podemos hacer algo... que en scala sino sería imposible ya que una función no puede devolver null.

---

En JAVA existe la palabra static, que podemos poner en funciones de clases.
En python hay equivalente? Tenemos la anotación @staticmethod para funciones estáticas... @classmethod para funciones de clase... y las funciones normales son funciones de instancia.

En cualquier de lops casos, sirven para definir funciones cuyo comportamiento no depende de la instancia de la clase... es decir, que no necesitan acceder a las variables de instancia (atributos) de la clase...

En JAVA:
```java
public class MiLibreriaMatematica {

    static int doble(int numero) {
        return numero * 2;
    }

    static int mitad(int numero) {
        return numero / 2;
    }

}

    MiLibreriaMatematica.doble(4); // Devuelve 8
    MiLibreriaMatematica.mitad(4); // Devuelve 2

    // Si no fueran estáticas:
    MiLibreriaMatematica miLibreria = new MiLibreriaMatematica();
    miLibreria.doble(4); // Devuelve 8
    miLibreria.mitad(4); // Devuelve 2
```

Esto, en java tiene 2 usos. Uno, resolver otra de esas ñapas de java.

En python:
```py
# libreria_matematica.py
def doble(numero):
    return numero * 2

def mitad(numero):
    return numero / 2


    import libreria_matematica

    libreria_matematica.doble(4) # Devuelve 8
    libreria_matematica.mitad(4) # Devuelve 2
```

En java no puedo hacer este tipo de código. Me obligan a meterlo todo en una clase.

Este es uno de los usos de java... En python este uso no es necesario.

Otro uso de esto es tener una función que conceptualmente pertenece a una clase... pero que no necesita acceder a las variables de instancia de esa clase... y entonces la hago estática para indicar que no necesita acceder a las variables de instancia de esa clase... y así evito confusiones a la hora de mantener el código.

```java
class Trabajador{

    public Trabajador(String nombre) {
        // Código para crear un trabajador
    }

    public void empezarATrabajar() {
        // Código para empezar a trabajar
    }

    public void dejarDeTrabajar() {
        // Código para dejar de trabajar
    }

    public static void pararTodosLosTrabajadores() {
        // Código para parar a todos los trabajadores
    }

}

Trabajador trabajador1 = new Trabajador("Juan");
Trabajador trabajador2 = new Trabajador("Pedro");
trabajador1.empezarATrabajar();
trabajador2.empezarATrabajar();
Trabajador.pararTodosLosTrabajadores(); // Esto para a todos los trabajadores, no solo a trabajador1 o a trabajador2
```

En Scala...cómo resolvemos este tinglao! Con los Objects

Si quisieramos montar una librería de operaciones matemáticas en scala, lo haríamos así:

```scala
object MiLibreriaMatematica {
  def doble(numero: Int): Int = numero * 2
  def mitad(numero: Int): Int = numero / 2
}
MiLibreriaMatematica.doble(4) // Devuelve 8
MiLibreriaMatematica.mitad(4) // Devuelve 2
```

Pero ... y el caso de los trabajadores? En scala tenemos un concepto potente para ello: COMPAÑION OBJECTS

```scala
class Trabajador(val nombre: String) {
  def empezarATrabajar(): Unit = {
    // Código para empezar a trabajar
  }

  def dejarDeTrabajar(): Unit = {
    // Código para dejar de trabajar
  }
}
object Trabajador {
  def pararTodosLosTrabajadores(): Unit = {
    // Código para parar a todos los trabajadores
  }
}

val trabajador1 = new Trabajador("Juan")
val trabajador2 = new Trabajador("Pedro")
trabajador1.empezarATrabajar()
trabajador2.empezarATrabajar()
Trabajador.pararTodosLosTrabajadores() // Esto para a todos los trabajadores, no solo a trabajador1 o a trabajador2
```

Puedo tener una clase y un objecto con el mismo nombre... y ambos forman lo que llamamos un companion object... y el companion object es como una clase estática que acompaña a la clase principal.

La gracia es que desde el companion object (object) podemos acceder a datos / funciones privadas de la clase principal (class) y desde la clase principal (class) podemos acceder a datos / funciones privadas del companion object (object)... 

---

En java todo programa necesita una función "main". Cuando ejecuto una clase, lo que hace la JVM es buscar esa función main y ejecutarla... y esa función main es el punto de entrada de la aplicación... es decir, el código que se ejecuta al arrancar la aplicación. Debe ser una función static.

```java

public class MiPrograma {
    public static void main(String[] args) {
        // Código que se ejecuta al arrancar la aplicación
    }
}
```

En python no es necesario tener una función main... Cuando ejecuto un fichero python, se ejecuta todo el código que hay en ese fichero... de forma imperativa

Qué apsa en scala? En scala también necesitamos una función main... y debe estar definida en un object.

```scala

object MiPrograma {
  def main(args: Array[String]): Unit = {
    // Código que se ejecuta al arrancar la aplicación
  }
}
```