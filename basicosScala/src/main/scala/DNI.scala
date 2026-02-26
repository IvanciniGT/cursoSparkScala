
/**
 * Either[Error, Valor]
 *
 * - Left(error)  => ha fallado (y me dice el motivo)
 * - Right(valor) => ha ido bien (y tengo el resultado)
 *
 * Aquí:
 *   Either[DniError, DNI]
 *     - Left(DniError)  = DNI inválido (formato mal, o letra no coincide)
 *     - Right(DNI)      = DNI válido (número y letra ya interpretados)
 */


/** Posibles motivos de error al interpretar/validar un DNI */
sealed trait DniError {
  def mensaje: String
}

object DniError {
  case object FormatoNoReconocido extends DniError {
    override val mensaje: String = "Formato de DNI no reconocido"
  }
  case object LetraNoCoincide extends DniError {
    override val mensaje: String = "La letra no coincide con el número"
  }
}


/**
 * Entidad de dominio: representa un DNI válido.
 * OJO: este tipo SOLO existe si el DNI es válido.
 * Si no es válido, NO creamos un DNI: devolvemos un DniError.
 */
final case class DNI(numero: Int, letra: Char) {

  /**
   * Formatea este DNI en texto.
   * Ejemplos:
   * - "12345678Z"
   * - "12.345.678-Z"
   * - "12.345.678 z"
   */
  def formatear(opciones: FormatoDNI = FormatoDNI()): String = {

    // 1) Número -> texto (con o sin ceros delante)
    val numeroBase =
      if (opciones.cerosDelante) f"$numero%08d" else numero.toString

    // 2) Número -> con puntos opcionalmente (12.345.678)
    val numeroConPuntos =
      if (!opciones.puntos) numeroBase
      else numeroBase.reverse.grouped(3).mkString(".").reverse

    // 3) Letra -> mayúscula/minúscula
    val letraFormateada =
      if (opciones.letraEnMayuscula) letra.toUpper else letra.toLower

    // 4) Separador opcional antes de la letra: "", "-", " ", etc.
    s"$numeroConPuntos${opciones.separador}$letraFormateada"
  }
}


/** Opciones para el método DNI.formatear(...) */
final case class FormatoDNI(
                             puntos: Boolean = false,
                             letraEnMayuscula: Boolean = true,
                             separador: String = "",
                             cerosDelante: Boolean = true
                           )


/**
 * Parser / factoría:
 * aquí vive la lógica de "texto -> (DNI válido) o (error)".
 */
object DNI {

  // Tabla oficial de letras del DNI (posición = numero % 23)
  private val letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE"

  // Acepta:
  // - 12345678Z
  // - 12.345.678-Z
  // - 12.345.678 Z
  // - 123.456-Z   (formatos con menos dígitos también)
  private val regexDni =
    """^(([0-9]{1,2}(\.[0-9]{3}){2})|([0-9]{1,3}\.[0-9]{3})|([0-9]{1,8}))[ -]?[A-Za-z]$""".r

  /**
   * Intenta interpretar y validar un DNI.
   *
   * Devuelve:
   * - Right(DNI(numero, letra)) si es válido
   * - Left(DniError) si es inválido (y por qué)
   */
  def parse(textoOriginal: String): Either[DniError, DNI] = {

    // 1) Validación rápida de "forma" general con regex
    if (!regexDni.matches(textoOriginal)) {
      return Left(DniError.FormatoNoReconocido)
    }

    // 2) Normalizamos el texto para poder trabajar fácil:
    //    - quitamos puntos, espacios y guiones
    //    - pasamos todo a mayúsculas
    val textoLimpio =
      textoOriginal
        .replace(".", "")
        .replace(" ", "")
        .replace("-", "")
        .toUpperCase

    // 3) Separamos letra y número:
    //    - la letra es el último carácter
    //    - el resto es el número
    val letraLeida = textoLimpio.last
    val numeroComoTexto = textoLimpio.dropRight(1)

    // 4) Convertimos el número a Int
    val numeroLeido: Int =
      try numeroComoTexto.toInt
      catch {
        case _: NumberFormatException =>
          return Left(DniError.FormatoNoReconocido)
      }

    // 5) Calculamos la letra esperada según la regla: numero % 23
    val letraEsperada = letrasValidas.charAt(numeroLeido % 23)

    // 6) Comprobamos la letra
    if (letraLeida == letraEsperada) {
      Right(DNI(numeroLeido, letraLeida))
    } else {
      Left(DniError.LetraNoCoincide)
    }
  }

  /** Helper: cuando solo quieres true/false y te da igual el motivo */
  def esValido(texto: String): Boolean =
    parse(texto).isRight
}

object EjemplosDNI {

  def main(args: Array[String]): Unit = {

    // Ejemplos variados:
    // - válidos (depende de la letra)
    // - letra mal
    // - formato mal
    val entradas = List(
      "12345678Z",
      "12.345.678-Z",
      "12345678A",
      "ABC123",
      "12.345.678  z"   // espacios + minúscula (lo normalizamos)
    )

    println("=== 1) Parsear cada entrada y reaccionar ===")

    for (texto <- entradas) {

      // parse devuelve Either:
      // - Left(error)  => DNI inválido
      // - Right(dni)   => DNI válido
      val resultado = DNI.parse(texto)

      resultado match {

        case Left(error) =>
          println(s"$texto -> ERROR: ${error.mensaje}")

        case Right(dni) =>
          // Probamos varios formateos para enseñar la ergonomía de uso
          val sinPuntos = dni.formatear(FormatoDNI(puntos = false, separador = "", letraEnMayuscula = true))
          val conPuntosGuion = dni.formatear(FormatoDNI(puntos = true, separador = "-", letraEnMayuscula = true))
          val conPuntosEspacioMinus = dni.formatear(FormatoDNI(puntos = true, separador = " ", letraEnMayuscula = false))

          println(s"$texto -> OK")
          println(s"   numero=${dni.numero}, letra=${dni.letra}")
          println(s"   sin puntos:          $sinPuntos")
          println(s"   con puntos y guion:  $conPuntosGuion")
          println(s"   con puntos y espacio (letra min): $conPuntosEspacioMinus")
      }
    }

    println()
    println("=== 2) Pipeline: quedarme solo con DNIs válidos ===")

    // toOption:
    // - Right(dni) -> Some(dni)
    // - Left(err)  -> None
    val validos: List[DNI] =
      entradas.flatMap(texto => DNI.parse(texto).toOption)

    println(s"Validos encontrados: ${validos.size}")
    validos.foreach(dni => println("  " + dni.formatear(FormatoDNI(puntos = true, separador = "-"))))

    println()
    println("=== 3) Boolean rápido ===")
    println(s"¿'12345678Z' válido? ${DNI.esValido("12345678Z")}")
    println(s"¿'ABC123' válido?     ${DNI.esValido("ABC123")}")
  }
}