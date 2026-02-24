object ProgramacionProcedural {

  def main(args: Array[String]):Unit = {
    funcionConValoresPorDefecto("Ivan")
    funcionConValoresPorDefecto("Menchu", 30)

    funcionConNumeroVariableDeArgumentos(1,2,3,4,5)
    funcionConNumeroVariableDeArgumentos(1)
    funcionConNumeroVariableDeArgumentos(1,2,3,4,5,6,7,8,9,0)
    funcionesAnidadas();
  }

  def funcionConValoresPorDefecto(nombre: String, edad: Number = 0): Unit = {
    println(s"Hola $nombre. Tienes $edad años")
  }

  def funcionConNumeroVariableDeArgumentos(numeros:Int*) : Unit = {
    println(numeros.sum)
  }

  def funcionesAnidadas():Unit = {
    def diaDeLaSemana(numero:Int):String = { // Closures: Funciones que devuelven una función.
      numero match { // Valores fijos
        case 1 => "Lunes"
        case 2 => "Martes"
        case 3 => "Miércoles"
        case 4 => "Jueves"
        case 5 => "Viernes"
        case 6 | 7 => "Fin de semana"
        case _ => "Día inválido"
      }
    }
    println("Dia de la semana 1 " + diaDeLaSemana(1))
    println("Dia de la semana 4 " + diaDeLaSemana(4))
    println("Dia de la semana 7 " + diaDeLaSemana(7))
  }

}
