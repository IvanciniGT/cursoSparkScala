
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


