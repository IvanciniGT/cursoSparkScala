// A diferencia de JAVA, en un fichero scala puedo tener más de una cosa definida:
// Por ejemplo: Varias clases, un objet un class.
// Esas cosas, su nombre, tampoco tiene porque coincidir con el nombre del fichero.

object BasicoScala {

  def main(args: Array[String]): Unit = { // Equivalente en java a la función main(String[] args)
    // Aquí iría el código que se ejecuta de mi programa.
    println("Hola Mundo!")  // Imprimir en consola. En java sería System.out.println("Hola Mundo!")
    // O el equivalente en python print("Hola Mundo!")
    ejemploSintaxisBasica()  // Llamada a la función que muestra la sintaxis básica de scala.
    operadoresBasicos()
    tiposDatosBasicos()
    options()
  }

  def tiposDatosBasicos(): Unit ={
    val numero1:Byte = 17            // -128 a 127 // 1 byte
    val numero2:Short = 17           // -32.768 a 32767 // 2 bytes
    val numero3:Int = 17             // -2^31  a 2^31... Del -2.050.000.000.000... 4 bytes
    val numero4:Long = 17L           // 8 bytes
    // Transcendencia del tipo de datos... númerico. Es importante?
    // - Funcionalmente no hay diferencia (más allá del rango de valores)
    // - En uso de RAM... solo con los Long (8 bytes)
    //   Byte, Short y Int, en RAM todos ocupan 4 bytes.
    // - En disco, si guado binario si habrá mucha diferencia!
    // Y más en nuestro mundo: BIGDATA

    val texto:String = "hola"
    val caracter:Char = 'A'
  }

  def options():Unit = {
    val potencialMensaje:Option[String] = puedeDevolverAlgo("HOLA") // En este caso el texto no llega a 5 caracteres. No devolverá nada (en realidad devuelve None)
    if(potencialMensaje.isEmpty) {
      println("Esta vacio el primero")
    }else{
      println("Esta lleno el primero")
    }
    val potencialMensaje2:Option[String] = puedeDevolverAlgo("HOLA AMIGOS") // En este caso el texto si llega a 5 caracteres. Devolverá el mismo texto (empaquetado con lacito!)
    if(potencialMensaje2.isEmpty) {
      println("Esta vacio el segundo")
    }else{
      println("Esta lleno el segundo " + potencialMensaje2.get)
    }
  }

  def puedeDevolverAlgo(mensaje:String): Option[String] = {
    if (mensaje.length > 5)
      Some(mensaje)
    else
      None
  }


  def condicion1(): Boolean = {
    true
  }
  def condicion0(): Boolean = {
    return true
  }

  def condicion2(): Boolean = false

  def operadoresBasicos(): Unit = { // En este caso, como la función no recibe
    // Argumentos, he optado por no poner los paréntesis, aunque también podría haberlos puesto. Es una cuestión de estilo.
    // Matemáticos:
    val suma = 1 + 2 - 6 * 3 / 2; // El orden de las operaciones es el mismo que en matemáticas, primero multiplicación y división, luego suma y resta.
    // Lógicos: (igual que java) En python: and, or, not
    // AND: && o &
    // OR: || o |
    // NOT: !
    // Cuando pongo 2, se dice que el operador trabaja en condiciones de corto circuito.
    // Es decir, en base al resultado del primer operando, se determina si el segundo se evalua o no:
    val resultado = condicion1() && condicion2()
    // Si condicion1 devolviera false, resultado ya va a ser false... con independencia de lo que devuelva condicion2
    // Si uso el operador && condicion2() ni siquiera será invocada
    // Si uso el operador & condicion2() si sería invocada, aunque ya da igual de cara al "resultado"
    // Operadores relacionales:
    // >, <, >=, <=, ==,    !=
    //       > =     = =   ! =

    // Condicionales:
    // IF Como statemnts.. iguales que en java o python
    val edad = 17;
    if (edad < 18) {
      println("Eres menor de edad")
    } else {
      println("Eres mayor de edad")
    }


  }

  def ejemploSintaxisBasica(): Unit = {
    // En scala no es necesario declarar el tipo de dato de las variables, el compilador lo infiere.
    // Pero si quiero, puedo declararlo explícitamente.
    val nombre: String = "Ivan"  // val es una variable inmutable, no se puede reasignar. Es como una constante.
    var edad: Int = 30           // var es una variable mutable, se puede reasignar.

    val variable = 33; // De qué tipo es esta variable?
    // Int?  ES LA BUENA!
    // Aunque no defina tipo de dato para la variable, lña variable lo infiere del
    // primer dato que se asigna. En este caso 33 -> Int
    // No tiene tipo como en python al no haberlo puesto? ERROR
    // variable = "hola"; // ERROR, no puedo asignar un string a una variable que el compilador ha inferido como Int.
    // La tendencia. No ponerlo para casos evidentes
    val nombreFichero = "1-basico.scala"; // El compilador infiere que es un String, no hace falta ponerlo explícitamente.
    //val informe = MiLibreria.generarInforme(nombreFichero);
    // en este caso, el compilador infiere que el tipo de dato de informe es
    // EL QUE SEA QUE DEVUELVE LA FUNCIÓN generarInforme, no hace falta ponerlo explícitamente.
    // Claro.. el compilador lo sabe.. y yo humano? NO... en este caso lo PONDRIA EXPLICITAMENTE
    // Para favorecer la lectura (MTNO, EVOLUCION) del programa

    // En scala no es necesario usar punto y coma al final de cada línea, pero si quiero, puedo usarlos.
    println("Mi nombre es " + nombre + " y tengo " + edad + " años.")  // Concatenación de strings.

    // En scala también puedo usar interpolación de strings, que es más fácil de leer.
    println(s"Mi nombre es $nombre y tengo $edad años.");  // La s antes del string indica que es un string interpolado.

    // En scala también puedo usar expresiones dentro de los strings interpolados.
    println(s"El año que viene tendré ${edad + 1} años.");  // La expresión dentro de ${} se evalúa y se inserta en el string.

    println(f"El año que viene tendré $edad%2f años.")  // La f antes del string indica que es un string formateado, puedo usar formato de printf.
    // Eso de %2f es un formato de printf, indica que quiero mostrar el número con 2 decimales. En este caso no tiene sentido porque edad es un Int, pero si fuera un Double, sí tendría sentido.
    // Eso sacaría: El año que viene tendré 30.00 años.
    // El f te permite aplcar esos formatos, con el % despues del ${}
    // Qué formatos tienes:
    // %d -> entero
    // %f -> número con decimales
    // %s -> string
    // En general no se usa tanto.. ya que la consola si acaso, la usamos más como logger.
  }

}