import scala.util.Random

object GeneradorDNIs {

  // Tabla oficial letras DNI
  private val LetrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE"

  /** Calcula la letra correcta para un número */
  private def letraCorrecta(numero: Int): Char =
    LetrasValidas.charAt(numero % 23)

  /** Genera un DNI válido en formato "12345678Z" (8 dígitos + letra) */
  def generarDniValido(rnd: Random): String = {
    val numero = rnd.nextInt(100_000_000) // 0..99.999.999
    val letra = letraCorrecta(numero)
    f"$numero%08d$letra"
  }

  /** Genera un DNI inválido (varios tipos: letra mal o formato basura) */
  def generarDniInvalido(rnd: Random): String = {
    val modo = rnd.nextInt(3)

    modo match {
      case 0 =>
        // Formato inválido puro
        val basura = Array("Menchu", "ABC123", "12.34.567-X", "1234567", "123456789Z", "11.223.344%X")
        basura(rnd.nextInt(basura.length))

      case 1 =>
        // Formato correcto, pero letra incorrecta
        val numero = rnd.nextInt(100_000_000)
        val letraOk = letraCorrecta(numero)
        var letraMal = ('A' + rnd.nextInt(26)).toChar
        while (letraMal == letraOk) {
          letraMal = ('A' + rnd.nextInt(26)).toChar
        }
        f"$numero%08d$letraMal"

      case _ =>
        // Formato parecido, pero con separadores raros / letra en medio
        val numero = rnd.nextInt(100_000_000)
        val letra = letraCorrecta(numero)
        // Ej: "12.345.678--Z" o "1234-5678Z" (según tu regex será inválido)
        if (rnd.nextBoolean())
          f"$numero%08d".grouped(4).mkString("-") + letra
        else
          f"$numero%08d".grouped(2).mkString(".") + "--" + letra
    }
  }

  /**
   * Genera N DNIs (Strings), mezclando válidos e inválidos.
   * @param total cantidad total (ej. 1_000_000)
   * @param porcentajeValidos 0.0 .. 1.0 (ej. 0.7 = 70% válidos)
   */
  def generarLote(total: Int, porcentajeValidos: Double, seed: Long = 42L): Array[String] = {
    val rnd = new Random(seed)
    val out = new Array[String](total)

    var i = 0
    while (i < total) {
      val esValido = rnd.nextDouble() < porcentajeValidos
      out(i) = if (esValido) generarDniValido(rnd) else generarDniInvalido(rnd)
      i += 1
    }

    out
  }

  def main(args: Array[String]): Unit = {
    val total = 1_000_000
    val porcentajeValidos = 0.7

    val dnis = generarLote(total, porcentajeValidos)

    println(s"Generados: ${dnis.length}")
    println("Ejemplos:")
    dnis.take(20).foreach(println)
  }
}