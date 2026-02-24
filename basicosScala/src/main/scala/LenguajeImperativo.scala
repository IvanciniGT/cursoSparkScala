object LenguajeImperativo {

  def main(args: Array[String]):Unit = {
    condicionales();
    bucles()
    patternMatch()
    gestionErrores()
  }

  def condicionales():Unit = {
    val edad = 16
    val resultadoPrimero: Unit =
    if (edad >= 18) {
      println("Eres mayor de edad")
    }else{
      println("Eres un yogurín!")
    }
    println("El resultado es "+ resultadoPrimero)
    // A diferencia de otros lenguajes de programación, en SCALA Los ifs devuelven un valor.
    val resultado: String = if (edad >= 18) {
      "mayor"
    } else if (edad >= 16) {
      "casi mayor"
    } else {
      "menor"
    }
    println("Eres " + resultado + " de edad")
  }

  def bucles(): Unit ={
    // For tradicional por índices:
    for (i <- 1 to 5) {  // Equivalente en python: range(1,6). Java: for (int i = 1; i<=5; i++)
      println(s"Voy por el $i")
    }
    for (i <- 1 until 5) {  // Equivalente en python: range(1,5). Java: for (int i = 1; i<5; i++)
      println(s"Voy por el $i")
    }
    // quiero iterar sobre los elemenmtos pares
    for (i <- 1 to 10 if i % 2 == 0) {
      println(s"Voy por el $i")
    }
    for (i <- 1 to 10 by 2) { // Metemos paso:: python: range(1,11,2). Java: for (int i = 1; i<=10; i=i+2)
      println(s"Voy por el $i")
    }

    // For each, para procesar colecciones de datos (listas, arrays, mapas(diccionario))
    val colores = Array("rojo", "verde","azul")
    for (color <- colores) {
      println ("Tengo el color " + color)
    }

    // Bucles dobles (anidados)
    // for i (1-10)
    //     for j (1-25)
    for (i<-1 to 10; j<- 1 to 3){
      println (s"Coordenada: [$i, $j]")
    }

    // While
    var contador = 1
    while (contador < 10){
      println("Voy por el : " + contador) // En Scala no hay el problema de python de cast explicito: "Voy por el " + str(contador)
      contador += 1
    }
    // Until (do-while)
    contador = 1
    do {
      println("Voy por el : " + contador) // En Scala no hay el problema de python de cast explicito: "Voy por el " + str(contador)
      contador += 1
    } while (contador < 10)


  }


  def patternMatch():Unit = { // Equivalente mejorado del switch de java
    val dia = 3
    val nombreDelDia = dia match { // Valores fijos
      case 1 => "Lunes"
      case 2 => "Martes"
      case 3 => "Miércoles"
      case 4 => "Jueves"
      case 5 => "Viernes"
      case 6 | 7 => "Fin de semana"
      case _ => "Día inválido"
    }
    println("Estamos a " + nombreDelDia)

    val nota = 85

    val calificacion = nota match { // RANGOS
      case n if n >= 90 => "Excelente"
      case n if n >= 80 => "Muy buena"
      case n if n >= 60 => "Buena"
      case n if n >= 40 => "Suficiente"
      case _ => "Insuficiente"
    }
    println("Tu nota es: " + calificacion)

    val dato: Any = 3 // "hola"
    val tipo = dato match {
      case n: Int => s"Es un entero $n"
      case t: String => s"Es un texto $t"
      case b: Boolean => s"Es un valor lógico $b"
      case _ => "Ni idea"
    }
    println("El dato " + tipo)
  }

  def gestionErrores():Unit = {
    var resultado = "1234".toInt     // toDouble /// toBoolean
    resultado *= 2
    println("El resultado vale " + resultado)

    val valorDevuelto = try{
      var resultado2 = "hola".toInt     // toDouble /// toBoolean
      resultado2 *= 2
      println("El resultado vale " + resultado2)
    }catch {
      case e: NumberFormatException => println("Excepcion de conversión numérica " + e)
      case e1: Any => println("Excepcion generica " + e1)
    }

    // OJO !
    // Igual que los ifs, los try /Catch devuelven un valor


    val valorDevuelto2 = try{
      var resultado2 = "hola".toInt     // toDouble /// toBoolean
      resultado2 * 2
    }catch {
      case e: NumberFormatException => 0
      case e1: Any => -1
    }
    println(valorDevuelto2)
  }
}
