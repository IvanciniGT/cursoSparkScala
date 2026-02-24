object ProgramacionFuncional {

  def saluda(nombre: String) : String = s"Hola $nombre"
  def saludoFormal(nombre: String) : String = s"Buenos días, $nombre"







  // MUCHO CODIGO






  def main(args: Array[String]):Unit = {

    println(saluda("Menchu"))

    // Una función que recibe un String y produce un String
    val miFuncion: (String => String) = saluda // Referencia la función. Sería lo mismo que saluda _
    println(miFuncion("Federico"))

    imprimirSaludo(saludoFormal, "Menchu") // Qué sale por pantalla? NPI. Tengo que buscarlo.
    // Imaginad que no quiero reutilizar la lógica de saludoFormal.. Solo la estoy definiendo porque para llamar a imprimirSaludo, necesito pasar una función
    // Me trae cuentas el tener definida esa función en otro sitio del código? NO.. no me aporta legibilidad
    // En casos como este, TODOS LOS LENGUAJES que soportan programación funcional (python, java...) ofrecen una segunda forma de definir funciones:
    // EXPRESIONES LAMBDA: Ante to do es una expresión!
    // EXPRESION? Un trozo de código que devuelve un valor!
    val numero = 17       // Statement: Declaración, Sentencia ( = Oración/Frase )
    val otroNumero = 5+7  // Statement
                     /// Expresion: Trozo de código que devuelve un valor
    // Por ende: Una expresión lambda, es un trozo de código que devuelve una referencia a una función anónima creada dentro de la propia expresión.
    val miVariable: String => Option[String] = (nombre:String) => { // El tipo de dato de vuelta se infiere del RETURN (del código)
     if (nombre.length > 5 )
       Some(nombre)
     else
       None
    }
    ///// De aqui
    val miVariable2: String => Option[String] = (nombre) => { // El tipo de dato del argumento se infiere de la variable
      if (nombre.length > 5 )
        Some(nombre)
      else
        None
    }
    val miVariable3: String => Option[String] = nombre => { // Si solo hay un argumento, me puedo fumar los parentesis
      if (nombre.length > 5 )
        Some(nombre)
      else
        None
    }
    val miVariable4: String => Option[String] = nombre =>  if (nombre.length > 5 ) Some(nombre)  else  None

    imprimirSaludo( nombre => s"Qué pasa $nombre" , "Federico")
    imprimirSaludo( "Qué pasa " + _, "Federico") // CARAJOS !!!!
    // Más simplificado aún:
      // nombre => "Que pasa " + nombre
      // _      => "Que pasa " + _
      //           "Que pasa " + _

    imprimirSaludo2( "Qué pasa " + _ + " " + _ , "Federico", "García") // CARAJOS !!!!

  }

  def generarSaludo(nombre:String) : String = {
    return "Qué pasa "+ nombre
  }

  // "Que pasa " + _

  def imprimirSaludo(funcionGeneradoraDeSaludos: String => String, nombre:String):Unit = {
    val saludo = funcionGeneradoraDeSaludos(nombre)
    println(saludo)

  }
  def imprimirSaludo2(funcionGeneradoraDeSaludos: (String ,String)=> String, nombre:String, apellidos:String):Unit = {
    val saludo = funcionGeneradoraDeSaludos(nombre, apellidos)
    println(saludo)

  }

  def quedarmeConElNombreSiTieneMasDe5Caracteres(nombre:String): Option[String] = {
    if (nombre.length > 5 )
      Some(nombre)
    else
      None
  }

}
